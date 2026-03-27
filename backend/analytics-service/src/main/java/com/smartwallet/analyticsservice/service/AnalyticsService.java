package com.smartwallet.analyticsservice.service;

import com.smartwallet.analyticsservice.entity.AnalyticsSnapshot;
import com.smartwallet.analyticsservice.repository.AnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private AnalyticsRepository analyticsRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String TX_SERVICE_URL = "http://localhost:8083/api/v1/transactions/user/";

    @Transactional
    public Map<String, Object> getInsights(Long userId) {
        // 1. Fetch live transactions
        Map[] transactions = restTemplate.getForObject(TX_SERVICE_URL + userId, Map[].class);
        if (transactions == null) return Collections.emptyMap();
        
        List<Map> txList = Arrays.asList(transactions);
        
        // 2. Aggregate logic
        double totalExpense = txList.stream()
                .filter(tx -> "EXPENSE".equals(tx.get("type")))
                .mapToDouble(tx -> Double.parseDouble(tx.get("amount").toString()))
                .sum();
                
        double totalIncome = txList.stream()
                .filter(tx -> "INCOME".equals(tx.get("type")))
                .mapToDouble(tx -> Double.parseDouble(tx.get("amount").toString()))
                .sum();

        double netSavings = totalIncome - totalExpense;

        Map<String, Double> categoryBreakdown = txList.stream()
                .filter(tx -> "EXPENSE".equals(tx.get("type")))
                .collect(Collectors.groupingBy(
                        tx -> tx.get("category").toString(),
                        Collectors.summingDouble(tx -> Double.parseDouble(tx.get("amount").toString()))
                ));

        // 3. Save to Analytics DB Layer (Snapshotting)
        AnalyticsSnapshot snapshot = analyticsRepository.findByUserId(userId)
                .orElseGet(() -> {
                    AnalyticsSnapshot s = new AnalyticsSnapshot();
                    s.setUserId(userId);
                    return s;
                });
        
        snapshot.setTotalIncome(totalIncome);
        snapshot.setTotalExpense(totalExpense);
        snapshot.setNetSavings(netSavings);
        analyticsRepository.save(snapshot);

        // 4. Return complex mapped response
        Map<String, Object> insights = new HashMap<>();
        insights.put("snapshotId", snapshot.getId());
        insights.put("userId", userId);
        insights.put("totalIncome", totalIncome);
        insights.put("totalExpense", totalExpense);
        insights.put("netSavings", netSavings);
        insights.put("expensesByCategory", categoryBreakdown);
        insights.put("generatedAt", snapshot.getGeneratedAt());
        
        return insights;
    }
}
