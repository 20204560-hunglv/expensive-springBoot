package com.hung.expensive.dto;

/**
 * Why: DTO cho Authentication response - chứa JWT token và user info
 * Design decision: Tách biệt response structure với Entity
 * Security: Không expose password, chỉ trả về thông tin cần thiết
 */
public class AuthResponse {
    
    private String token;
    private String type = "Bearer";
    private Long id;
    private Long userId;
    private String username;
    private String email;
    
    // Why: Default constructor cần thiết cho JSON serialization
    public AuthResponse() {}
    
    public AuthResponse(String token, Long id, String username, String email) {
        this.token = token;
        this.id = id;
        this.userId = id;
        this.username = username;
        this.email = email;
    }
    
    // Why: Builder pattern cho flexible object creation
    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }
    
    public static class AuthResponseBuilder {
        private String token;
        private String type = "Bearer";
        private Long id;
        private Long userId;
        private String username;
        private String email;
        
        public AuthResponseBuilder token(String token) {
            this.token = token;
            return this;
        }
        
        public AuthResponseBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        public AuthResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }
        
        public AuthResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }
        
        public AuthResponseBuilder username(String username) {
            this.username = username;
            return this;
        }
        
        public AuthResponseBuilder email(String email) {
            this.email = email;
            return this;
        }
        
        public AuthResponse build() {
            AuthResponse response = new AuthResponse();
            response.setToken(this.token);
            response.setType(this.type);
            response.setId(this.id);
            response.setUserId(this.userId);
            response.setUsername(this.username);
            response.setEmail(this.email);
            return response;
        }
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

