package com.hung.expensive.expense.client;

import com.hung.expensive.dto.CategoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Why: Feign Client để gọi Category Service
 * Design decision: Declarative REST client để simplify service-to-service communication
 * Business requirement: Cần validate category existence khi tạo expense
 */
@FeignClient(name = "category-service", url = "http://localhost:8084")
public interface CategoryServiceClient {

    /**
     * Why: Get category by ID
     * Business rule: Validate category exists trước khi tạo expense
     */
    @GetMapping("/api/categories/{id}")
    CategoryResponse getCategoryById(@PathVariable("id") Long id);

    /**
     * Why: Get category by name
     * Business rule: Alternative way to get category info
     */
    @GetMapping("/api/categories/name/{name}")
    CategoryResponse getCategoryByName(@PathVariable("name") String name);
}
