package com.hung.expensive.category;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Why: Category Service quản lý danh mục chi tiêu
 * Design decision: Tách riêng category management để dễ maintain và scale
 * Business requirement: Cần service riêng cho category CRUD và analytics
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CategoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CategoryServiceApplication.class, args);
    }
}
