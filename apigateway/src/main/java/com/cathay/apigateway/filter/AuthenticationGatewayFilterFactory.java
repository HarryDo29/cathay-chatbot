package com.cathay.apigateway.filter;
import com.cathay.apigateway.config.RouteValidator;
import com.cathay.apigateway.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.NonNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AuthenticationGatewayFilterFactory extends
        AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private final RouteValidator routeValidator;
    private final JwtUtil jwtUtil;

    public AuthenticationGatewayFilterFactory(RouteValidator routeValidator, JwtUtil jwtUtil) {
        super(Config.class); // <--- DÒNG QUAN TRỌNG NHẤT: Báo cho lớp cha biết Config class
        this.routeValidator = routeValidator;
        this.jwtUtil = jwtUtil;
    }

    @Value("${internal.api.key}")
    private String internalApiKey;

    @Override
    public @NonNull GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (routeValidator.isSecured.test((ServerHttpRequest) exchange.getRequest())){
                try {
                    List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
                    if (authHeader == null || authHeader.isEmpty()) {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }

                    // validate authorization (`Bearer ...` ?) ()
                    if (!authHeader.getFirst().startsWith("Bearer ")) {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }

                    // Extract and verify JWT token
                    val claim = jwtUtil.extractToken(authHeader.getFirst().substring(7));
                    // check expiration
                    val expiration = claim.getExpiration();
                    if (expiration.before(new Date())) {
                        throw new RuntimeException("Access Token is expired!!");
                    }

                    // take account in4 from token
                    String account_id = claim.getSubject();
                    String email = jwtUtil.extractClaim(
                            claim, claims -> claims.get("email", String.class));
                    String role = jwtUtil.extractClaim(
                            claim, claims -> claims.get("role", String.class));

                    // Create new request with header contain account in4 and internal API key
                    ServerHttpRequest req = exchange.getRequest()
                            .mutate()
                            .header("X-User-Id", account_id != null ? account_id : "")
                            .header("X-User-Email", email != null ? email : "")
                            .header("X-User-Role", role != null ? role : "")
                            .header("X-Internal-API-Key", internalApiKey)  // Thêm internal API key
                            .build();
                    return chain.filter(exchange.mutate().request(req).build());
                } catch (ExpiredJwtException e){
                    //response as jwt expired
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                } catch (JwtException e){
                    System.out.println("Invalid token: " + e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            } else {
                ServerHttpRequest req = exchange.getRequest()
                        .mutate()
                        .header("X-Internal-API-Key", internalApiKey)  // ← Thêm key cho public endpoints
                        .build();
                return chain.filter(exchange.mutate().request(req).build());
            }
        };
    }

    public static class Config {
    }
}
