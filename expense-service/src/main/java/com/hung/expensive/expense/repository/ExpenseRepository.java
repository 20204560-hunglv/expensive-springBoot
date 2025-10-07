package com.hung.expensive.expense.repository;

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
 * Why: Expense Repository cho database operations
 * Design decision: JPA Repository pattern với custom queries
 * Business requirement: Cần complex queries cho filtering, reporting
 */
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    /**
     * Why: Find expenses by user ID
     * Business rule: Get all expenses for specific user
     */
    List<Expense> findByUserId(Long userId);

    /**
     * Why: Find expenses by user ID với pagination
     * Business rule: Support pagination cho large datasets
     */
    Page<Expense> findByUserId(Long userId, Pageable pageable);

    /**
     * Why: Find expenses by category ID
     * Business rule: Get all expenses for specific category
     */
    List<Expense> findByCategoryId(Long categoryId);

    /**
     * Why: Find expenses by category ID với pagination
     * Business rule: Support pagination cho large datasets
     */
    Page<Expense> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * Why: Find expenses by user và date range
     * Business rule: Support date filtering cho reporting
     */
    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId AND e.expenseDate BETWEEN :startDate AND :endDate ORDER BY e.expenseDate DESC")
    Page<Expense> findByUserAndDateRange(@Param("userId") Long userId, 
                                        @Param("startDate") LocalDate startDate, 
                                        @Param("endDate") LocalDate endDate, 
                                        Pageable pageable);

    /**
     * Why: Calculate total expenses by user
     * Business rule: Sum all expenses for specific user
     */
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId")
    BigDecimal getTotalExpensesByUser(@Param("userId") Long userId);

    /**
     * Why: Calculate total expenses by user và month
     * Business rule: Monthly reporting
     */
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId AND YEAR(e.expenseDate) = :year AND MONTH(e.expenseDate) = :month")
    BigDecimal getTotalExpensesByUserAndMonth(@Param("userId") Long userId, 
                                             @Param("year") int year, 
                                             @Param("month") int month);

    /**
     * Why: Find expenses by user và category
     * Business rule: Category-based reporting
     */
    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId AND e.category.id = :categoryId ORDER BY e.expenseDate DESC")
    List<Expense> findByUserAndCategory(@Param("userId") Long userId, 
                                       @Param("categoryId") Long categoryId);

    /**
     * Why: Find top expenses by user
     * Business rule: Get highest expenses for analysis
     */
    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId ORDER BY e.amount DESC")
    List<Expense> findTopExpensesByUser(@Param("userId") Long userId, Pageable pageable);

    /**
     * Why: Count expenses by user
     * Business rule: Statistics và reporting
     */
    @Query("SELECT COUNT(e) FROM Expense e WHERE e.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);
}
