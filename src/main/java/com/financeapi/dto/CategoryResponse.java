package com.financeapi.dto;

import com.financeapi.model.CategoryType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private CategoryType type;
}
