package com.cathay.identify.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "refresh_tokens")
@Data // lombok giúp tự generate ra tất cả getter/setter
@NoArgsConstructor // constructor mặc định
@AllArgsConstructor // constructor có đầy đủ các field
@Builder
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @NotBlank
    @Column(name = "token", nullable = false )
    private String token;

    @Column(name = "createdAt")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updatedAt")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private AccountEntity account;
}
