package com.cathay.apigateway.service;

import com.cathay.apigateway.entity.ServiceEntity;
import com.cathay.apigateway.interfaces.IRouteServiceRepository;
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

    public Mono<Void> loadServices(){
        return serviceRepo.getAllServices()
                .collectMap(ServiceEntity::getId)
                .doOnNext(map -> serviceCache = Map.copyOf(map))
                .then();
    }

    public Collection<ServiceEntity> getServiceCacheMap() {
        return serviceCache.values();
    }
}
