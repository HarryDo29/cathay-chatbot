package com.cathay.identify.dto.request.account;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @Size(min = 8, message = "Current Password is invalid!")
    @NotBlank
    String current_pass;

    @Size(min = 8, message = "New password is invalid")
    @NotBlank
    String new_pass;

    @NotBlank
    String confirm_pass;

    @AssertTrue(message = "Password và Confirm Password không khớp")
    public boolean isPasswordMatching() {
        // Cần check null trước để tránh lỗi NullPointer
        if (new_pass == null || confirm_pass == null) {
            return true; // Để yên cho @NotBlank ở trên xử lý lỗi null
        }
        return new_pass.equals(confirm_pass);
    }
}
