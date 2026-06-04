package com.financeapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "budgets")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "monthly_limit", nullable = false, precision = 19, scale = 2)
    private BigDecimal monthlyLimit;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal spent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer year;
}
