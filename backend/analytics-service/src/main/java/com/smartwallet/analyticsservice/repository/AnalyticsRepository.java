package com.smartwallet.analyticsservice.repository;

import com.smartwallet.analyticsservice.entity.AnalyticsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AnalyticsRepository extends JpaRepository<AnalyticsSnapshot, Long> {
    Optional<AnalyticsSnapshot> findByUserId(Long userId);
}
