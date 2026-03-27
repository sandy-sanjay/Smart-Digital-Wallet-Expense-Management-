package com.smartwallet.transactionservice.dto;

import com.smartwallet.transactionservice.entity.Transaction.TransactionType;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionRequest {
    private Long userId;
    private BigDecimal amount;
    private TransactionType type;
    private String category;
}
