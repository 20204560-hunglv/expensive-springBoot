package com.hung.expensive.service;

import com.hung.expensive.dto.CategoryRequest;
import com.hung.expensive.dto.CategoryResponse;
import com.hung.expensive.entity.Category;
import com.hung.expensive.entity.User;
import com.hung.expensive.repository.CategoryRepository;
import com.hung.expensive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Why: Service layer cho category management
 * Business logic: Default categories creation, user category management
 * Security: Ensure user can only access own categories
 */
@Service
@Transactional
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    
    // Why: Predefined default categories cho new users
    private static final List<DefaultCategoryData> DEFAULT_CATEGORIES = Arrays.asList(
        new DefaultCategoryData("Ăn uống", "Chi tiêu cho thức ăn và đồ uống", "#FF6B6B", "food"),
        new DefaultCategoryData("Di chuyển", "Xăng xe, xe bus, taxi, grab", "#4ECDC4", "transport"),
        new DefaultCategoryData("Giải trí", "Phim ảnh, game, du lịch", "#45B7D1", "entertainment"),
        new DefaultCategoryData("Mua sắm", "Quần áo, đồ dùng cá nhân", "#96CEB4", "shopping"),
        new DefaultCategoryData("Y tế", "Khám bệnh, thuốc men", "#FFEAA7", "healthcare"),
        new DefaultCategoryData("Giáo dục", "Học phí, sách vở, khóa học", "#DDA0DD", "education"),
        new DefaultCategoryData("Hóa đơn", "Điện, nước, internet, điện thoại", "#FFB6C1", "bills"),
        new DefaultCategoryData("Khác", "Các chi tiêu khác", "#D3D3D3", "other")
    );
    
    @Autowired
    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }
    
    /**
     * Why: Create default categories cho new user
     * Business rule: Mỗi user mới được tạo sẵn basic categories
     */
    public void createDefaultCategoriesForUser(User user) {
        for (DefaultCategoryData defaultData : DEFAULT_CATEGORIES) {
            Category category = new Category(defaultData.name, defaultData.description, user);
            category.setColorCode(defaultData.colorCode);
            category.setIconName(defaultData.iconName);
            category.setIsDefault(false); // Why: User-specific copy of default
            
            categoryRepository.save(category);
        }
    }
    
    /**
     * Why: Get all categories for user
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getUserCategories(String username) {
        User user = findUserByUsername(username);
        List<Category> categories = categoryRepository.findByUserIdOrderByNameAsc(user.getId());
        
        return categories.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Why: Get categories với expense statistics
     * Performance: Single query để get stats instead of N+1 queries
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getUserCategoriesWithStats(String username) {
        User user = findUserByUsername(username);
        List<Object[]> categoryStats = categoryRepository.findCategoryStatsWithExpenses(user.getId());
        
        return categoryStats.stream()
            .map(this::mapToResponseWithStats)
            .collect(Collectors.toList());
    }
    
    /**
     * Why: Create new category với duplicate name validation
     */
    public CategoryResponse createCategory(CategoryRequest request, String username) {
        User user = findUserByUsername(username);
        
        // Why: Check duplicate name
        if (categoryRepository.existsByUserIdAndNameIgnoreCase(user.getId(), request.getName())) {
            throw new RuntimeException("Category với tên này đã tồn tại");
        }
        
        Category category = new Category(request.getName(), request.getDescription(), user);
        category.setColorCode(request.getColorCode());
        category.setIconName(request.getIconName());
        category.setIsDefault(false);
        
        category = categoryRepository.save(category);
        return mapToResponse(category);
    }
    
    /**
     * Why: Update existing category với validation
     */
    public CategoryResponse updateCategory(Long id, CategoryRequest request, String username) {
        User user = findUserByUsername(username);
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category không tồn tại"));
        
        // Why: Security check
        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền chỉnh sửa category này");
        }
        
        // Why: Check duplicate name (exclude current category)
        if (!category.getName().equalsIgnoreCase(request.getName()) &&
            categoryRepository.existsByUserIdAndNameIgnoreCase(user.getId(), request.getName())) {
            throw new RuntimeException("Category với tên này đã tồn tại");
        }
        
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setColorCode(request.getColorCode());
        category.setIconName(request.getIconName());
        
        category = categoryRepository.save(category);
        return mapToResponse(category);
    }
    
    /**
     * Why: Get single category by ID với ownership validation
     */
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id, String username) {
        User user = findUserByUsername(username);
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category không tồn tại"));
        
        // Why: Security check
        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền truy cập category này");
        }
        
        return mapToResponse(category);
    }
    
    /**
     * Why: Delete category với business rule validation
     * Business rule: Cannot delete category có expenses
     */
    public void deleteCategory(Long id, String username) {
        User user = findUserByUsername(username);
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category không tồn tại"));
        
        // Why: Security check
        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền xóa category này");
        }
        
        // Why: Business rule - cannot delete category with expenses
        if (category.getExpenses() != null && !category.getExpenses().isEmpty()) {
            throw new RuntimeException("Không thể xóa category đang có expenses. " +
                "Vui lòng chuyển expenses sang category khác trước.");
        }
        
        categoryRepository.delete(category);
    }
    
    // Helper Methods
    
    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User không tồn tại"));
    }
    
    private CategoryResponse mapToResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setColorCode(category.getColorCode());
        response.setIconName(category.getIconName());
        response.setIsDefault(category.getIsDefault());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        
        return response;
    }
    
    private CategoryResponse mapToResponseWithStats(Object[] stats) {
        CategoryResponse response = new CategoryResponse();
        response.setId(((Number) stats[0]).longValue());
        response.setName((String) stats[1]);
        response.setExpenseCount(((Number) stats[2]).longValue());
        response.setTotalAmount((BigDecimal) stats[3]);
        
        return response;
    }
    
    // Why: Helper class cho default category data
    private static class DefaultCategoryData {
        final String name;
        final String description;
        final String colorCode;
        final String iconName;
        
        DefaultCategoryData(String name, String description, String colorCode, String iconName) {
            this.name = name;
            this.description = description;
            this.colorCode = colorCode;
            this.iconName = iconName;
        }
    }
}
