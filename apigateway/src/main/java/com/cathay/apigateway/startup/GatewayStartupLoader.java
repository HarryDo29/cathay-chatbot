package com.cathay.apigateway.startup;

import com.cathay.apigateway.service.EndpointRegisterService;
import com.cathay.apigateway.service.RouteRegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GatewayStartupLoader implements CommandLineRunner {
    private final RouteRegistryService routeRegistryService;
    private final EndpointRegisterService endpointRegisterService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Starting to load services into Redis and in-memmory cache...");

        Mono.when(
                routeRegistryService.loadServices(),
                endpointRegisterService.loadEndpoints()
                ).doOnSuccess(v -> System.out.println("✅ Gateway bootstrap completed"))
                .doOnError(e -> System.err.println("❌ Gateway bootstrap failed: " + e.getMessage()))
                .subscribe();
        // Note: Using subscribe() here to run asynchronously during startup
        // Load all configurations from application.yml (app) to Redis and in-memory cache
    }
}
