package com.cathay.identify.interfaces;

import com.cathay.identify.dto.request.auth.LoginRequest;
import com.cathay.identify.dto.request.auth.RegisterRequest;
import com.cathay.identify.entity.AccountEntity;

public interface AuthService {
    AccountEntity login(LoginRequest loginDto);

    AccountEntity register(RegisterRequest registerDto);
}
