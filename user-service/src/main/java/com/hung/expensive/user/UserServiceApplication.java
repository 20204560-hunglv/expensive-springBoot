package com.hung.expensive.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Why: User Service quản lý thông tin user và profile
 * Design decision: Tách riêng user management khỏi authentication
 * Business requirement: Cần service riêng cho user profile, preferences, settings
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
