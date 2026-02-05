package com.cathay.apigateway.service;

import com.cathay.apigateway.entity.EndpointHeaderRuleEntity;
import com.cathay.apigateway.interfaces.IEndpointHeaderRuleRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EndpointHeaderRuleService {
    private final IEndpointHeaderRuleRepository endpointHeaderRuleRepo;
    private final HeaderRuleService headerRuleService;

    @Getter
    private volatile Map<String, List<EndpointHeaderRuleEntity>> endpointHeaderRule = Map.of();

    public EndpointHeaderRuleService(IEndpointHeaderRuleRepository endpointHeaderRuleRepo, HeaderRuleService headerRuleService) {
        this.endpointHeaderRuleRepo = endpointHeaderRuleRepo;
        this.headerRuleService = headerRuleService;
    }

    public Mono<Void> loadEndpointHeaderRules() {
        return endpointHeaderRuleRepo.loadAllEndpointHeaderRules()
                .collectList()
                .doOnNext(list -> {
                    endpointHeaderRule = Map.copyOf(
                            list.stream().collect(Collectors.toMap(
                                    item -> item.getEndpoint_id().toString(),
                                    item -> {
                                        List<EndpointHeaderRuleEntity> endpointHeaderRuleList = new ArrayList<>();
                                        endpointHeaderRuleList.add(item);
                                        return endpointHeaderRuleList;
                                    },
                                    (existing, replacement) -> {
                                        existing.addAll(replacement);
                                        return existing;
                                    }
                            )
                    )
                );
                }).then();
    }
}
