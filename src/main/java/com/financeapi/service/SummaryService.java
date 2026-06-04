package com.financeapi.service;

import com.financeapi.dto.BudgetResponse;
import com.financeapi.dto.SummaryResponse;
import com.financeapi.repository.CategoryRepository;
import com.financeapi.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetService budgetService;
    private final UserContextService userContextService;

    public SummaryResponse getSummary(Integer month, Integer year) {
        Long userId = userContextService.currentUser().getId();
        BigDecimal income = transactionRepository.totalIncome(userId, month, year);
        BigDecimal expenses = transactionRepository.totalExpenses(userId, month, year);
        List<SummaryResponse.CategorySpending> spending = categoryRepository.findByUserId(userId).stream()
                .map(c -> SummaryResponse.CategorySpending.builder()
                        .categoryId(c.getId())
                        .categoryName(c.getName())
                        .totalSpent(transactionRepository.findByUserId(userId).stream()
                                .filter(t -> t.getCategory().getId().equals(c.getId()))
                                .filter(t -> month == null || t.getDate().getMonthValue() == month)
                                .filter(t -> year == null || t.getDate().getYear() == year)
                                .map(t -> t.getAmount())
                                .reduce(BigDecimal.ZERO, BigDecimal::add))
                        .build())
                .toList();
        Integer effectiveMonth = month != null ? month : LocalDate.now().getMonthValue();
        Integer effectiveYear = year != null ? year : LocalDate.now().getYear();
        List<BudgetResponse> alerts = budgetService.alertsForMonth(effectiveMonth, effectiveYear);
        return SummaryResponse.builder()
                .totalIncome(income)
                .totalExpenses(expenses)
                .netBalance(income.subtract(expenses))
                .spendingByCategory(spending)
                .budgetAlerts(alerts)
                .build();
    }
}
