package com.hung.expensive.dto;

/**
 * Why: DTO cho API Response wrapper - chuẩn hóa response format
 * Design decision: Consistent response structure cho tất cả API endpoints
 * Context: Giúp frontend dễ dàng handle response và error messages
 */
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    
    // Why: Default constructor cần thiết cho JSON serialization
    public ApiResponse() {}
    
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    // Why: Static factory methods để tạo response dễ dàng
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }
    
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, null);
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
}

