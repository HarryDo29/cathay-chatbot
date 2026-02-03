package com.cathay.apigateway.service;
import com.cathay.apigateway.entity.EndpointsEntity;
import com.cathay.apigateway.interfaces.IEndpointServiceRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Set;

@Service
public class EndpointRegisterService {
    private final IEndpointServiceRepository endpointRepo;

    @Getter
    private volatile Set<EndpointsEntity> publicEndpoints = Set.of();

    public EndpointRegisterService(IEndpointServiceRepository endpointRepo) {
        this.endpointRepo = endpointRepo;
    }

    @PostConstruct
    public void init() {
        System.out.println("🔧 EndpointRegisterService @PostConstruct: Loading endpoints...");
        loadEndpoints().block(); // Load synchronously during bean initialization
        System.out.println("✅ EndpointRegisterService initialized with " + publicEndpoints.size() + " public endpoints");
    }

    public Mono<Void> loadEndpoints() {
        return endpointRepo.getAllEndpoints()
                .collectList()
                .doOnNext(endpoints -> publicEndpoints = Set.copyOf(endpoints))
                .then();
    }

    public EndpointsEntity isPublic(String path) {
        return publicEndpoints.stream()
                .filter(ep -> ep.getPath()
                        .equals(path)
                        && ep.isPublic()
                        && ep.isEnabled())
                .findFirst()
                .orElse(null);
    }

    public EndpointsEntity isEnabled(String path, String method) {
        return publicEndpoints.stream()
                .filter(ep -> ep.getPath().equals(path)
                        && ep.getMethod().equalsIgnoreCase(method)
                        && ep.isEnabled())
                .findFirst()
                .orElse(null);
    }
}
