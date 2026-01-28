package com.cathay.apigateway.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.gateway")
public class EndpointConfig {
    List<Endpoint> endpoints;

    @Data
    public static class Endpoint {
        private String id;
        private String path;
        private String method;
        private String serviceId;
        private String enabled;
        private String isPublic;
    }
}
