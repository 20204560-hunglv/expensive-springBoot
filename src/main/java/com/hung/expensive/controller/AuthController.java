package com.hung.expensive.controller;

import com.hung.expensive.dto.ApiResponse;
import com.hung.expensive.dto.AuthResponse;
import com.hung.expensive.dto.LoginRequest;
import com.hung.expensive.dto.RegisterRequest;
import com.hung.expensive.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Why: REST Controller cho authentication endpoints
 * Design decision: Tách biệt auth endpoints khỏi business logic
 * Context: Handle HTTP requests cho login và register
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Why: Allow CORS cho frontend development
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * Why: POST endpoint cho user registration
     * Context: Accept RegisterRequest và return AuthResponse
     * Validation: @Valid annotation để validate request body
     * 
     * @param request RegisterRequest từ client
     * @return ResponseEntity với ApiResponse<AuthResponse>
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            // Why: Call service để register user
            AuthResponse authResponse = authService.register(request);
            
            // Why: Return success response với data
            return ResponseEntity.ok(
                ApiResponse.success("Đăng ký thành công", authResponse)
            );
            
        } catch (RuntimeException e) {
            // Why: Handle business logic errors
            return ResponseEntity.badRequest().body(
                ApiResponse.error(e.getMessage())
            );
        } catch (Exception e) {
            // Why: Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error("Có lỗi xảy ra khi đăng ký")
            );
        }
    }
    
    /**
     * Why: POST endpoint cho user login
     * Context: Accept LoginRequest và return AuthResponse
     * Validation: @Valid annotation để validate request body
     * 
     * @param request LoginRequest từ client
     * @return ResponseEntity với ApiResponse<AuthResponse>
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Why: Call service để login user
            AuthResponse authResponse = authService.login(request);
            
            // Why: Return success response với data
            return ResponseEntity.ok(
                ApiResponse.success("Đăng nhập thành công", authResponse)
            );
            
        } catch (RuntimeException e) {
            // Why: Handle authentication errors
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.error("Tên đăng nhập hoặc mật khẩu không đúng")
            );
        } catch (Exception e) {
            // Why: Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error("Có lỗi xảy ra khi đăng nhập")
            );
        }
    }
    
    /**
     * Why: GET endpoint để test authentication
     * Context: Simple endpoint để test JWT token
     * Security: Sẽ được protect bởi Spring Security
     * 
     * @return ResponseEntity với test message
     */
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test() {
        return ResponseEntity.ok(
            ApiResponse.success("API hoạt động bình thường", "Test successful")
        );
    }
}

