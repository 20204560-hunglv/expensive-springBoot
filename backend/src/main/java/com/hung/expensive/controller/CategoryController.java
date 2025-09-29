package com.hung.expensive.controller;

import com.hung.expensive.dto.ApiResponse;
import com.hung.expensive.dto.CategoryRequest;
import com.hung.expensive.dto.CategoryResponse;
import com.hung.expensive.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Why: REST API controller cho category management
 * Design decision: Simple CRUD operations với proper validation
 * Security: Authentication-based access control
 */
@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:5173") // Why: Allow React frontend access
public class CategoryController {
    
    private final CategoryService categoryService;
    
    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    
    /**
     * Why: Get all categories for authenticated user
     * GET /api/categories
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getUserCategories(
            Authentication authentication) {
        
        try {
            List<CategoryResponse> categories = categoryService.getUserCategories(authentication.getName());
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách categories thành công", categories));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * Why: Get categories with expense statistics
     * GET /api/categories/with-stats
     */
    @GetMapping("/with-stats")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getUserCategoriesWithStats(
            Authentication authentication) {
        
        try {
            List<CategoryResponse> categories = categoryService.getUserCategoriesWithStats(
                authentication.getName());
            
            return ResponseEntity.ok(new ApiResponse<>(true, 
                "Lấy categories với statistics thành công", categories));
                
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * Why: Get single category by ID
     * GET /api/categories/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
            @PathVariable Long id,
            Authentication authentication) {
        
        try {
            CategoryResponse category = categoryService.getCategoryById(id, authentication.getName());
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy category thành công", category));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * Why: Create new category
     * POST /api/categories
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request,
            Authentication authentication) {
        
        try {
            CategoryResponse category = categoryService.createCategory(request, authentication.getName());
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Category đã được tạo thành công", category));
                
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * Why: Update existing category
     * PUT /api/categories/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request,
            Authentication authentication) {
        
        try {
            CategoryResponse category = categoryService.updateCategory(id, request, authentication.getName());
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Category đã được cập nhật thành công", category));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * Why: Delete category
     * DELETE /api/categories/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @PathVariable Long id,
            Authentication authentication) {
        
        try {
            categoryService.deleteCategory(id, authentication.getName());
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Category đã được xóa thành công", null));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
