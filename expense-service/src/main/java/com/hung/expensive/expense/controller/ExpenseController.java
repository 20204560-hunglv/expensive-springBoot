package com.hung.expensive.expense.controller;

import com.hung.expensive.dto.ApiResponse;
import com.hung.expensive.dto.ExpenseRequest;
import com.hung.expensive.dto.ExpenseResponse;
import com.hung.expensive.expense.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Why: Expense Controller xử lý expense management endpoints
 * Design decision: RESTful API design với proper HTTP methods
 * Business requirement: Cần CRUD expenses, reporting, analytics
 */
@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    /**
     * Why: Get all expenses với pagination và filtering
     * Business rule: Support pagination, filtering by user, date range
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ExpenseResponse>>> getAllExpenses(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long categoryId,
            Pageable pageable) {
        try {
            Page<ExpenseResponse> expenses = expenseService.getAllExpenses(
                    userId, startDate, endDate, categoryId, pageable);
            return ResponseEntity.ok(ApiResponse.success(expenses, "Expenses retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve expenses: " + e.getMessage()));
        }
    }

    /**
     * Why: Get expense by ID
     * Business rule: Return specific expense details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> getExpenseById(@PathVariable Long id) {
        try {
            ExpenseResponse expense = expenseService.getExpenseById(id);
            return ResponseEntity.ok(ApiResponse.success(expense, "Expense retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Expense not found: " + e.getMessage()));
        }
    }

    /**
     * Why: Create new expense
     * Business rule: Validate input, check user và category existence
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseResponse>> createExpense(@Valid @RequestBody ExpenseRequest request) {
        try {
            ExpenseResponse expense = expenseService.createExpense(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(expense, "Expense created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create expense: " + e.getMessage()));
        }
    }

    /**
     * Why: Update existing expense
     * Business rule: Validate input, check ownership
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request) {
        try {
            ExpenseResponse expense = expenseService.updateExpense(id, request);
            return ResponseEntity.ok(ApiResponse.success(expense, "Expense updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update expense: " + e.getMessage()));
        }
    }

    /**
     * Why: Delete expense
     * Business rule: Check ownership, soft delete
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteExpense(@PathVariable Long id) {
        try {
            expenseService.deleteExpense(id);
            return ResponseEntity.ok(ApiResponse.success("Expense deleted successfully", "Delete successful"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete expense: " + e.getMessage()));
        }
    }

    /**
     * Why: Get expenses by user
     * Business rule: Return all expenses for specific user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> getExpensesByUser(@PathVariable Long userId) {
        try {
            List<ExpenseResponse> expenses = expenseService.getExpensesByUser(userId);
            return ResponseEntity.ok(ApiResponse.success(expenses, "User expenses retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve user expenses: " + e.getMessage()));
        }
    }

    /**
     * Why: Get total expenses by user
     * Business rule: Calculate total amount for user
     */
    @GetMapping("/user/{userId}/total")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalExpensesByUser(@PathVariable Long userId) {
        try {
            BigDecimal total = expenseService.getTotalExpensesByUser(userId);
            return ResponseEntity.ok(ApiResponse.success(total, "Total expenses calculated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to calculate total: " + e.getMessage()));
        }
    }

    /**
     * Why: Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Expense Service is running", "Service is healthy"));
    }
}
