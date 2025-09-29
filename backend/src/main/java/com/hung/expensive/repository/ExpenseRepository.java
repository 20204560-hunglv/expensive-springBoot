package com.hung.expensive.repository;

import com.hung.expensive.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Why: Repository interface cho Expense entity - core business operations
 * Design decision: Nhiều custom queries cho analytics và reporting
 * Performance: Optimized queries với proper indexing strategy
 */
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
    /**
     * Why: Find expenses by user với pagination - core listing function
     * Performance: Pagination để avoid loading too much data
     */
    Page<Expense> findByUserIdOrderByExpenseDateDescCreatedAtDesc(Long userId, Pageable pageable);
    
    /**
     * Why: Find expenses by user trong date range - filtering function
     */
    Page<Expense> findByUserIdAndExpenseDateBetweenOrderByExpenseDateDescCreatedAtDesc(
        Long userId, LocalDate startDate, LocalDate endDate, Pageable pageable
    );
    
    /**
     * Why: Find expenses by category - category analysis
     */
    Page<Expense> findByUserIdAndCategoryIdOrderByExpenseDateDescCreatedAtDesc(
        Long userId, Long categoryId, Pageable pageable
    );
    
    /**
     * Why: Calculate total expenses for user trong tháng cụ thể
     * Business requirement: Monthly spending analysis
     */
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
           "WHERE e.user.id = :userId " +
           "AND YEAR(e.expenseDate) = :year AND MONTH(e.expenseDate) = :month")
    BigDecimal calculateTotalByUserAndMonth(@Param("userId") Long userId, 
                                           @Param("year") int year, 
                                           @Param("month") int month);
    
    /**
     * Why: Calculate total expenses by category trong tháng
     * Performance: Single query thay vì multiple calls
     */
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
           "WHERE e.user.id = :userId AND e.category.id = :categoryId " +
           "AND YEAR(e.expenseDate) = :year AND MONTH(e.expenseDate) = :month")
    BigDecimal calculateTotalByCategoryAndMonth(@Param("userId") Long userId,
                                               @Param("categoryId") Long categoryId,
                                               @Param("year") int year,
                                               @Param("month") int month);
    
    /**
     * Why: Daily expenses summary - dashboard analytics
     * Performance: Group by date để reduce data transfer
     */
    @Query("SELECT e.expenseDate, SUM(e.amount) " +
           "FROM Expense e " +
           "WHERE e.user.id = :userId " +
           "AND e.expenseDate BETWEEN :startDate AND :endDate " +
           "GROUP BY e.expenseDate " +
           "ORDER BY e.expenseDate DESC")
    List<Object[]> findDailyExpenseSummary(@Param("userId") Long userId,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);
    
    /**
     * Why: Monthly category breakdown - pie chart data
     * Business requirement: Show spending distribution by category
     */
    @Query("SELECT c.name, c.colorCode, SUM(e.amount), COUNT(e) " +
           "FROM Expense e JOIN e.category c " +
           "WHERE e.user.id = :userId " +
           "AND YEAR(e.expenseDate) = :year AND MONTH(e.expenseDate) = :month " +
           "GROUP BY c.id, c.name, c.colorCode " +
           "ORDER BY SUM(e.amount) DESC")
    List<Object[]> findMonthlyExpensesByCategory(@Param("userId") Long userId,
                                                @Param("year") int year,
                                                @Param("month") int month);
    
    /**
     * Why: Recent expenses preview - dashboard quick view
     * Performance: LIMIT query để avoid loading too much data
     */
    @Query(value = "SELECT * FROM expenses e " +
                   "WHERE e.user_id = :userId " +
                   "ORDER BY e.expense_date DESC, e.created_at DESC " +
                   "LIMIT :limit", nativeQuery = true)
    List<Expense> findRecentExpenses(@Param("userId") Long userId, @Param("limit") int limit);
    
    /**
     * Why: Search expenses by description - search functionality
     * Performance: LIKE query với index trên description
     */
    Page<Expense> findByUserIdAndDescriptionContainingIgnoreCaseOrderByExpenseDateDescCreatedAtDesc(
        Long userId, String searchTerm, Pageable pageable
    );
    
    /**
     * Why: Find top spending days - analytics feature
     */
    @Query("SELECT e.expenseDate, SUM(e.amount) as dailyTotal " +
           "FROM Expense e " +
           "WHERE e.user.id = :userId " +
           "AND e.expenseDate BETWEEN :startDate AND :endDate " +
           "GROUP BY e.expenseDate " +
           "ORDER BY dailyTotal DESC")
    List<Object[]> findTopSpendingDays(@Param("userId") Long userId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate,
                                      Pageable pageable);
    
    /**
     * Why: Check if user has any expenses - validation for delete user
     */
    boolean existsByUserId(Long userId);
    
    /**
     * Why: Count expenses by category - for statistics
     */
    long countByUserIdAndCategoryId(Long userId, Long categoryId);
}
