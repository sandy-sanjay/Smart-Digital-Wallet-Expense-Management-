package com.smartwallet.transactionservice.service;

import com.smartwallet.transactionservice.dto.TransactionRequest;
import com.smartwallet.transactionservice.entity.Transaction;
import com.smartwallet.transactionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String WALLET_SERVICE_URL = "http://localhost:8082/api/v1/wallet/";

    public Transaction createTransaction(TransactionRequest request) {
        // Automatically interact with Wallet Service to deduct or add funds
        String url = WALLET_SERVICE_URL + request.getUserId();
        if (request.getType() == Transaction.TransactionType.INCOME) {
            url += "/add?amount=" + request.getAmount();
        } else {
            url += "/deduct?amount=" + request.getAmount();
        }
        
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Wallet operation failed");
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not process wallet transaction: " + e.getMessage());
        }

        Transaction tx = new Transaction();
        tx.setUserId(request.getUserId());
        tx.setAmount(request.getAmount());
        tx.setType(request.getType());
        tx.setCategory(request.getCategory());
        
        return transactionRepository.save(tx);
    }

    public List<Transaction> getUserTransactions(Long userId) {
        return transactionRepository.findByUserIdOrderByTransactionDateDesc(userId);
    }
}
