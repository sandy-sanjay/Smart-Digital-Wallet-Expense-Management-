package com.smartwallet.analyticsservice.controller;

import com.smartwallet.analyticsservice.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/{userId}/insights")
    public ResponseEntity<Map<String, Object>> getInsights(@PathVariable Long userId) {
        return ResponseEntity.ok(analyticsService.getInsights(userId));
    }
}
