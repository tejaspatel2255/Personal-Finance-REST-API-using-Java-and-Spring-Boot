package com.financeapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    private int status;
    private String message;
    private String timestamp;
}
