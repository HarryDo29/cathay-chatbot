package com.cathay.apigateway.service;

import com.cathay.apigateway.entity.MethodRuleEntity;
import com.cathay.apigateway.interfaces.IMethodRuleRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
public class MethodRuleService {
    @Getter
    private volatile Set<MethodRuleEntity> methodRuleSet = Set.of();

    private final IMethodRuleRepository methodRuleRepo;

    public MethodRuleService(IMethodRuleRepository methodRuleRepo) {
        this.methodRuleRepo = methodRuleRepo;
    }

    @PostConstruct
    public void init() {
        System.out.println("🔧 MethodRuleService @PostConstruct: Loading method rules...");
        loadMethodRules().block(); // Load synchronously during bean initialization
        System.out.println("✅ MethodRuleService initialized with " + methodRuleSet.size() + " method rules");
    }

    public Mono<Void> loadMethodRules() {
        return methodRuleRepo.getAllMethodRules()
                .collectList()
                .doOnNext(methodRuleList -> methodRuleSet = Set.copyOf(methodRuleList))
                .then();
    }

    public MethodRuleEntity getMethodRule(String method) {
        return methodRuleSet.stream()
                .filter(rule -> rule.getMethod().equalsIgnoreCase(method))
                .findFirst()
                .get();
    }
    
}
