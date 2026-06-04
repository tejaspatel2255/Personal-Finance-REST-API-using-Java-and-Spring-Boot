package com.financeapi.controller;

import com.financeapi.dto.TransactionRequest;
import com.financeapi.dto.TransactionResponse;
import com.financeapi.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAll(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        return ResponseEntity.ok(transactionService.getAll(startDate, endDate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(@PathVariable Long id, @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
