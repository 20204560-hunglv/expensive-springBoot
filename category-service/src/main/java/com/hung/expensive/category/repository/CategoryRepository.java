package com.hung.expensive.category.repository;

import com.hung.expensive.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Why: Category Repository cho database operations
 * Design decision: JPA Repository pattern để simplify database access
 * Business requirement: Cần query category by name, default categories, etc.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Why: Find category by name - Spring Data JPA tự động implement
     * Business rule: Category name phải unique trong hệ thống
     */
    Optional<Category> findByName(String name);

    /**
     * Why: Find default categories
     * Business rule: Return predefined categories for new users
     */
    List<Category> findByIsDefaultTrue();

    /**
     * Why: Check if category name exists - custom query cho performance
     * Business rule: Cần check duplicate name khi create/update
     */
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.name = :name")
    boolean existsByName(@Param("name") String name);

    /**
     * Why: Find categories by name containing - search functionality
     * Business rule: Support category search
     */
    @Query("SELECT c FROM Category c WHERE c.name LIKE %:name%")
    List<Category> findByNameContaining(@Param("name") String name);

    /**
     * Why: Find active categories only
     * Business rule: Chỉ return active categories
     */
    @Query("SELECT c FROM Category c WHERE c.isActive = true")
    List<Category> findActiveCategories();

    /**
     * Why: Count total categories
     * Business rule: Statistics và reporting
     */
    @Query("SELECT COUNT(c) FROM Category c")
    Long countTotalCategories();

    /**
     * Why: Find categories created in date range
     * Business rule: Reporting và analytics
     */
    @Query("SELECT c FROM Category c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    List<Category> findCategoriesCreatedBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                               @Param("endDate") java.time.LocalDateTime endDate);
}
