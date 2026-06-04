package com.financeapi.controller;

import com.financeapi.dto.BudgetRequest;
import com.financeapi.dto.BudgetResponse;
import com.financeapi.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponse> upsert(@Valid @RequestBody BudgetRequest request) {
        return ResponseEntity.ok(budgetService.upsert(request));
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getBudgets(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        if (month != null && year != null) {
            return ResponseEntity.ok(budgetService.getAllForMonth(month, year));
        }
        return ResponseEntity.ok(budgetService.getCurrentMonthBudgets());
    }
}
