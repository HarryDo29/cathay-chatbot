package com.cathay.apigateway.filter;

import com.cathay.apigateway.core.routing.MatchResult;
import com.cathay.apigateway.data.config.LimitPropertiesConfig;
import com.cathay.apigateway.entity.EndpointsEntity;
import com.cathay.apigateway.entity.HeaderRulesEntity;
import com.cathay.apigateway.entity.MethodRuleEntity;
import com.cathay.apigateway.service.EndpointRegisterService;
import com.cathay.apigateway.service.HeaderRuleService;
import com.cathay.apigateway.service.MethodRuleService;
import com.cathay.apigateway.util.ErrorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidationGlobalGateway implements GlobalFilter, Ordered {
    private final LimitPropertiesConfig limitPropertiesConfig;
    private final EndpointRegisterService endpointRegisterService;
    private final MethodRuleService methodRuleService;
    private final HeaderRuleService headerRuleService;
    private final ErrorHandler errorHandler;

    // Cache for header rules (O(1) lookup instead of O(n) stream)
    // Key: header name (lowercase), Value: HeaderRulesEntity
    private Map<String, HeaderRulesEntity> headerRulesCache = null;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();
        String method = req.getMethod().toString();
        String path = req.getURI().getPath().toString();
        log.info("Validating request: {} {}", method, path);
        // endpoint existed or not (or enabled or not)
        MatchResult result = endpointRegisterService.getEndpoint(path, method);
        if (result.getStatus() == MatchResult.Status.PATH_NOT_FOUND) {
            log.warn("Endpoint not found: {} {}", method, path);
            return errorHandler.writeError(exchange,
                    new NotFoundException("Endpoint not found"),
                    HttpStatus.NOT_FOUND);
        } else if (result.getStatus() == MatchResult.Status.PATH_NOT_FOUND) {
            log.warn("Method not allowed: {} {}", method, path);
            return errorHandler.writeError(exchange,
                    new RuntimeException("Method not allowed"),
                    HttpStatus.METHOD_NOT_ALLOWED);
        }
        if (!result.getEntity().isEnabled()){
            return errorHandler.writeError(exchange,
                    new NotFoundException("Endpoint is not found"),
                    HttpStatus.NOT_FOUND);
        }
        // validate by method
        MethodRuleEntity methodRule = methodRuleService.getMethodRule(method);
        // Tách logic check body ra riêng
        if (methodRule.isRequire_body()) {
            long length = req.getHeaders().getContentLength();
            long maxSize = methodRule.getMax_body_size();
            // Content-Length = -1 is meaned length not existed (or chunked)
            if (length <= 0) {
                log.warn("Missing Content-Length header");
                return errorHandler.writeError(exchange,
                        new RuntimeException("Body required"),
                        HttpStatus.BAD_REQUEST);
            }
            if (length > maxSize) {
                log.warn("Payload too large: {} > {}", length, maxSize);
                return errorHandler.writeError(exchange,
                        new RuntimeException("Payload too large"),
                        HttpStatus.PAYLOAD_TOO_LARGE);
            }

            // Check Content-Type (Chỉ check khi cần body)
            if (methodRule.isRequire_content_type()) {
                MediaType contentType = req.getHeaders().getContentType();
                if (contentType == null ||
                    !contentType.isCompatibleWith(MediaType.APPLICATION_JSON) &&
                    !contentType.isCompatibleWith(MediaType.APPLICATION_FORM_URLENCODED) &&
                    !contentType.isCompatibleWith(MediaType.MULTIPART_FORM_DATA)) {
                    log.warn("Unsupported Content-Type: {}", contentType);
                    return errorHandler.writeError(exchange,
                            new UnsupportedMediaTypeException("Unsupported Content-Type"),
                            HttpStatus.UNSUPPORTED_MEDIA_TYPE);
                }
            }
        }

        // 2. Validate Query Params (Check độc lập, không dùng else)
        MultiValueMap<String, String> params = req.getQueryParams();
        if (params.size() > limitPropertiesConfig.getMax_query_params()) {
            log.warn("Too many query params: {}", params.size());
            return errorHandler.writeError(exchange,
                    new RuntimeException("Too many query params"),
                    HttpStatus.BAD_REQUEST);
        }

        // 3. Validate Authentication (Private Endpoint)
        HttpHeaders headers = req.getHeaders();
        if ((result.getEntity().isPublic())) {
            String auth = headers.getFirst("Authorization");
            if (auth == null || auth.isBlank()) {
                log.warn("Missing Authorization header");
                return errorHandler.writeError(exchange,
                        new AuthenticationException("Missing Authorization header"),
                        HttpStatus.UNAUTHORIZED);
            }
            if (!auth.startsWith("Bearer ") || auth.length() <= 7) {
                log.warn("Invalid Bearer token format: {}", auth);
                return errorHandler.writeError(exchange,
                        new AuthenticationException("Invalid Bearer token format"),
                        HttpStatus.UNAUTHORIZED);
            }
        }
        // 4. Validate Headers Max Length
        // transform header rules from set to map for O(1) lookup
        if (headerRulesCache == null) {
            log.info("Building header rules cache...");
            headerRulesCache = headerRuleService.getHeaders().stream()
                    .collect(Collectors.toMap(
                            rule -> rule.getName().toLowerCase(),  // Key: lowercase header name
                            rule -> rule,                           // Value: HeaderRulesEntity
                            (existing, replacement) -> existing     // If duplicate, keep existing
                    ));
        }

        // Validate all present headers
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            List<String> headerValues = entry.getValue();

            // O(1) lookup instead of O(n) stream
            HeaderRulesEntity headerRule = headerRulesCache.get(headerName.toLowerCase());

            // If header not in rules, skip (allow unknown headers)
            // This allows proxy headers (X-Forwarded-*, X-Real-IP, etc.)
            if (headerRule == null) {
                log.debug("Unknown header (allowed): {}", headerName);
                continue;
            }

            // Validate max length for all values
            for (String headerValue : headerValues) {
                if (headerValue.length() > headerRule.getMax_length()) {
                    log.warn("Header {} exceeds max length: {} > {}",
                            headerName, headerValue.length(), headerRule.getMax_length());
                    return errorHandler.writeError(
                            exchange,
                            new RuntimeException(
                                    "Header '" + headerName + "' exceeds maximum length of " +
                                    headerRule.getMax_length() + " characters (current: " +
                                    headerValue.length() + ")"
                            ),
                            HttpStatus.BAD_REQUEST
                    );
                }

                // Security: Check for CRLF injection
                if (headerValue.contains("\r") || headerValue.contains("\n")) {
                    log.warn("Header {} contains invalid characters (CRLF injection attempt)", headerName);
                    return errorHandler.writeError(
                            exchange,
                            new RuntimeException("Header '" + headerName + "' contains invalid characters"),
                            HttpStatus.BAD_REQUEST
                    );
                }
            }
        }

        log.info(" Validation passed for: {} {}", method, path);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -20;
    }
}
