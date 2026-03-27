package com.smartwallet.analyticsservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "analytics_snapshots")
@Data
public class AnalyticsSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    private Double totalIncome;
    private Double totalExpense;
    private Double netSavings;

    @Column(nullable = false)
    private LocalDateTime generatedAt = LocalDateTime.now();
    
    @PreUpdate
    public void preUpdate() {
        this.generatedAt = LocalDateTime.now();
    }
}
