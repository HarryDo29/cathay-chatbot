package com.cathay.apigateway.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;
import java.util.UUID;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.routes")
public class ServiceConfig {
    private List<RouteService> services;

    @Data
    public static class RouteService {
        private UUID id;
        private String name;
        private String path;
        private String url;
        private String enabled;
    }
}
