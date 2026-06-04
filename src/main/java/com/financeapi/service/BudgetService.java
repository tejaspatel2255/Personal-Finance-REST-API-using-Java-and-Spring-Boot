package com.financeapi.service;

import com.financeapi.dto.BudgetRequest;
import com.financeapi.dto.BudgetResponse;
import com.financeapi.exception.ResourceNotFoundException;
import com.financeapi.model.Budget;
import com.financeapi.model.CategoryType;
import com.financeapi.repository.BudgetRepository;
import com.financeapi.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService;
    private final UserContextService userContextService;

    public BudgetResponse upsert(BudgetRequest request) {
        var user = userContextService.currentUser();
        var category = categoryService.requireOwnedCategory(request.getCategoryId());
        if (category.getType() != CategoryType.EXPENSE) {
            throw new IllegalArgumentException("Budgets can only be set for expense categories");
        }
        BigDecimal spent = transactionRepository.findByUserId(user.getId()).stream()
                .filter(t -> t.getCategory().getId().equals(category.getId()))
                .filter(t -> t.getDate().getMonthValue() == request.getMonth() && t.getDate().getYear() == request.getYear())
                .map(t -> t.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Budget budget = budgetRepository.findByUserIdAndCategoryIdAndMonthAndYear(user.getId(), category.getId(), request.getMonth(), request.getYear())
                .orElse(Budget.builder().user(user).category(category).month(request.getMonth()).year(request.getYear()).build());
        budget.setMonthlyLimit(request.getMonthlyLimit());
        budget.setSpent(spent);
        return toResponse(budgetRepository.save(budget));
    }

    public List<BudgetResponse> getCurrentMonthBudgets() {
        var now = LocalDate.now();
        return budgetRepository.findByUserIdAndMonthAndYear(userContextService.currentUser().getId(), now.getMonthValue(), now.getYear())
                .stream().map(this::toResponse).toList();
    }

    public List<BudgetResponse> getAllForMonth(Integer month, Integer year) {
        return budgetRepository.findByUserIdAndMonthAndYear(userContextService.currentUser().getId(), month, year)
                .stream().map(this::toResponse).toList();
    }

    public List<BudgetResponse> alertsForMonth(Integer month, Integer year) {
        return getAllForMonth(month, year).stream()
                .filter(b -> b.isExceeded() || b.isCloseToLimit())
                .toList();
    }

    private BudgetResponse toResponse(Budget budget) {
        BigDecimal remaining = budget.getMonthlyLimit().subtract(budget.getSpent());
        BigDecimal utilization = budget.getMonthlyLimit().compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : budget.getSpent().multiply(BigDecimal.valueOf(100)).divide(budget.getMonthlyLimit(), 2, RoundingMode.HALF_UP);
        return BudgetResponse.builder()
                .id(budget.getId())
                .categoryId(budget.getCategory().getId())
                .categoryName(budget.getCategory().getName())
                .month(budget.getMonth())
                .year(budget.getYear())
                .monthlyLimit(budget.getMonthlyLimit())
                .spent(budget.getSpent())
                .remaining(remaining)
                .utilizationPercent(utilization)
                .exceeded(remaining.signum() < 0)
                .closeToLimit(utilization.compareTo(BigDecimal.valueOf(80)) >= 0 && utilization.compareTo(BigDecimal.valueOf(100)) < 0)
                .build();
    }
}
