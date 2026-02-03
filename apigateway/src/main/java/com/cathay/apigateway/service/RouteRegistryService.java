package com.cathay.apigateway.service;

import com.cathay.apigateway.entity.ServiceEntity;
import com.cathay.apigateway.interfaces.IRouteServiceRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Service
public class RouteRegistryService {
    private volatile Map<UUID, ServiceEntity> serviceCache = Map.of();

    private final IRouteServiceRepository serviceRepo;

    public RouteRegistryService(IRouteServiceRepository serviceRepo) {
        this.serviceRepo = serviceRepo;
    }
    
    @PostConstruct
    public void init() {
        System.out.println("🔧 RouteRegistryService @PostConstruct: Loading services...");
        loadServices().block(); // Load synchronously during bean initialization
        System.out.println("✅ RouteRegistryService initialized with " + serviceCache.size() + " services");
    }

    public Mono<Void> loadServices(){
        return serviceRepo.getAllServices()
                .doOnNext(service -> System.out.println("📦 Loading service: " + service.getName() + " -> " + service.getPath()))
                .collectMap(ServiceEntity::getId)
                .doOnNext(map -> {
                    serviceCache = Map.copyOf(map);
                    System.out.println("✅ Total services loaded into cache: " + map.size());
                })
                .then();
    }

    public Collection<ServiceEntity> getServiceCacheMap() {
        return serviceCache.values();
    }
}
