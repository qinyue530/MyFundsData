package com.example.myfunds.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FixedInvestment {
    private Long id;
    private Fund fund;
    private Long userId;
    private Double amount;
    private String frequency; // DAILY, WEEKLY, MONTHLY
    private LocalDateTime startDate;
    private LocalDateTime nextExecutionDate;
    private String status; // ACTIVE, PAUSED, STOPPED
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}