package com.example.myfunds.scheduler;

import com.example.myfunds.service.FundDataService;
import com.example.myfunds.service.FundTradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FundDataScheduler {

    @Autowired
    private FundDataService fundDataService;

    @Autowired
    private FundTradeService fundTradeService;

    /**
     * 每天上午9:30更新所有基金数据
     * 表达式：秒 分 时 日 月 周
     */
    @Scheduled(cron = "0 30 9 * * ?")
    public void updateFundDataDaily() {
        log.info("开始执行每日基金数据更新任务");
        fundDataService.updateAllFundsData();
        log.info("每日基金数据更新任务完成");
    }

    /**
     * 每天下午15:00执行定投计划
     */
    @Scheduled(cron = "0 0 15 * * ?")
    public void executeFixedInvestments() {
        log.info("开始执行每日定投计划");
        fundTradeService.executeFixedInvestments();
        log.info("每日定投计划执行完成");
    }

    /**
     * 每小时更新一次基金数据（可选，用于测试）
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void updateFundDataHourly() {
        log.info("开始执行每小时基金数据更新任务");
        fundDataService.updateAllFundsData();
        log.info("每小时基金数据更新任务完成");
    }
}