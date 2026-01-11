package com.cathay.identify.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Configuration
public class OAuth2ClientConfig {
    @Value("${google.client-id}")
    private String client_id;

    @Value("${google.secret}")
    private String secret;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(){
        ClientRegistration google = ClientRegistration.withRegistrationId("google")
                .clientId(client_id)
                .clientSecret(secret)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope("email", "profile")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://oauth2.googleapis.com/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName("sub")
                .redirectUri("http://localhost:8080/api/v1/identify/login/oauth2/code/{registrationId}")
                .clientName("Google")
                .build();
        return new InMemoryClientRegistrationRepository(google);
    }
}
