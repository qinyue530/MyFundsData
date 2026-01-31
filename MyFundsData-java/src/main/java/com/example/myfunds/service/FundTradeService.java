package com.example.myfunds.service;

import com.example.myfunds.entity.FixedInvestment;
import com.example.myfunds.entity.FundTransaction;
import com.example.myfunds.entity.UserFund;

import java.util.List;

public interface FundTradeService {
    /**
     * 手动添加基金持仓
     */
    UserFund addFundHolding(Long userId, String fundCode, Double shares, Double costPrice);

    /**
     * 加仓操作
     */
    FundTransaction buyFund(Long userId, String fundCode, Double amount);

    /**
     * 减仓操作
     */
    FundTransaction sellFund(Long userId, String fundCode, Double shares);

    /**
     * 设置定投计划
     */
    FixedInvestment setFixedInvestment(Long userId, String fundCode, Double amount, String frequency);

    /**
     * 暂停定投计划
     */
    FixedInvestment pauseFixedInvestment(Long id);

    /**
     * 恢复定投计划
     */
    FixedInvestment resumeFixedInvestment(Long id);

    /**
     * 停止定投计划
     */
    FixedInvestment stopFixedInvestment(Long id);

    /**
     * 获取用户的基金持仓列表
     */
    List<UserFund> getUserFunds(Long userId);

    /**
     * 获取用户的交易记录
     */
    List<FundTransaction> getUserTransactions(Long userId);

    /**
     * 获取用户的定投计划
     */
    List<FixedInvestment> getUserFixedInvestments(Long userId);

    /**
     * 执行定投计划
     */
    void executeFixedInvestments();
}