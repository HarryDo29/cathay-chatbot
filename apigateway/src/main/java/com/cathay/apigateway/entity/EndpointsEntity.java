package com.cathay.apigateway.entity;

import com.cathay.apigateway.enums.EndpointMethod;
import lombok.Data;

//@Table("endpoints")
@Data
public class EndpointsEntity {
//    @Id
//    private UUID id;
    private String id;

    private String path;

    private EndpointMethod method;

//    @Column("service_id")
    private String serviceId;

//    @Column("is_enabled")
    private boolean enabled;

//    @Column("is_public")
    private boolean isPublic;

//    @CreatedDate
//    private LocalDateTime created_at;
//
//    @LastModifiedDate
//    private LocalDateTime updated_at;
}
