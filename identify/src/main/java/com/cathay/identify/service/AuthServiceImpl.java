package com.cathay.identify.service;

import com.cathay.identify.dto.request.auth.LoginRequest;
import com.cathay.identify.dto.request.auth.RegisterRequest;
import com.cathay.identify.entity.AccountEntity;
import com.cathay.identify.exception.AppException;
import com.cathay.identify.exception.ErrorCode;
import com.cathay.identify.interfaces.AuthService;
import com.cathay.identify.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountRepository accRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AccountEntity login(LoginRequest loginDto) {
        Optional<AccountEntity> result = accRepo.findAccountByEmail(loginDto.getEmail());
        AccountEntity account = result.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        val isValidPassword = passwordEncoder.matches(loginDto.getPassword(), account.getHash_password());
        if (isValidPassword){
            return account;
        }
        return null;
    }

    @Override
    public AccountEntity register(RegisterRequest registerDto) {
        Optional<AccountEntity> result = accRepo.findAccountByEmail(registerDto.getEmail());
        if (result.isPresent()){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        val hash_password = passwordEncoder.encode(registerDto.getPassword());
        AccountEntity account = AccountEntity.builder()
                .name(registerDto.getName())
                .email(registerDto.getEmail())
                .hash_password(hash_password)
                .build();
        return accRepo.save(account);
    }
}
