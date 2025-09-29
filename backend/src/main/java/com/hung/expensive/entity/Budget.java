package com.hung.expensive.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

/**
 * Why: Entity đại diện cho budget hàng tháng của user
 * Design decision: Track budget theo tháng để user có thể monitor spending
 * Business rule: User có thể set budget cho từng category hoặc total budget
 */
@Entity
@Table(name = "budgets", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "category_id", "budget_month", "budget_year"})
})
@NamedQueries({
    @NamedQuery(
        name = "Budget.findByUserAndMonth",
        query = "SELECT b FROM Budget b WHERE b.user.id = :userId AND b.budgetMonth = :month AND b.budgetYear = :year"
    ),
    @NamedQuery(
        name = "Budget.findByUserCategoryAndMonth",
        query = "SELECT b FROM Budget b WHERE b.user.id = :userId AND b.category.id = :categoryId AND b.budgetMonth = :month AND b.budgetYear = :year"
    )
})
public class Budget {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Số tiền budget không được để trống")
    @DecimalMin(value = "0.01", message = "Budget phải lớn hơn 0")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;
    
    @NotNull(message = "Tháng budget không được để trống")
    @Column(name = "budget_month", nullable = false)
    private Integer budgetMonth; // 1-12
    
    @NotNull(message = "Năm budget không được để trống")
    @Column(name = "budget_year", nullable = false)
    private Integer budgetYear;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Why: Many-to-One relationship - budget thuộc về user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // Why: Many-to-One relationship - budget có thể cho specific category (nullable cho total budget)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    // Constructors
    public Budget() {}
    
    public Budget(BigDecimal amount, YearMonth yearMonth, User user) {
        this.amount = amount;
        this.budgetMonth = yearMonth.getMonthValue();
        this.budgetYear = yearMonth.getYear();
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Budget(BigDecimal amount, YearMonth yearMonth, User user, Category category) {
        this(amount, yearMonth, user);
        this.category = category;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Helper methods
    public YearMonth getYearMonth() {
        return YearMonth.of(budgetYear, budgetMonth);
    }
    
    public void setYearMonth(YearMonth yearMonth) {
        this.budgetYear = yearMonth.getYear();
        this.budgetMonth = yearMonth.getMonthValue();
    }
    
    // Why: Check if this is a total budget (không có category cụ thể)
    public boolean isTotalBudget() {
        return category == null;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public Integer getBudgetMonth() {
        return budgetMonth;
    }
    
    public void setBudgetMonth(Integer budgetMonth) {
        this.budgetMonth = budgetMonth;
    }
    
    public Integer getBudgetYear() {
        return budgetYear;
    }
    
    public void setBudgetYear(Integer budgetYear) {
        this.budgetYear = budgetYear;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
}
