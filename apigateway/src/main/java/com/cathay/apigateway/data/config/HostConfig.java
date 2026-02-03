package com.cathay.apigateway.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "app.domain")
public class HostConfig {
    List<HostData> hosts;

    @Data
    public static class HostData {
        private String domain;
        private boolean enable;
        private String environment;
    }
}
