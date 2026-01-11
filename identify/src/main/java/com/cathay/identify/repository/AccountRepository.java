package com.cathay.identify.repository;

import com.cathay.identify.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, String> {

    // kiểm tra email đã tồn tại chưa
    boolean existsByEmail(String email);

    Optional<AccountEntity> findAccountByEmail(String email);

    @Query("SELECT u FROM AccountEntity u WHERE u.name LIKE CONCAT('%', :username, '%')")
    Optional<AccountEntity> findAccountsByName(
            @Param("username") String username
    );
}
