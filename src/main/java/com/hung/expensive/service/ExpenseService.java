package com.hung.expensive.service;

import com.hung.expensive.dto.ExpenseRequest;
import com.hung.expensive.dto.ExpenseResponse;
import com.hung.expensive.entity.Category;
import com.hung.expensive.entity.Expense;
import com.hung.expensive.entity.User;
import com.hung.expensive.repository.CategoryRepository;
import com.hung.expensive.repository.ExpenseRepository;
import com.hung.expensive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Why: Service layer cho expense business logic
 * Design decision: Transactional methods để ensure data consistency
 * Security: Always verify user ownership trước khi modify data
 */
@Service
@Transactional
public class ExpenseService {
    
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, 
                         CategoryRepository categoryRepository,
                         UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }
    
    /**
     * Why: Create new expense với validation
     * Business rule: Verify category thuộc về user hoặc là default category
     */
    public ExpenseResponse createExpense(ExpenseRequest request, String username) {
        User user = findUserByUsername(username);
        Category category = findAndValidateCategory(request.getCategoryId(), user.getId());
        
        Expense expense = new Expense(
            request.getAmount(),
            request.getDescription(),
            request.getExpenseDate(),
            user,
            category
        );
        
        // Why: Set optional fields nếu có
        expense.setNotes(request.getNotes());
        expense.setLocation(request.getLocation());
        expense.setReceiptUrl(request.getReceiptUrl());
        
        expense = expenseRepository.save(expense);
        return mapToResponse(expense);
    }
    
    /**
     * Why: Get paginated expenses cho user
     * Performance: Pagination để avoid loading too much data
     */
    @Transactional(readOnly = true)
    public Page<ExpenseResponse> getUserExpenses(String username, int page, int size) {
        User user = findUserByUsername(username);
        Pageable pageable = PageRequest.of(page, size);
        
        Page<Expense> expenses = expenseRepository
            .findByUserIdOrderByExpenseDateDescCreatedAtDesc(user.getId(), pageable);
        
        return expenses.map(this::mapToResponse);
    }
    
    /**
     * Why: Get expenses trong date range
     */
    @Transactional(readOnly = true)
    public Page<ExpenseResponse> getUserExpensesByDateRange(String username, 
                                                           LocalDate startDate, 
                                                           LocalDate endDate, 
                                                           int page, int size) {
        User user = findUserByUsername(username);
        Pageable pageable = PageRequest.of(page, size);
        
        Page<Expense> expenses = expenseRepository
            .findByUserIdAndExpenseDateBetweenOrderByExpenseDateDescCreatedAtDesc(
                user.getId(), startDate, endDate, pageable);
        
        return expenses.map(this::mapToResponse);
    }
    
    /**
     * Why: Get single expense by ID với ownership validation
     */
    @Transactional(readOnly = true)
    public ExpenseResponse getExpenseById(Long id, String username) {
        User user = findUserByUsername(username);
        Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Expense không tồn tại"));
        
        // Why: Security check - ensure user owns this expense
        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền truy cập expense này");
        }
        
        return mapToResponse(expense);
    }
    
    /**
     * Why: Update existing expense với validation
     */
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request, String username) {
        User user = findUserByUsername(username);
        Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Expense không tồn tại"));
        
        // Why: Security check
        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền chỉnh sửa expense này");
        }
        
        Category category = findAndValidateCategory(request.getCategoryId(), user.getId());
        
        // Why: Update expense fields
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setNotes(request.getNotes());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setLocation(request.getLocation());
        expense.setReceiptUrl(request.getReceiptUrl());
        expense.setCategory(category);
        
        expense = expenseRepository.save(expense);
        return mapToResponse(expense);
    }
    
    /**
     * Why: Delete expense với ownership validation
     */
    public void deleteExpense(Long id, String username) {
        User user = findUserByUsername(username);
        Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Expense không tồn tại"));
        
        // Why: Security check
        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền xóa expense này");
        }
        
        expenseRepository.delete(expense);
    }
    
    /**
     * Why: Calculate monthly total cho dashboard
     */
    @Transactional(readOnly = true)
    public BigDecimal getMonthlyTotal(String username, int year, int month) {
        User user = findUserByUsername(username);
        return expenseRepository.calculateTotalByUserAndMonth(user.getId(), year, month);
    }
    
    /**
     * Why: Get recent expenses cho dashboard preview
     */
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getRecentExpenses(String username, int limit) {
        User user = findUserByUsername(username);
        List<Expense> expenses = expenseRepository.findRecentExpenses(user.getId(), limit);
        
        return expenses.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Why: Search expenses by description
     */
    @Transactional(readOnly = true)
    public Page<ExpenseResponse> searchExpenses(String username, String searchTerm, int page, int size) {
        User user = findUserByUsername(username);
        Pageable pageable = PageRequest.of(page, size);
        
        Page<Expense> expenses = expenseRepository
            .findByUserIdAndDescriptionContainingIgnoreCaseOrderByExpenseDateDescCreatedAtDesc(
                user.getId(), searchTerm, pageable);
        
        return expenses.map(this::mapToResponse);
    }
    
    // Helper Methods
    
    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User không tồn tại"));
    }
    
    private Category findAndValidateCategory(Long categoryId, Long userId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category không tồn tại"));
        
        // Why: Validate category thuộc về user hoặc là default category
        if (!category.getUser().getId().equals(userId) && !category.getIsDefault()) {
            throw new RuntimeException("Bạn không có quyền sử dụng category này");
        }
        
        return category;
    }
    
    private ExpenseResponse mapToResponse(Expense expense) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setAmount(expense.getAmount());
        response.setDescription(expense.getDescription());
        response.setNotes(expense.getNotes());
        response.setExpenseDate(expense.getExpenseDate());
        response.setLocation(expense.getLocation());
        response.setReceiptUrl(expense.getReceiptUrl());
        response.setCreatedAt(expense.getCreatedAt());
        response.setUpdatedAt(expense.getUpdatedAt());
        
        // Why: Include category information để avoid additional API calls
        Category category = expense.getCategory();
        response.setCategoryId(category.getId());
        response.setCategoryName(category.getName());
        response.setCategoryColorCode(category.getColorCode());
        response.setCategoryIconName(category.getIconName());
        
        return response;
    }
}
