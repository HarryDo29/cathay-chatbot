package com.cathay.identify.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @Email(message = "Email is invalid")
    @NotBlank
    String email;

    @Size(min = 8, message = "Password must have size longer than 8 characters.")
    @NotBlank
    String password;
}
