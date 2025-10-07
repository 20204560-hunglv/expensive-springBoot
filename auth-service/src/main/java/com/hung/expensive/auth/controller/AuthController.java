package com.hung.expensive.auth.controller;

import com.hung.expensive.dto.ApiResponse;
import com.hung.expensive.dto.AuthResponse;
import com.hung.expensive.dto.LoginRequest;
import com.hung.expensive.dto.RegisterRequest;
import com.hung.expensive.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Why: Auth Controller xử lý authentication endpoints
 * Design decision: Tách riêng auth logic để dễ maintain và secure
 * Business requirement: Cần login, register, và token validation
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    // Why: Constructor injection thay vì @Autowired - best practice
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Why: Register endpoint cho user mới
     * Business rule: Validate input, check duplicate, hash password
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User registered successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Registration failed: " + e.getMessage()));
        }
    }

    /**
     * Why: Login endpoint cho existing user
     * Business rule: Validate credentials, generate JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Login failed: " + e.getMessage()));
        }
    }

    /**
     * Why: Validate token endpoint cho API Gateway
     * Business rule: Check token validity, return user info
     */
    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<AuthResponse>> validateToken(@RequestHeader("Authorization") String token) {
        try {
            // Why: Remove "Bearer " prefix
            String jwtToken = token.replace("Bearer ", "");
            AuthResponse response = authService.validateToken(jwtToken);
            return ResponseEntity.ok(ApiResponse.success("Token is valid", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Token validation failed: " + e.getMessage()));
        }
    }

    /**
     * Why: Health check endpoint
     * Business rule: Simple endpoint để check service status
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Auth Service is running", "Service is healthy"));
    }
}