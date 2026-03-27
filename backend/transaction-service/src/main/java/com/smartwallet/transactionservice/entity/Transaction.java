package com.smartwallet.transactionservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private String category; // e.g., Food, Travel, Salary

    @Column(nullable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();

    public enum TransactionType {
        INCOME, EXPENSE
    }
}
