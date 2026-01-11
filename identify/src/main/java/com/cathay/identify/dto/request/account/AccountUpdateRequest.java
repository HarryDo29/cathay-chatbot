package com.cathay.identify.dto.request.account;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountUpdateRequest {
    @Size(min = 10, max = 10, message = "Phone number is invalid")
    String phone;

    @Size(min = 1, message = "Name is invalid")
    String name;

    @URL
    String avtUrl;
}
