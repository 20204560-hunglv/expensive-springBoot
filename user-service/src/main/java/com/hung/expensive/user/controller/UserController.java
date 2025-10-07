package com.hung.expensive.user.controller;

import com.hung.expensive.dto.ApiResponse;
import com.hung.expensive.user.dto.UserProfileRequest;
import com.hung.expensive.user.dto.UserProfileResponse;
import com.hung.expensive.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Why: User Controller xử lý user profile management
 * Design decision: Tách riêng user management khỏi authentication
 * Business requirement: Cần CRUD user profile, preferences, settings
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Why: Get user profile by ID
     * Business rule: Return user profile information
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(@PathVariable Long userId) {
        try {
            UserProfileResponse response = userService.getUserProfile(userId);
            return ResponseEntity.ok(ApiResponse.success(response, "User profile retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found: " + e.getMessage()));
        }
    }

    /**
     * Why: Update user profile
     * Business rule: Update user information, validate input
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserProfileRequest request) {
        try {
            UserProfileResponse response = userService.updateUserProfile(userId, request);
            return ResponseEntity.ok(ApiResponse.success(response, "User profile updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Update failed: " + e.getMessage()));
        }
    }

    /**
     * Why: Get user by username
     * Business rule: Find user by username for internal service calls
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserByUsername(@PathVariable String username) {
        try {
            UserProfileResponse response = userService.getUserByUsername(username);
            return ResponseEntity.ok(ApiResponse.success(response, "User found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found: " + e.getMessage()));
        }
    }

    /**
     * Why: Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("User Service is running", "Service is healthy"));
    }
}
