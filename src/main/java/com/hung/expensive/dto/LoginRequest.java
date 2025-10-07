package com.hung.expensive.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Why: DTO cho Login request - đơn giản chỉ cần username và password
 * Design decision: Dùng username thay vì email để login (có thể thay đổi)
 * Security: Password sẽ được hash trước khi so sánh
 */
public class LoginRequest {
    
    @NotBlank(message = "Username không được để trống")
    private String username;
    
    @NotBlank(message = "Password không được để trống")
    private String password;
    
    // Why: Default constructor cần thiết cho JSON deserialization
    public LoginRequest() {}
    
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}

