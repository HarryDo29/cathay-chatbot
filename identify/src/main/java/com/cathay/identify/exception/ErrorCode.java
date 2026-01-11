package com.cathay.identify.exception;

public enum ErrorCode {
    USER_EXISTED(1001, "User đã tồn tại"),
    USER_NOT_FOUND(1002, "Không tìm thấy User"),
    INVALID_TOKEN(1003, "Token không hợp lệ"),
    TOKEN_EXPIRED(1004, "Token đã hết hạn"),
    UNAUTHORIZED(1005, "Không có quyền truy cập"),
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi hệ thống chưa được định nghĩa");

    private int code;
    private String message;

    ErrorCode (int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
