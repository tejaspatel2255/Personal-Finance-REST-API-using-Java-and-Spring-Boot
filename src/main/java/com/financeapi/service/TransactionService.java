package com.financeapi.service;

import com.financeapi.dto.TransactionRequest;
import com.financeapi.dto.TransactionResponse;
import com.financeapi.exception.ResourceNotFoundException;
import com.financeapi.exception.UnauthorizedActionException;
import com.financeapi.model.Transaction;
import com.financeapi.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService;
    private final UserContextService userContextService;
    private final BudgetService budgetService;

    public TransactionResponse create(TransactionRequest request) {
        var user = userContextService.currentUser();
        var category = categoryService.requireOwnedCategory(request.getCategoryId());
        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .description(request.getDescription())
                .date(request.getDate())
                .category(category)
                .user(user)
                .build();
        TransactionResponse response = toResponse(transactionRepository.save(transaction));
        budgetService.recalculateBudgetSpent(user.getId(), category.getId(), transaction.getDate().getMonthValue(), transaction.getDate().getYear());
        return response;
    }

    public List<TransactionResponse> getAll(LocalDate startDate, LocalDate endDate) {
        Long userId = userContextService.currentUser().getId();
        List<Transaction> transactions = (startDate != null && endDate != null)
                ? transactionRepository.findByUserIdAndDateBetween(userId, startDate, endDate)
                : transactionRepository.findByUserId(userId);
        return transactions.stream().map(this::toResponse).toList();
    }

    public TransactionResponse update(Long id, TransactionRequest request) {
        Long userId = userContextService.currentUser().getId();
        Transaction transaction = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        
        Long oldCategoryId = transaction.getCategory().getId();
        int oldMonth = transaction.getDate().getMonthValue();
        int oldYear = transaction.getDate().getYear();

        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());
        transaction.setCategory(categoryService.requireOwnedCategory(request.getCategoryId()));
        
        TransactionResponse response = toResponse(transactionRepository.save(transaction));
        
        budgetService.recalculateBudgetSpent(userId, oldCategoryId, oldMonth, oldYear);
        if (!oldCategoryId.equals(transaction.getCategory().getId()) || oldMonth != transaction.getDate().getMonthValue() || oldYear != transaction.getDate().getYear()) {
            budgetService.recalculateBudgetSpent(userId, transaction.getCategory().getId(), transaction.getDate().getMonthValue(), transaction.getDate().getYear());
        }
        
        return response;
    }

    public void delete(Long id) {
        Long userId = userContextService.currentUser().getId();
        Transaction transaction = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        
        Long categoryId = transaction.getCategory().getId();
        int month = transaction.getDate().getMonthValue();
        int year = transaction.getDate().getYear();
        
        transactionRepository.delete(transaction);
        
        budgetService.recalculateBudgetSpent(userId, categoryId, month, year);
    }

    private TransactionResponse toResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .description(t.getDescription())
                .date(t.getDate())
                .categoryId(t.getCategory().getId())
                .categoryName(t.getCategory().getName())
                .categoryType(t.getCategory().getType())
                .build();
    }
}
