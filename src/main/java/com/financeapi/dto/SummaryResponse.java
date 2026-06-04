package com.financeapi.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class SummaryResponse {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance;
    private List<CategorySpending> spendingByCategory;
    private List<BudgetResponse> budgetAlerts;

    @Data
    @Builder
    public static class CategorySpending {
        private Long categoryId;
        private String categoryName;
        private BigDecimal totalSpent;
    }
}
