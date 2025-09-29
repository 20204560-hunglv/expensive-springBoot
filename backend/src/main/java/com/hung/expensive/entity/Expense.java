package com.hung.expensive.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Why: Core entity đại diện cho một khoản chi tiêu
 * Design decision: Sử dụng BigDecimal cho amount để tránh floating point errors
 * Business rule: Mỗi expense phải có amount > 0, thuộc về một user và một category
 */
@Entity
@Table(name = "expenses")
@NamedQueries({
    @NamedQuery(
        name = "Expense.findByUserAndDateRange",
        query = "SELECT e FROM Expense e WHERE e.user.id = :userId AND e.expenseDate BETWEEN :startDate AND :endDate ORDER BY e.expenseDate DESC"
    ),
    @NamedQuery(
        name = "Expense.findTotalByUserAndMonth",
        query = "SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId AND YEAR(e.expenseDate) = :year AND MONTH(e.expenseDate) = :month"
    ),
    @NamedQuery(
        name = "Expense.findByUserAndCategory",
        query = "SELECT e FROM Expense e WHERE e.user.id = :userId AND e.category.id = :categoryId ORDER BY e.expenseDate DESC"
    )
})
public class Expense {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Số tiền không được để trống")
    @DecimalMin(value = "0.01", message = "Số tiền phải lớn hơn 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @NotBlank(message = "Mô tả không được để trống")
    @Size(min = 1, max = 255, message = "Mô tả phải từ 1-255 ký tự")
    @Column(nullable = false)
    private String description;
    
    @Size(max = 1000, message = "Ghi chú không quá 1000 ký tự")
    private String notes;
    
    @NotNull(message = "Ngày chi tiêu không được để trống")
    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;
    
    @Column(name = "location")
    private String location; // Why: Ghi lại địa điểm chi tiêu cho tracking
    
    @Column(name = "receipt_url")
    private String receiptUrl; // Why: Link đến hình ảnh hóa đơn nếu có
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Why: Many-to-One relationship - expense thuộc về user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // Why: Many-to-One relationship - expense thuộc về category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    // Constructors
    public Expense() {}
    
    public Expense(BigDecimal amount, String description, LocalDate expenseDate, User user, Category category) {
        this.amount = amount;
        this.description = description;
        this.expenseDate = expenseDate;
        this.user = user;
        this.category = category;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDate getExpenseDate() {
        return expenseDate;
    }
    
    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getReceiptUrl() {
        return receiptUrl;
    }
    
    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
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
