package com.example.myfunds.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FundTransaction {
    private Long id;
    private Fund fund;
    private Long userId;
    private String transactionType; // BUY, SELL, FIXED_INVESTMENT
    private Double transactionAmount;
    private Double transactionShares;
    private Double transactionPrice;
    private Double fee;
    private String status; // PENDING, SUCCESS, FAILED
    private LocalDateTime transactionTime;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}