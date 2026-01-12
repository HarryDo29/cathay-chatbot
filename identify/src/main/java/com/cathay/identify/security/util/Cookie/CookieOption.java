package com.cathay.identify.security.util.Cookie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CookieOption {
    private int maxAge;
    @Builder.Default
    private boolean httpOnly = false;
    @Builder.Default
    private boolean secure = false;
    @Builder.Default
    private String path = "/";
}
