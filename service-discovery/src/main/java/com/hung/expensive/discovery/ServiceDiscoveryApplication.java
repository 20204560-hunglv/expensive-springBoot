package com.hung.expensive.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Why: Eureka Server cho service discovery trong microservices architecture
 * Design decision: Centralized service registry để các service có thể tìm thấy nhau
 * Business requirement: Cần service discovery để support dynamic service registration
 */
@SpringBootApplication
@EnableEurekaServer
public class ServiceDiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceDiscoveryApplication.class, args);
    }
}
