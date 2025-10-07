package com.hung.expensive.repository;

import com.hung.expensive.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Why: Repository cho Budget entity - budget management functionality
 * Design decision: Custom queries cho budget tracking và comparison
 * Business requirement: Track budget vs actual spending
 */
@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    
    /**
     * Why: Find all budgets for user trong specific month
     * Business requirement: Monthly budget overview
     */
    List<Budget> findByUserIdAndBudgetYearAndBudgetMonthOrderByCategoryNameAsc(
        Long userId, Integer year, Integer month
    );
    
    /**
     * Why: Find total budget (không có category) cho user trong month
     * Business rule: User có thể set overall monthly budget
     */
    Optional<Budget> findByUserIdAndBudgetYearAndBudgetMonthAndCategoryIsNull(
        Long userId, Integer year, Integer month
    );
    
    /**
     * Why: Find category budget cho specific month
     * Business requirement: Category-specific budget tracking
     */
    Optional<Budget> findByUserIdAndCategoryIdAndBudgetYearAndBudgetMonth(
        Long userId, Long categoryId, Integer year, Integer month
    );
    
    /**
     * Why: Check if budget exists - prevent duplicate budget creation
     */
    boolean existsByUserIdAndCategoryIdAndBudgetYearAndBudgetMonth(
        Long userId, Long categoryId, Integer year, Integer month
    );
    
    /**
     * Why: Check if total budget exists cho month
     */
    boolean existsByUserIdAndBudgetYearAndBudgetMonthAndCategoryIsNull(
        Long userId, Integer year, Integer month
    );
    
    /**
     * Why: Calculate total budgeted amount cho user trong month
     * Performance: SUM query thay vì load all budgets
     */
    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM Budget b " +
           "WHERE b.user.id = :userId " +
           "AND b.budgetYear = :year AND b.budgetMonth = :month")
    BigDecimal calculateTotalBudgetByMonth(@Param("userId") Long userId,
                                          @Param("year") Integer year,
                                          @Param("month") Integer month);
    
    /**
     * Why: Budget vs Actual comparison data cho dashboard
     * Business requirement: Show budget performance analytics
     */
    @Query("SELECT b.category.name, b.amount as budgetAmount, " +
           "COALESCE(SUM(e.amount), 0) as actualAmount, " +
           "b.category.colorCode " +
           "FROM Budget b " +
           "LEFT JOIN Expense e ON e.category = b.category " +
           "AND e.user.id = :userId " +
           "AND YEAR(e.expenseDate) = :year AND MONTH(e.expenseDate) = :month " +
           "WHERE b.user.id = :userId " +
           "AND b.budgetYear = :year AND b.budgetMonth = :month " +
           "AND b.category IS NOT NULL " +
           "GROUP BY b.id, b.category.name, b.amount, b.category.colorCode " +
           "ORDER BY b.category.name")
    List<Object[]> findBudgetVsActualByMonth(@Param("userId") Long userId,
                                            @Param("year") Integer year,
                                            @Param("month") Integer month);
    
    /**
     * Why: Find budgets where actual spending exceeds budget - alert system
     * Business requirement: Budget overrun notifications
     */
    @Query("SELECT b FROM Budget b " +
           "WHERE b.user.id = :userId " +
           "AND b.budgetYear = :year AND b.budgetMonth = :month " +
           "AND b.amount < (" +
           "    SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
           "    WHERE e.user.id = :userId " +
           "    AND e.category = b.category " +
           "    AND YEAR(e.expenseDate) = :year AND MONTH(e.expenseDate) = :month" +
           ")")
    List<Budget> findOverBudgetCategories(@Param("userId") Long userId,
                                         @Param("year") Integer year,
                                         @Param("month") Integer month);
    
    /**
     * Why: Find budgets for multiple months - trend analysis
     */
    @Query("SELECT b FROM Budget b " +
           "WHERE b.user.id = :userId " +
           "AND (b.budgetYear > :startYear " +
           "     OR (b.budgetYear = :startYear AND b.budgetMonth >= :startMonth)) " +
           "AND (b.budgetYear < :endYear " +
           "     OR (b.budgetYear = :endYear AND b.budgetMonth <= :endMonth)) " +
           "ORDER BY b.budgetYear, b.budgetMonth, b.category.name")
    List<Budget> findBudgetsInDateRange(@Param("userId") Long userId,
                                       @Param("startYear") Integer startYear,
                                       @Param("startMonth") Integer startMonth,
                                       @Param("endYear") Integer endYear,
                                       @Param("endMonth") Integer endMonth);
    
    /**
     * Why: Get all user budgets for year - annual planning
     */
    List<Budget> findByUserIdAndBudgetYearOrderByBudgetMonthAscCategoryNameAsc(
        Long userId, Integer year
    );
}
