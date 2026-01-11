package com.cathay.identify.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Quan trọng: Field nào null thì bỏ qua, không trả về JSON
public class ApiResponse<T> {

    @Builder.Default
    private int code = 1000; // Mặc định code 1000 là Thành công (Quy định riêng của bạn)

    private String message;

    private T result; // Generic Type: Có thể là User, List<User>, hoặc String...
}
