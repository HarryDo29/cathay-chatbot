package com.cathay.identify.dto.response.account;

import com.cathay.identify.entity.AccountEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private AccountEntity authenticated;
    private String token;
}
