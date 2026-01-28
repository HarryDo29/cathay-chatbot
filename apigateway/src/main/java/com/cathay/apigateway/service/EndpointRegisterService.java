package com.cathay.apigateway.service;
import com.cathay.apigateway.entity.EndpointsEntity;
import com.cathay.apigateway.interfaces.IEndpointServiceRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Set;

@Service
public class EndpointRegisterService {
    private final IEndpointServiceRepository endpointRepo;
    private volatile Set<String> publicEndpoints = Set.of();

    public EndpointRegisterService(IEndpointServiceRepository endpointRepo) {
        this.endpointRepo = endpointRepo;
    }

    public Mono<Void> loadEndpoints() {
        return endpointRepo.getAllEndpoints()
                .map(EndpointsEntity::getPath)
                .collectList()
                .doOnNext(endpoints -> publicEndpoints = Set.copyOf(endpoints))
                .then();
    }

    public boolean isPublic(String path) {
        return publicEndpoints.contains(path);
    }
}
