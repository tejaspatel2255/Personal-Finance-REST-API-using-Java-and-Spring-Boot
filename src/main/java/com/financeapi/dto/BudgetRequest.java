package com.financeapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetRequest {
    @NotNull
    private BigDecimal monthlyLimit;
    @NotNull
    private Long categoryId;
    @NotNull
    private Integer month;
    @NotNull
    private Integer year;
}
