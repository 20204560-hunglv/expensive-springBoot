package com.hung.expensive.expense.service;

import com.hung.expensive.dto.ExpenseRequest;
import com.hung.expensive.dto.ExpenseResponse;
import com.hung.expensive.entity.Expense;
import com.hung.expensive.entity.User;
import com.hung.expensive.entity.Category;
import com.hung.expensive.expense.repository.ExpenseRepository;
import com.hung.expensive.expense.client.UserServiceClient;
import com.hung.expensive.expense.client.CategoryServiceClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Why: Expense Service chứa business logic cho expense management
 * Design decision: Service layer pattern với external service calls
 * Business requirement: Handle expense CRUD, validation, reporting
 */
@Service
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserServiceClient userServiceClient;
    private final CategoryServiceClient categoryServiceClient;

    public ExpenseService(ExpenseRepository expenseRepository,
                         UserServiceClient userServiceClient,
                         CategoryServiceClient categoryServiceClient) {
        this.expenseRepository = expenseRepository;
        this.userServiceClient = userServiceClient;
        this.categoryServiceClient = categoryServiceClient;
    }

    /**
     * Why: Get all expenses với filtering và pagination
     * Business rule: Support multiple filters, pagination
     */
    public Page<ExpenseResponse> getAllExpenses(Long userId, LocalDate startDate, 
                                               LocalDate endDate, Long categoryId, Pageable pageable) {
        Page<Expense> expenses;
        
        if (userId != null && startDate != null && endDate != null) {
            expenses = expenseRepository.findByUserAndDateRange(userId, startDate, endDate, pageable);
        } else if (userId != null) {
            expenses = expenseRepository.findByUserId(userId, pageable);
        } else if (categoryId != null) {
            expenses = expenseRepository.findByCategoryId(categoryId, pageable);
        } else {
            expenses = expenseRepository.findAll(pageable);
        }

        return expenses.map(this::mapToExpenseResponse);
    }

    /**
     * Why: Get expense by ID
     * Business rule: Return specific expense details
     */
    public ExpenseResponse getExpenseById(Long id) {
        Optional<Expense> expenseOpt = expenseRepository.findById(id);
        if (expenseOpt.isEmpty()) {
            throw new RuntimeException("Expense not found with ID: " + id);
        }

        return mapToExpenseResponse(expenseOpt.get());
    }

    /**
     * Why: Create new expense
     * Business rule: Validate user và category existence, create expense
     */
    public ExpenseResponse createExpense(ExpenseRequest request) {
        // Why: Validate user exists
        try {
            userServiceClient.getUserProfile(request.getUserId());
        } catch (Exception e) {
            throw new RuntimeException("User not found: " + request.getUserId());
        }

        // Why: Validate category exists
        try {
            categoryServiceClient.getCategoryById(request.getCategoryId());
        } catch (Exception e) {
            throw new RuntimeException("Category not found: " + request.getCategoryId());
        }

        // Why: Create expense entity
        Expense expense = new Expense();
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setNotes(request.getNotes());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setLocation(request.getLocation());
        expense.setReceiptUrl(request.getReceiptUrl());

        // Why: Set user và category (simplified - trong thực tế cần proper entity mapping)
        User user = new User();
        user.setId(request.getUserId());
        expense.setUser(user);

        Category category = new Category();
        category.setId(request.getCategoryId());
        expense.setCategory(category);

        Expense savedExpense = expenseRepository.save(expense);
        return mapToExpenseResponse(savedExpense);
    }

    /**
     * Why: Update existing expense
     * Business rule: Validate ownership, update fields
     */
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {
        Optional<Expense> expenseOpt = expenseRepository.findById(id);
        if (expenseOpt.isEmpty()) {
            throw new RuntimeException("Expense not found with ID: " + id);
        }

        Expense expense = expenseOpt.get();

        // Why: Update fields
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setNotes(request.getNotes());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setLocation(request.getLocation());
        expense.setReceiptUrl(request.getReceiptUrl());

        Expense updatedExpense = expenseRepository.save(expense);
        return mapToExpenseResponse(updatedExpense);
    }

    /**
     * Why: Delete expense
     * Business rule: Soft delete hoặc hard delete
     */
    public void deleteExpense(Long id) {
        Optional<Expense> expenseOpt = expenseRepository.findById(id);
        if (expenseOpt.isEmpty()) {
            throw new RuntimeException("Expense not found with ID: " + id);
        }

        expenseRepository.deleteById(id);
    }

    /**
     * Why: Get expenses by user
     * Business rule: Return all expenses for specific user
     */
    public List<ExpenseResponse> getExpensesByUser(Long userId) {
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        return expenses.stream()
                .map(this::mapToExpenseResponse)
                .collect(Collectors.toList());
    }

    /**
     * Why: Get total expenses by user
     * Business rule: Calculate total amount for user
     */
    public BigDecimal getTotalExpensesByUser(Long userId) {
        return expenseRepository.getTotalExpensesByUser(userId);
    }

    /**
     * Why: Map Expense entity to ExpenseResponse DTO
     * Design decision: Separate mapping logic để maintain clean code
     */
    private ExpenseResponse mapToExpenseResponse(Expense expense) {
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
        
        // Why: Set user và category IDs
        if (expense.getUser() != null) {
            response.setUserId(expense.getUser().getId());
        }
        if (expense.getCategory() != null) {
            response.setCategoryId(expense.getCategory().getId());
        }
        
        return response;
    }
}
