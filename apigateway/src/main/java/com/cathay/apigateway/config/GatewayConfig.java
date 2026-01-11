package com.cathay.apigateway.config;

import com.cathay.apigateway.filter.AuthenticationGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    public final AuthenticationGatewayFilterFactory authenticationFilter;

    public GatewayConfig(AuthenticationGatewayFilterFactory authenticationFilter){
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public RouteLocator customLocator(RouteLocatorBuilder builder){
        return builder.routes()
                // identify service
                .route("identify", r -> r
                        // 1. Predicate: Path=/api/v1/identify/**
                        .path("/api/v1/identify/**")

                        .filters(f -> f
                                // 2. Filter: StripPrefix=3
                                // Cắt bỏ 3 phần: /api, /v1, /identify
                                // Ví dụ: /api/v1/identify/auth/login -> /auth/login
                                .stripPrefix(3)

                                // 3. Filter: Authentication
                                // Lưu ý: Filter này sẽ nhận được đường dẫn ĐÃ BỊ CẮT (/auth/login)
                                .filter(authenticationFilter.apply(new AuthenticationGatewayFilterFactory.Config()))
                        )
                        // 4. URI: http://localhost:8081
                        .uri("http://localhost:8081"))
                .build();
    }
}
