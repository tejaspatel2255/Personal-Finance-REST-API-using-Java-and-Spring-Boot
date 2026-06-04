package com.financeapi.dto;

import com.financeapi.model.CategoryType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDate date;
    private Long categoryId;
    private String categoryName;
    private CategoryType categoryType;
}
