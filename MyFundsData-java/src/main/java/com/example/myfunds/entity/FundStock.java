package com.example.myfunds.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FundStock {
    private Long id;
    private Fund fund;
    private String stockCode;
    private String stockName;
    private Double holdingRatio;
    private Double stockPrice;
    private Double dayGrowth;
    private Double holdingValue;
    private LocalDateTime reportDate;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}