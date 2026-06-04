package com.financeapi.dto;

import com.financeapi.model.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank
    private String name;
    @NotNull
    private CategoryType type;
}
