package com.hung.expensive.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Why: DTO cho expense creation và update requests
 * Design decision: Separate DTO để avoid exposing entity structure
 * Security: Validation annotations để ensure data integrity
 */
public class ExpenseRequest {
    
    @NotNull(message = "Số tiền không được để trống")
    @DecimalMin(value = "0.01", message = "Số tiền phải lớn hơn 0")
    private BigDecimal amount;
    
    @NotBlank(message = "Mô tả không được để trống")
    @Size(min = 1, max = 255, message = "Mô tả phải từ 1-255 ký tự")
    private String description;
    
    @Size(max = 1000, message = "Ghi chú không quá 1000 ký tự")
    private String notes;
    
    @NotNull(message = "Ngày chi tiêu không được để trống")
    private LocalDate expenseDate;
    
    @Size(max = 255, message = "Địa điểm không quá 255 ký tự")
    private String location;
    
    @NotNull(message = "Category ID không được để trống")
    private Long categoryId;
    
    @Size(max = 500, message = "Receipt URL không quá 500 ký tự")
    private String receiptUrl;
    
    // Constructors
    public ExpenseRequest() {}
    
    public ExpenseRequest(BigDecimal amount, String description, LocalDate expenseDate, Long categoryId) {
        this.amount = amount;
        this.description = description;
        this.expenseDate = expenseDate;
        this.categoryId = categoryId;
    }
    
    // Getters and Setters
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
    
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getReceiptUrl() {
        return receiptUrl;
    }
    
    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }
}
