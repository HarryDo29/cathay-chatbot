package com.cathay.apigateway.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    // endpoints not authentication
    public static final List<String> openApiEndpoints = List.of(
            "/auth/register",
            "/auth/login",
            "/google/callback",
            "/login/oauth2/code/google",
            "/oauth2/authorization/google"
    );

    public Predicate<ServerHttpRequest> isSecured =
            req -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> req.getURI().getPath().startsWith(uri));
                    // check api in list or not: in --> false | not --> true
}
