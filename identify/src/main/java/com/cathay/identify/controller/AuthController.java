package com.cathay.identify.controller;

import com.cathay.identify.dto.request.auth.LoginRequest;
import com.cathay.identify.dto.request.auth.RegisterRequest;
import com.cathay.identify.dto.response.ApiResponse;
import com.cathay.identify.dto.response.account.AuthenticationResponse;
import com.cathay.identify.dto.response.refreshToken.RefreshTokenResponse;
import com.cathay.identify.entity.AccountEntity;
import com.cathay.identify.service.AuthServiceImpl;
import com.cathay.identify.service.RefreshTokenImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceImpl authSer;
    private final RefreshTokenImpl rfSer;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login (@RequestBody LoginRequest loginDto){
        AccountEntity account = authSer.login(loginDto);
        RefreshTokenResponse tokens = rfSer.createRefreshToken(account.getId());
        AuthenticationResponse authAcc = AuthenticationResponse
                .builder()
                .token(tokens.getAccess_token())
                .authenticated(account)
                .build();
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authAcc)
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<AuthenticationResponse> register (@RequestBody RegisterRequest registerDto){
        AccountEntity account = authSer.register(registerDto);
        RefreshTokenResponse token = rfSer.createRefreshToken(account.getId());
        AuthenticationResponse authAcc = AuthenticationResponse
                .builder()
                .token(token.getAccess_token())
                .authenticated(account)
                .build();
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authAcc)
                .build();
    }
}
