package com.hung.expensive.controller;

import com.hung.expensive.dto.ApiResponse;
import com.hung.expensive.dto.ExpenseRequest;
import com.hung.expensive.dto.ExpenseResponse;
import com.hung.expensive.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Why: REST API controller cho expense management
 * Design decision: RESTful endpoints với proper HTTP status codes
 * Security: Use Authentication để get current user thay vì truyền username
 */
@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "http://localhost:5173") // Why: Allow React frontend access
public class ExpenseController {
    
    private final ExpenseService expenseService;
    
    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }
    
    /**
     * Why: Create new expense
     * POST /api/expenses
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseResponse>> createExpense(
            @Valid @RequestBody ExpenseRequest request,
            Authentication authentication) {
        
        try {
            ExpenseResponse expense = expenseService.createExpense(request, authentication.getName());
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Expense đã được tạo thành công", expense));
                
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * Why: Get paginated list of user expenses
     * GET /api/expenses?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ExpenseResponse>>> getUserExpenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        
        try {
            Page<ExpenseResponse> expenses = expenseService.getUserExpenses(
                authentication.getName(), page, size);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách expenses thành công", expenses));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * Why: Get expenses trong date range
     * GET /api/expenses/date-range?startDate=2024-01-01&endDate=2024-01-31
     */
    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<Page<ExpenseResponse>>> getExpensesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        
        try {
            Page<ExpenseResponse> expenses = expenseService.getUserExpensesByDateRange(
                authentication.getName(), startDate, endDate, page, size);
            
            return ResponseEntity.ok(new ApiResponse<>(true, 
                "Lấy expenses theo date range thành công", expenses));
                
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * Why: Get single expense by ID
     * GET /api/expenses/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> getExpenseById(
            @PathVariable Long id,
            Authentication authentication) {
        
        try {
            ExpenseResponse expense = expenseService.getExpenseById(id, authentication.getName());
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy expense thành công", expense));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * Why: Update existing expense
     * PUT /api/expenses/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request,
            Authentication authentication) {
        
        try {
            ExpenseResponse expense = expenseService.updateExpense(id, request, authentication.getName());
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Expense đã được cập nhật thành công", expense));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * Why: Delete expense
     * DELETE /api/expenses/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(
            @PathVariable Long id,
            Authentication authentication) {
        
        try {
            expenseService.deleteExpense(id, authentication.getName());
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Expense đã được xóa thành công", null));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * Why: Get monthly total cho dashboard
     * GET /api/expenses/monthly-total?year=2024&month=1
     */
    @GetMapping("/monthly-total")
    public ResponseEntity<ApiResponse<BigDecimal>> getMonthlyTotal(
            @RequestParam int year,
            @RequestParam int month,
            Authentication authentication) {
        
        try {
            BigDecimal total = expenseService.getMonthlyTotal(authentication.getName(), year, month);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy total tháng thành công", total));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * Why: Get recent expenses cho dashboard preview
     * GET /api/expenses/recent?limit=5
     */
    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> getRecentExpenses(
            @RequestParam(defaultValue = "5") int limit,
            Authentication authentication) {
        
        try {
            List<ExpenseResponse> expenses = expenseService.getRecentExpenses(
                authentication.getName(), limit);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy recent expenses thành công", expenses));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * Why: Search expenses by description
     * GET /api/expenses/search?term=coffee
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ExpenseResponse>>> searchExpenses(
            @RequestParam String term,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        
        try {
            Page<ExpenseResponse> expenses = expenseService.searchExpenses(
                authentication.getName(), term, page, size);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Tìm kiếm expenses thành công", expenses));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
