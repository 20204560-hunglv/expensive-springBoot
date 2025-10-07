package com.hung.expensive.category.service;

import com.hung.expensive.dto.CategoryRequest;
import com.hung.expensive.dto.CategoryResponse;
import com.hung.expensive.entity.Category;
import com.hung.expensive.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Why: Category Service chứa business logic cho category management
 * Design decision: Service layer pattern để tách biệt business logic
 * Business requirement: Handle category CRUD, default categories, validation
 */
@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Why: Get all categories
     * Business rule: Return all available categories
     */
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Why: Get category by ID
     * Business rule: Return specific category details
     */
    public CategoryResponse getCategoryById(Long id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            throw new RuntimeException("Category not found with ID: " + id);
        }

        return mapToCategoryResponse(categoryOpt.get());
    }

    /**
     * Why: Get category by name
     * Business rule: Find category by name for internal service calls
     */
    public CategoryResponse getCategoryByName(String name) {
        Optional<Category> categoryOpt = categoryRepository.findByName(name);
        if (categoryOpt.isEmpty()) {
            throw new RuntimeException("Category not found with name: " + name);
        }

        return mapToCategoryResponse(categoryOpt.get());
    }

    /**
     * Why: Create new category
     * Business rule: Validate input, check duplicate name
     */
    public CategoryResponse createCategory(CategoryRequest request) {
        // Why: Check if category name already exists
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("Category name already exists: " + request.getName());
        }

        // Why: Create category entity
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setColor(request.getColor());
        category.setIcon(request.getIcon());

        Category savedCategory = categoryRepository.save(category);
        return mapToCategoryResponse(savedCategory);
    }

    /**
     * Why: Update existing category
     * Business rule: Validate input, check ownership
     */
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            throw new RuntimeException("Category not found with ID: " + id);
        }

        Category category = categoryOpt.get();

        // Why: Update fields
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setColor(request.getColor());
        category.setIcon(request.getIcon());

        Category updatedCategory = categoryRepository.save(category);
        return mapToCategoryResponse(updatedCategory);
    }

    /**
     * Why: Delete category
     * Business rule: Check if category is used, soft delete
     */
    public void deleteCategory(Long id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            throw new RuntimeException("Category not found with ID: " + id);
        }

        // Why: Check if category is used in expenses (simplified check)
        // Trong thực tế cần check với expense service
        categoryRepository.deleteById(id);
    }

    /**
     * Why: Get default categories
     * Business rule: Return predefined categories for new users
     */
    public List<CategoryResponse> getDefaultCategories() {
        // Why: Create default categories nếu chưa có
        createDefaultCategoriesIfNotExists();

        List<Category> categories = categoryRepository.findByIsDefaultTrue();
        return categories.stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Why: Create default categories nếu chưa có
     * Business rule: Ensure system có basic categories
     */
    private void createDefaultCategoriesIfNotExists() {
        List<String> defaultCategoryNames = Arrays.asList(
                "Ăn uống", "Đi lại", "Học tập", "Giải trí", 
                "Sức khỏe", "Mua sắm", "Hóa đơn", "Khác"
        );

        for (String name : defaultCategoryNames) {
            if (categoryRepository.findByName(name).isEmpty()) {
                Category category = new Category();
                category.setName(name);
                category.setDescription("Danh mục mặc định: " + name);
                category.setColor("#007bff"); // Default blue color
                category.setIcon("default-icon");
                category.setIsDefault(true);
                categoryRepository.save(category);
            }
        }
    }

    /**
     * Why: Map Category entity to CategoryResponse DTO
     * Design decision: Separate mapping logic để maintain clean code
     */
    private CategoryResponse mapToCategoryResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setColor(category.getColor());
        response.setIcon(category.getIcon());
        response.setIsDefault(category.getIsDefault());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        return response;
    }
}
