package com.hung.expensive.expense.client;

import com.hung.expensive.user.dto.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Why: Feign Client để gọi User Service
 * Design decision: Declarative REST client để simplify service-to-service communication
 * Business requirement: Cần validate user existence khi tạo expense
 */
@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface UserServiceClient {

    /**
     * Why: Get user profile by ID
     * Business rule: Validate user exists trước khi tạo expense
     */
    @GetMapping("/api/users/{userId}")
    UserProfileResponse getUserProfile(@PathVariable("userId") Long userId);

    /**
     * Why: Get user by username
     * Business rule: Alternative way to get user info
     */
    @GetMapping("/api/users/username/{username}")
    UserProfileResponse getUserByUsername(@PathVariable("username") String username);
}
