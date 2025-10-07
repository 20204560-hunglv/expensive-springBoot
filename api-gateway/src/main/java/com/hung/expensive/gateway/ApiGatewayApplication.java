package com.hung.expensive.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Why: API Gateway làm entry point cho tất cả client requests
 * Design decision: Centralized routing, authentication, và load balancing
 * Business requirement: Cần single point of entry cho security và monitoring
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
