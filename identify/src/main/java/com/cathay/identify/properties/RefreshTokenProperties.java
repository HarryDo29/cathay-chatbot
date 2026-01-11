
package com.cathay.identify.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Data
@Component
@ConfigurationProperties(prefix = "jwt.refresh-token")
public class RefreshTokenProperties {

    private String secret;

    private Duration expire;
}