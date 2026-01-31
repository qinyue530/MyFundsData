package com.example.myfunds.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserFund {
    private Long id;
    private Fund fund;
    private Long userId;
    private Double totalShares;
    private Double averageCost;
    private Double totalCost;
    private Double currentValue;
    private Double profitLoss;
    private Double profitLossRatio;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}