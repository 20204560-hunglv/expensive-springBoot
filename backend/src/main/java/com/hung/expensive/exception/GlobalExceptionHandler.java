package com.hung.expensive.exception;

import com.hung.expensive.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Why: Global Exception Handler để handle tất cả exceptions trong application
 * Design decision: Centralized error handling để consistent response format
 * Context: Handle validation errors, business logic errors và unexpected errors
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Why: Handle validation errors từ @Valid annotation
     * Context: MethodArgumentNotValidException được throw khi validation fail
     * 
     * @param ex MethodArgumentNotValidException
     * @return ResponseEntity với validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        
        // Why: Extract field errors từ exception
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        // Why: Return validation error response
        return ResponseEntity.badRequest().body(
            ApiResponse.error("Validation failed")
        );
    }
    
    /**
     * Why: Handle business logic exceptions
     * Context: RuntimeException từ service layer
     * 
     * @param ex RuntimeException
     * @return ResponseEntity với error message
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(
            ApiResponse.error(ex.getMessage())
        );
    }
    
    /**
     * Why: Handle unexpected exceptions
     * Context: Catch-all cho các exceptions không được handle
     * 
     * @param ex Exception
     * @return ResponseEntity với generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex) {
        // Why: Log error để debug
        ex.printStackTrace();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ApiResponse.error("Có lỗi xảy ra trong hệ thống")
        );
    }
}
