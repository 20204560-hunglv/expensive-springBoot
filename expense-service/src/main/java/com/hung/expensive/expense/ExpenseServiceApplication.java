package com.hung.expensive.expense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Why: Expense Service quản lý tất cả operations liên quan đến chi tiêu
 * Design decision: Core business logic service với complex calculations
 * Business requirement: Cần service riêng cho expense tracking, reporting, analytics
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ExpenseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseServiceApplication.class, args);
    }
}
