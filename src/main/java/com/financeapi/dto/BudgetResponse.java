package com.financeapi.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BudgetResponse {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private Integer month;
    private Integer year;
    private BigDecimal monthlyLimit;
    private BigDecimal spent;
    private BigDecimal remaining;
    private BigDecimal utilizationPercent;
    private boolean exceeded;
    private boolean closeToLimit;
}
