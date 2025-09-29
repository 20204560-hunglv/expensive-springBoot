package com.hung.expensive.repository;

import com.hung.expensive.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Why: Repository interface cho Category entity operations
 * Design decision: Extend JpaRepository để có sẵn CRUD operations + custom queries
 * Performance: Sử dụng @Query để optimize specific queries cho business needs
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * Why: Find categories by user - core business function
     * Performance: Index trên user_id để fast lookup
     */
    List<Category> findByUserIdOrderByNameAsc(Long userId);
    
    /**
     * Why: Find default categories - system predefined categories
     * Business rule: Default categories có thể được clone cho new users
     */
    List<Category> findByIsDefaultTrueOrderByNameAsc();
    
    /**
     * Why: Check if category name exists for user - prevent duplicates
     * Business rule: User không thể có 2 categories cùng tên
     */
    boolean existsByUserIdAndNameIgnoreCase(Long userId, String name);
    
    /**
     * Why: Find category by user and name - for validation và lookup
     */
    Optional<Category> findByUserIdAndNameIgnoreCase(Long userId, String name);
    
    /**
     * Why: Custom query để count total expenses per category
     * Performance: Efficient aggregation query thay vì load all expenses
     */
    @Query("SELECT c.id, c.name, COUNT(e) as expenseCount, COALESCE(SUM(e.amount), 0) as totalAmount " +
           "FROM Category c LEFT JOIN c.expenses e " +
           "WHERE c.user.id = :userId " +
           "GROUP BY c.id, c.name " +
           "ORDER BY totalAmount DESC")
    List<Object[]> findCategoryStatsWithExpenses(@Param("userId") Long userId);
    
    /**
     * Why: Find categories với expense count trong date range
     * Business requirement: Dashboard analytics cho user
     */
    @Query("SELECT c FROM Category c " +
           "WHERE c.user.id = :userId " +
           "AND EXISTS (SELECT 1 FROM Expense e WHERE e.category = c " +
           "            AND e.expenseDate >= :startDate AND e.expenseDate <= :endDate)")
    List<Category> findCategoriesWithExpensesInDateRange(
        @Param("userId") Long userId, 
        @Param("startDate") java.time.LocalDate startDate, 
        @Param("endDate") java.time.LocalDate endDate
    );
    
    /**
     * Why: Soft delete alternative - disable category instead of delete
     * Business rule: Categories with expenses should not be deleted, only disabled
     */
    @Query("SELECT c FROM Category c WHERE c.user.id = :userId AND " +
           "NOT EXISTS (SELECT 1 FROM Expense e WHERE e.category = c)")
    List<Category> findEmptyCategoriesByUser(@Param("userId") Long userId);
}
