package com.financeapi.controller;

import com.financeapi.dto.SummaryResponse;
import com.financeapi.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summary")
@RequiredArgsConstructor
public class SummaryController {
    private final SummaryService summaryService;

    @GetMapping
    public ResponseEntity<SummaryResponse> summary(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(summaryService.getSummary(month, year));
    }
}
