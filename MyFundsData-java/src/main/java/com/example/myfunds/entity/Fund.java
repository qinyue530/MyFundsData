package com.example.myfunds.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Fund {
    private Long id;
    private String fundCode;
    private String fundName;
    private String fundType;
    private String manager;
    private LocalDateTime establishDate;
    private Double latestNav;
    private Double dayGrowth;
    private Double weekGrowth;
    private Double monthGrowth;
    private Double quarterGrowth;
    private Double yearGrowth;
    
    // 估值相关字段
    private Double estimatedDayGrowth; // 当日预估涨跌幅
    private Double estimatedNav; // 当日预估净值
    private Double estimatedProfit; // 当日预估收益
    
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}