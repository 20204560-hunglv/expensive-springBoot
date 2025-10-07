package com.hung.expensive.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Why: Main application class cho Auth Service
 * Design decision: Enable Discovery client để register với service discovery
 * Context: Microservice chịu trách nhiệm authentication và user management
 * Migration: Tách từ monolithic app, giữ nguyên business logic
 */
@SpringBootApplication
@EnableDiscoveryClient // Why: Register service với service discovery
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
