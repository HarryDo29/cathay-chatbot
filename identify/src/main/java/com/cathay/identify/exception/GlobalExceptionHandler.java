package com.cathay.identify.exception;

import com.cathay.identify.dto.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
        // 1. Xử lý AppException (custom exceptions)
        @ExceptionHandler(AppException.class)
        ResponseEntity<ApiResponse<?>> handleAppException(AppException exception) {
            ErrorCode errorCode = exception.getErrCode();
            ApiResponse<?> apiResponse = ApiResponse.builder()
                    .code(errorCode.getCode())
                    .message(errorCode.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    
        // 2. Xử lý Validation errors
        @ExceptionHandler(MethodArgumentNotValidException.class)
        ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException exception) {
            Map<String, String> errors = new HashMap<>();
            exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
            );
            
            ApiResponse<?> apiResponse = ApiResponse.builder()
                    .code(400)
                    .message("Validation failed")
                    .result(errors)
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }
    
        // 3. Xử lý JWT exceptions
        @ExceptionHandler({
            ExpiredJwtException.class,
            MalformedJwtException.class,
            SignatureException.class
        })
        ResponseEntity<ApiResponse<?>> handleJwtException(Exception exception) {
            ErrorCode errorCode = exception instanceof ExpiredJwtException 
                ? ErrorCode.TOKEN_EXPIRED 
                : ErrorCode.INVALID_TOKEN;
                
            ApiResponse<?> apiResponse = ApiResponse.builder()
                    .code(errorCode.getCode())
                    .message(errorCode.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        }
    
        // 4. Xử lý tất cả exceptions khác
        @ExceptionHandler(Exception.class)
        ResponseEntity<ApiResponse<?>> handleGenericException(Exception exception) {
            // Log chi tiết để debug
            System.out.println("Unhandled exception: " + exception);
            
            ApiResponse<?> apiResponse = ApiResponse.builder()
                    .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                    .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }

}
