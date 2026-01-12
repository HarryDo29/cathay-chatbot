package com.cathay.identify.controller;

import com.cathay.identify.dto.request.auth.LoginRequest;
import com.cathay.identify.dto.request.auth.RegisterRequest;
import com.cathay.identify.dto.response.ApiResponse;
import com.cathay.identify.dto.response.refreshToken.RefreshTokenResponse;
import com.cathay.identify.entity.AccountEntity;
import com.cathay.identify.security.models.CustomAccountDetails;
import com.cathay.identify.service.AuthServiceImpl;
import com.cathay.identify.service.RefreshTokenImpl;
import com.cathay.identify.security.util.Cookie.CookieOption;
import com.cathay.identify.security.util.Cookie.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authManager;
    private final AuthServiceImpl authSer;
    private final RefreshTokenImpl rfSer;
    private final CookieUtil cookieUtil;

    @PostMapping("/login")
    public ApiResponse<AccountEntity> login (@RequestBody LoginRequest loginDto,
                                                      HttpServletResponse response){
//        AccountEntity account = authSer.login(loginDto);
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );

        CustomAccountDetails accDetails = (CustomAccountDetails) authentication.getPrincipal();
        RefreshTokenResponse tokens = rfSer.createRefreshToken(accDetails.getAccountId());
        CookieOption acOption = CookieOption.builder()
                .httpOnly(false)      // Để false nếu Frontend cần đọc bằng JS
                .maxAge(15 * 60)      // 15 phút * 60 giây = 900 giây
                .secure(false)        // Đổi thành TRUE nếu chạy https (Production)
                .path("/")            // Access token cần dùng cho toàn bộ app
                .build();
        cookieUtil.addTo(response, "access_token", tokens.getAccess_token(), acOption);

        CookieOption rfOption = CookieOption.builder()
                .httpOnly(true)       // BẮT BUỘC TRUE: Chống hacker lấy trộm qua XSS
                .maxAge(30 * 24 * 60 * 60) // 30 ngày * 24 giờ * 60 phút * 60 giây
                .secure(false)        // Đổi thành TRUE nếu chạy https (Production)
                .path("/")            // Tối ưu: Nên đổi thành "/api/v1/auth/refresh" nếu có thể
                .build();
        cookieUtil.addTo(response, "refresh_token", tokens.getRefresh_token(), rfOption);

        return ApiResponse.<AccountEntity>builder()
                .result(accDetails.getAccount())
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<AccountEntity> register (@RequestBody RegisterRequest registerDto,
                                                         HttpServletResponse response){
        AccountEntity account = authSer.register(registerDto);
        RefreshTokenResponse tokens = rfSer.createRefreshToken(account.getId());
        CookieOption acOption = CookieOption.builder()
                .httpOnly(false)
                .maxAge(15 * 60)
                .secure(false)
                .path("/")
                .build();
        cookieUtil.addTo(response, "access_token", tokens.getRefresh_token(), acOption);
        CookieOption rfOption = CookieOption.builder()
                .httpOnly(true)
                .maxAge(15 * 60)
                .secure(false)
                .path("/")
                .build();
        cookieUtil.addTo(response, "refresh_token", tokens.getRefresh_token(), rfOption);
        return ApiResponse.<AccountEntity>builder()
                .result(account)
                .build();
    }
}
