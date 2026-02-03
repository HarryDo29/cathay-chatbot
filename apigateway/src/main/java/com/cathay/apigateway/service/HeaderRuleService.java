package com.cathay.apigateway.service;

import com.cathay.apigateway.entity.HeaderRulesEntity;
import com.cathay.apigateway.interfaces.IHeaderRuleRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
public class HeaderRuleService {
    @Getter
    private volatile Set<HeaderRulesEntity> headers = Set.of();

    private final IHeaderRuleRepository allowedHeaderRepo;

    public HeaderRuleService(IHeaderRuleRepository allowedHeaderRepo) {
        this.allowedHeaderRepo = allowedHeaderRepo;
    }

    @PostConstruct
    public void init() {
        System.out.println("🔧 HeaderRuleService @PostConstruct: Loading allowed headers...");
        loadAllowedHeaders().block(); // Load synchronously during bean initialization
        System.out.println("✅ HeaderRuleService initialized with " + headers.size() + " allowed headers");
    }

    public Mono<Void> loadAllowedHeaders(){
        return allowedHeaderRepo.getAllAllowedHeaders()
                .collectList()
                .doOnNext(allowedHeaderList -> headers = Set.copyOf(allowedHeaderList))
                .then();
    }
}
