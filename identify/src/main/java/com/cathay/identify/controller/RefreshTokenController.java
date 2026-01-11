package com.cathay.identify.controller;

import com.cathay.identify.dto.response.ApiResponse;
import com.cathay.identify.dto.response.refreshToken.RefreshTokenResponse;
import com.cathay.identify.service.RefreshTokenImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/refresh-token")
@AllArgsConstructor
public class RefreshTokenController {
    private final RefreshTokenImpl rfSer;

    @PostMapping
    public ApiResponse<RefreshTokenResponse> refreshAccessToken(@RequestHeader String account_id){
        RefreshTokenResponse result = rfSer.refreshAccessToken(account_id);
        return ApiResponse.<RefreshTokenResponse>builder()
                .result(result)
                .build();
    }
}
