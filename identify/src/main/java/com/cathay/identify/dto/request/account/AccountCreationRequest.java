package com.cathay.identify.dto.request.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountCreationRequest {
    @Size(min = 8, message = "Your name is invalid.")
    @NotBlank(message = "You must fill in your name.")
    String name;

    @Size(min = 8, message = "Your email is invalid!")
    @NotBlank(message = "You must fill in your email.")
    @Email(message = "Your email is invalid!")
    String email;

    @Size(min = 8, message = "Your password must be longer than 8 characters!")
    @NotBlank(message = "You must fill in your password")
    String password;
}
