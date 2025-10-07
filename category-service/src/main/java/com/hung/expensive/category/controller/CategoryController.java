package com.hung.expensive.category.controller;

import com.hung.expensive.dto.ApiResponse;
import com.hung.expensive.dto.CategoryRequest;
import com.hung.expensive.dto.CategoryResponse;
import com.hung.expensive.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Why: Category Controller xử lý category management endpoints
 * Design decision: RESTful API design với proper HTTP methods
 * Business requirement: Cần CRUD categories, category analytics
 */
@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Why: Get all categories
     * Business rule: Return all available categories
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        try {
            List<CategoryResponse> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(ApiResponse.success(categories, "Categories retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve categories: " + e.getMessage()));
        }
    }

    /**
     * Why: Get category by ID
     * Business rule: Return specific category details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        try {
            CategoryResponse category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(ApiResponse.success(category, "Category retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Category not found: " + e.getMessage()));
        }
    }

    /**
     * Why: Get category by name
     * Business rule: Find category by name for internal service calls
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryByName(@PathVariable String name) {
        try {
            CategoryResponse category = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(ApiResponse.success(category, "Category found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Category not found: " + e.getMessage()));
        }
    }

    /**
     * Why: Create new category
     * Business rule: Validate input, check duplicate name
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest request) {
        try {
            CategoryResponse category = categoryService.createCategory(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(category, "Category created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create category: " + e.getMessage()));
        }
    }

    /**
     * Why: Update existing category
     * Business rule: Validate input, check ownership
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        try {
            CategoryResponse category = categoryService.updateCategory(id, request);
            return ResponseEntity.ok(ApiResponse.success(category, "Category updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update category: " + e.getMessage()));
        }
    }

    /**
     * Why: Delete category
     * Business rule: Check if category is used, soft delete
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", "Delete successful"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete category: " + e.getMessage()));
        }
    }

    /**
     * Why: Get default categories
     * Business rule: Return predefined categories for new users
     */
    @GetMapping("/defaults")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getDefaultCategories() {
        try {
            List<CategoryResponse> categories = categoryService.getDefaultCategories();
            return ResponseEntity.ok(ApiResponse.success(categories, "Default categories retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve default categories: " + e.getMessage()));
        }
    }

    /**
     * Why: Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Category Service is running", "Service is healthy"));
    }
}
