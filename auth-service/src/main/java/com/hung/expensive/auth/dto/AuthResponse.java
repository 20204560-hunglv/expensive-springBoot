package com.hung.expensive.auth.dto;

/**
 * DTO cho Auth Response - đơn giản
 */
public class AuthResponse {
    
    private String message;
    private Long userId;
    private String username;
    private String email;
    private boolean success;
    
    // Constructors
    public AuthResponse() {}
    
    public AuthResponse(String message, Long userId, String username, String email, boolean success) {
        this.message = message;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.success = success;
    }
    
    // Static factory methods
    public static AuthResponse success(String message, Long userId, String username, String email) {
        return new AuthResponse(message, userId, username, email, true);
    }
    
    public static AuthResponse error(String message) {
        return new AuthResponse(message, null, null, null, false);
    }
    
    // Getters and Setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
