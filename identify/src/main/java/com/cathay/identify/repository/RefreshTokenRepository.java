package com.cathay.identify.repository;

import com.cathay.identify.entity.AccountEntity;
import com.cathay.identify.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, String> {
    Optional<List<AccountEntity>> findTokenByAccountId(String accountId);
}
