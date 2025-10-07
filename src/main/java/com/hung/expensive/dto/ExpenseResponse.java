package com.hung.expensive.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Why: DTO cho expense response - control data exposure
 * Design decision: Include category info để frontend không cần additional call
 * Performance: Optimized data structure cho API responses
 */
public class ExpenseResponse {
    
    private Long id;
    private BigDecimal amount;
    private String description;
    private String notes;
    private LocalDate expenseDate;
    private String location;
    private String receiptUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Category information - Why: Embedded để reduce API calls
    private Long categoryId;
    private String categoryName;
    private String categoryColorCode;
    private String categoryIconName;
    
    // Constructors
    public ExpenseResponse() {}
    
    public ExpenseResponse(Long id, BigDecimal amount, String description, LocalDate expenseDate,
                          Long categoryId, String categoryName) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.expenseDate = expenseDate;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
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
    
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getCategoryColorCode() {
        return categoryColorCode;
    }
    
    public void setCategoryColorCode(String categoryColorCode) {
        this.categoryColorCode = categoryColorCode;
    }
    
    public String getCategoryIconName() {
        return categoryIconName;
    }
    
    public void setCategoryIconName(String categoryIconName) {
        this.categoryIconName = categoryIconName;
    }
}
