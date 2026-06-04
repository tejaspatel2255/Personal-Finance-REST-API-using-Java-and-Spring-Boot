package com.financeapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequest {
    @NotNull
    private BigDecimal amount;
    private String description;
    @NotNull
    private LocalDate date;
    @NotNull
    private Long categoryId;
}
