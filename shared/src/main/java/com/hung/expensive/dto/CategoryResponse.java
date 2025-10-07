package com.hung.expensive.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Why: DTO cho category response với optional expense statistics
 * Design decision: Include expense count và total amount cho analytics
 * Performance: Pre-calculated statistics để avoid N+1 queries
 */
public class CategoryResponse {
    
    private Long id;
    private String name;
    private String description;
    private String colorCode;
    private String iconName;
    private Boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Optional statistics - Why: Useful cho dashboard display
    private Long expenseCount;
    private BigDecimal totalAmount;
    
    // Constructors
    public CategoryResponse() {}
    
    public CategoryResponse(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    public CategoryResponse(Long id, String name, String description, String colorCode, String iconName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.colorCode = colorCode;
        this.iconName = iconName;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getColorCode() {
        return colorCode;
    }
    
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
    
    public String getIconName() {
        return iconName;
    }
    
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
    
    public Boolean getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
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
    
    public Long getExpenseCount() {
        return expenseCount;
    }
    
    public void setExpenseCount(Long expenseCount) {
        this.expenseCount = expenseCount;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
