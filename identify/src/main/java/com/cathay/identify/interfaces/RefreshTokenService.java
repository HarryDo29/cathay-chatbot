package com.cathay.identify.interfaces;

import com.cathay.identify.dto.response.refreshToken.RefreshTokenResponse;

public interface RefreshTokenService {
    RefreshTokenResponse createRefreshToken(String account_id);

    RefreshTokenResponse refreshAccessToken(String account_id);
}
