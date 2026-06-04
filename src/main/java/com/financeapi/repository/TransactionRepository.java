package com.financeapi.repository;

import com.financeapi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    @Query(value = """
        select coalesce(sum(amount), 0)
        from transactions t
        join categories c on c.id = t.category_id
        where t.user_id = :userId
          and c.type = 'INCOME'
          and (:month is null or extract(month from t.date) = :month)
          and (:year is null or extract(year from t.date) = :year)
    """, nativeQuery = true)
    BigDecimal totalIncome(Long userId, Integer month, Integer year);

    @Query(value = """
        select coalesce(sum(amount), 0)
        from transactions t
        join categories c on c.id = t.category_id
        where t.user_id = :userId
          and c.type = 'EXPENSE'
          and (:month is null or extract(month from t.date) = :month)
          and (:year is null or extract(year from t.date) = :year)
    """, nativeQuery = true)
    BigDecimal totalExpenses(Long userId, Integer month, Integer year);
}
