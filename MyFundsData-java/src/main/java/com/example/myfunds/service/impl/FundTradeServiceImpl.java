package com.example.myfunds.service.impl;

import com.example.myfunds.entity.*;
import com.example.myfunds.mapper.*;
import com.example.myfunds.service.FundDataService;
import com.example.myfunds.service.FundTradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FundTradeServiceImpl implements FundTradeService {

    @Autowired
    private FundMapper fundMapper;

    @Autowired
    private UserFundMapper userFundMapper;

    @Autowired
    private FundTransactionMapper transactionMapper;

    @Autowired
    private FixedInvestmentMapper fixedInvestmentMapper;

    @Autowired
    private FundDataService fundDataService;

    // 交易费率，实际项目中可配置
    private static final double TRADE_FEE_RATE = 0.0015;

    @Override
    @Transactional
    public UserFund addFundHolding(Long userId, String fundCode, Double shares, Double costPrice) {
        log.info("开始手动添加基金持仓，用户ID：{}, 基金代码：{}, 份额：{}, 成本价：{}", 
                userId, fundCode, shares, costPrice);
        
        try {
            // 1. 确保基金数据存在
            log.debug("步骤1：确保基金数据存在，基金代码：{}", fundCode);
            Fund fund = fundDataService.refreshFundData(fundCode);
            
            // 2. 检查是否已存在持仓
            log.debug("步骤2：检查是否已存在持仓，用户ID：{}, 基金ID：{}", userId, fund.getId());
            Optional<UserFund> existingHolding = userFundMapper.selectByUserIdAndFundId(userId, fund.getId());
            if (existingHolding.isPresent()) {
                log.error("该基金已存在持仓，请勿重复添加，用户ID：{}, 基金代码：{}", userId, fundCode);
                throw new RuntimeException("该基金已存在持仓，请勿重复添加");
            }
            
            // 3. 创建新的持仓记录
            log.debug("步骤3：创建新的持仓记录，用户ID：{}, 基金ID：{}", userId, fund.getId());
            UserFund userFund = new UserFund();
            userFund.setUserId(userId);
            userFund.setFund(fund);
            userFund.setTotalShares(shares);
            userFund.setAverageCost(costPrice);
            userFund.setTotalCost(shares * costPrice);
            userFund.setCurrentValue(shares * fund.getLatestNav());
            userFund.setProfitLoss(userFund.getCurrentValue() - userFund.getTotalCost());
            userFund.setProfitLossRatio((userFund.getProfitLoss() / userFund.getTotalCost()) * 100);
            
            userFundMapper.insert(userFund);
            log.info("手动添加基金持仓成功，用户ID：{}, 基金代码：{}, 持仓ID：{}", 
                    userId, fundCode, userFund.getId());
            return userFund;
        } catch (Exception e) {
            log.error("手动添加基金持仓失败，用户ID：{}, 基金代码：{}, 错误原因：{}", 
                    userId, fundCode, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public FundTransaction buyFund(Long userId, String fundCode, Double amount) {
        log.info("开始基金加仓操作，用户ID：{}, 基金代码：{}, 加仓金额：{}", 
                userId, fundCode, amount);
        
        try {
            // 1. 确保基金数据存在
            log.debug("步骤1：确保基金数据存在，基金代码：{}", fundCode);
            Fund fund = fundDataService.refreshFundData(fundCode);
            
            // 2. 计算交易费用和实际购买份额
            log.debug("步骤2：计算交易费用和实际购买份额，基金最新净值：{}", fund.getLatestNav());
            double fee = amount * TRADE_FEE_RATE;
            double actualAmount = amount - fee;
            double shares = actualAmount / fund.getLatestNav();
            log.debug("计算结果：交易费用={}, 实际购买金额={}, 购买份额={}", fee, actualAmount, shares);
            
            // 3. 创建交易记录
            log.debug("步骤3：创建交易记录，用户ID：{}, 基金ID：{}", userId, fund.getId());
            FundTransaction transaction = new FundTransaction();
            transaction.setUserId(userId);
            transaction.setFund(fund);
            transaction.setTransactionType("BUY");
            transaction.setTransactionAmount(amount);
            transaction.setTransactionShares(shares);
            transaction.setTransactionPrice(fund.getLatestNav());
            transaction.setFee(fee);
            transaction.setStatus("SUCCESS");
            transaction.setTransactionTime(LocalDateTime.now());
            
            transactionMapper.insert(transaction);
            log.debug("交易记录创建成功，交易ID：{}", transaction.getId());
            
            // 4. 更新用户持仓
            log.debug("步骤4：更新用户持仓，用户ID：{}, 基金ID：{}", userId, fund.getId());
            updateUserFundHolding(userId, fund, shares, fund.getLatestNav(), amount);
            
            log.info("基金加仓操作成功，用户ID：{}, 基金代码：{}, 交易ID：{}, 交易金额：{}, 购买份额：{}", 
                    userId, fundCode, transaction.getId(), transaction.getTransactionAmount(), shares);
            return transaction;
        } catch (Exception e) {
            log.error("基金加仓操作失败，用户ID：{}, 基金代码：{}, 错误原因：{}", 
                    userId, fundCode, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public FundTransaction sellFund(Long userId, String fundCode, Double shares) {
        log.info("开始基金减仓操作，用户ID：{}, 基金代码：{}, 减仓份额：{}", 
                userId, fundCode, shares);
        
        try {
            // 1. 确保基金数据存在
            log.debug("步骤1：确保基金数据存在，基金代码：{}", fundCode);
            Fund fund = fundDataService.refreshFundData(fundCode);
            
            // 2. 检查用户持仓
            log.debug("步骤2：检查用户持仓，用户ID：{}, 基金ID：{}", userId, fund.getId());
            Optional<UserFund> userFundOptional = userFundMapper.selectByUserIdAndFundId(userId, fund.getId());
            if (!userFundOptional.isPresent()) {
                log.error("用户未持有该基金，用户ID：{}, 基金代码：{}", userId, fundCode);
                throw new RuntimeException("用户未持有该基金");
            }
            
            UserFund userFund = userFundOptional.get();
            if (userFund.getTotalShares() < shares) {
                log.error("持仓份额不足，用户ID：{}, 基金代码：{}, 持仓份额：{}, 请求减仓份额：{}", 
                        userId, fundCode, userFund.getTotalShares(), shares);
                throw new RuntimeException("持仓份额不足");
            }
            
            // 3. 计算交易费用和实际到账金额
            log.debug("步骤3：计算交易费用和实际到账金额，基金最新净值：{}", fund.getLatestNav());
            double amount = shares * fund.getLatestNav();
            double fee = amount * TRADE_FEE_RATE;
            double actualAmount = amount - fee;
            log.debug("计算结果：交易金额={}, 交易费用={}, 实际到账金额={}", amount, fee, actualAmount);
            
            // 4. 创建交易记录
            log.debug("步骤4：创建交易记录，用户ID：{}, 基金ID：{}", userId, fund.getId());
            FundTransaction transaction = new FundTransaction();
            transaction.setUserId(userId);
            transaction.setFund(fund);
            transaction.setTransactionType("SELL");
            transaction.setTransactionAmount(amount);
            transaction.setTransactionShares(shares);
            transaction.setTransactionPrice(fund.getLatestNav());
            transaction.setFee(fee);
            transaction.setStatus("SUCCESS");
            transaction.setTransactionTime(LocalDateTime.now());
            
            transactionMapper.insert(transaction);
            log.debug("交易记录创建成功，交易ID：{}", transaction.getId());
            
            // 5. 更新用户持仓
            log.debug("步骤5：更新用户持仓，用户ID：{}, 基金ID：{}", userId, fund.getId());
            updateUserFundHolding(userId, fund, -shares, fund.getLatestNav(), -amount);
            
            log.info("基金减仓操作成功，用户ID：{}, 基金代码：{}, 交易ID：{}, 交易金额：{}, 减仓份额：{}", 
                    userId, fundCode, transaction.getId(), transaction.getTransactionAmount(), shares);
            return transaction;
        } catch (Exception e) {
            log.error("基金减仓操作失败，用户ID：{}, 基金代码：{}, 错误原因：{}", 
                    userId, fundCode, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public FixedInvestment setFixedInvestment(Long userId, String fundCode, Double amount, String frequency) {
        log.info("开始设置定投计划，用户ID：{}, 基金代码：{}, 定投金额：{}, 定投频率：{}", 
                userId, fundCode, amount, frequency);
        
        try {
            // 1. 确保基金数据存在
            log.debug("步骤1：确保基金数据存在，基金代码：{}", fundCode);
            Fund fund = fundDataService.refreshFundData(fundCode);
            
            // 2. 创建定投计划
            log.debug("步骤2：创建定投计划，用户ID：{}, 基金ID：{}", userId, fund.getId());
            FixedInvestment plan = new FixedInvestment();
            plan.setUserId(userId);
            plan.setFund(fund);
            plan.setAmount(amount);
            plan.setFrequency(frequency);
            plan.setStartDate(LocalDateTime.now());
            plan.setNextExecutionDate(calculateNextExecutionDate(LocalDateTime.now(), frequency));
            plan.setStatus("ACTIVE");
            
            fixedInvestmentMapper.insert(plan);
            log.info("设置定投计划成功，用户ID：{}, 基金代码：{}, 定投计划ID：{}, 定投频率：{}", 
                    userId, fundCode, plan.getId(), frequency);
            return plan;
        } catch (Exception e) {
            log.error("设置定投计划失败，用户ID：{}, 基金代码：{}, 错误原因：{}", 
                    userId, fundCode, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public FixedInvestment pauseFixedInvestment(Long id) {
        return updateFixedInvestmentStatus(id, "PAUSED");
    }

    @Override
    @Transactional
    public FixedInvestment resumeFixedInvestment(Long id) {
        FixedInvestment plan = updateFixedInvestmentStatus(id, "ACTIVE");
        // 重新计算下次执行时间
        plan.setNextExecutionDate(calculateNextExecutionDate(LocalDateTime.now(), plan.getFrequency()));
        fixedInvestmentMapper.update(plan);
        return plan;
    }

    @Override
    @Transactional
    public FixedInvestment stopFixedInvestment(Long id) {
        return updateFixedInvestmentStatus(id, "STOPPED");
    }

    @Override
    public List<UserFund> getUserFunds(Long userId) {
        log.info("开始获取用户基金持仓列表，用户ID：{}", userId);
        try {
            List<UserFund> userFunds = userFundMapper.selectByUserId(userId);
            log.info("获取用户基金持仓列表成功，用户ID：{}, 持仓数量：{}", userId, userFunds.size());
            log.debug("持仓详情：{}", userFunds);
            return userFunds;
        } catch (Exception e) {
            log.error("获取用户基金持仓列表失败，用户ID：{}, 错误原因：{}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<FundTransaction> getUserTransactions(Long userId) {
        log.info("开始获取用户交易记录，用户ID：{}", userId);
        try {
            List<FundTransaction> transactions = transactionMapper.selectByUserId(userId);
            log.info("获取用户交易记录成功，用户ID：{}, 交易记录数量：{}", userId, transactions.size());
            log.debug("交易记录详情：{}", transactions);
            return transactions;
        } catch (Exception e) {
            log.error("获取用户交易记录失败，用户ID：{}, 错误原因：{}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<FixedInvestment> getUserFixedInvestments(Long userId) {
        log.info("开始获取用户定投计划，用户ID：{}", userId);
        try {
            List<FixedInvestment> plans = fixedInvestmentMapper.selectByUserId(userId);
            log.info("获取用户定投计划成功，用户ID：{}, 定投计划数量：{}", userId, plans.size());
            log.debug("定投计划详情：{}", plans);
            return plans;
        } catch (Exception e) {
            log.error("获取用户定投计划失败，用户ID：{}, 错误原因：{}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void executeFixedInvestments() {
        log.info("开始执行定投计划");
        
        try {
            // 获取所有需要执行的定投计划
            log.debug("步骤1：获取所有需要执行的定投计划");
            List<FixedInvestment> plans = fixedInvestmentMapper.selectByStatusAndNextExecutionDateBefore(
                    "ACTIVE", LocalDateTime.now());
            log.info("共获取到{}个需要执行的定投计划", plans.size());
            
            for (FixedInvestment plan : plans) {
                log.info("开始执行定投计划，计划ID：{}, 用户ID：{}, 基金代码：{}, 定投金额：{}", 
                        plan.getId(), plan.getUserId(), plan.getFund().getFundCode(), plan.getAmount());
                
                try {
                    // 执行定投（调用加仓方法）
                    log.debug("执行定投操作，计划ID：{}", plan.getId());
                    buyFund(plan.getUserId(), plan.getFund().getFundCode(), plan.getAmount());
                    
                    // 更新下次执行时间
                    log.debug("更新下次执行时间，计划ID：{}", plan.getId());
                    plan.setNextExecutionDate(calculateNextExecutionDate(plan.getNextExecutionDate(), plan.getFrequency()));
                    fixedInvestmentMapper.update(plan);
                    
                    log.info("定投计划执行成功：ID{}, 下次执行时间：{}", plan.getId(), plan.getNextExecutionDate());
                } catch (Exception e) {
                    log.error("定投计划执行失败：ID{}，用户ID：{}, 基金代码：{}, 错误原因：{}", 
                            plan.getId(), plan.getUserId(), plan.getFund().getFundCode(), e.getMessage(), e);
                }
            }
            
            log.info("所有定投计划执行完成");
        } catch (Exception e) {
            log.error("执行定投计划过程中发生全局错误，错误原因：{}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 更新用户基金持仓
     */
    private void updateUserFundHolding(Long userId, Fund fund, Double sharesChange, Double price, Double amountChange) {
        log.debug("开始更新用户基金持仓，用户ID：{}, 基金代码：{}, 份额变化：{}, 交易价格：{}", 
                userId, fund.getFundCode(), sharesChange, price);
        
        Optional<UserFund> userFundOptional = userFundMapper.selectByUserIdAndFundId(userId, fund.getId());
        
        if (userFundOptional.isPresent()) {
            UserFund userFund = userFundOptional.get();
            double oldShares = userFund.getTotalShares();
            double newShares = oldShares + sharesChange;
            
            if (newShares <= 0) {
                // 全部卖出，删除持仓记录
                log.debug("全部卖出，删除持仓记录，持仓ID：{}", userFund.getId());
                userFundMapper.deleteById(userFund.getId());
                log.debug("持仓记录删除成功，持仓ID：{}", userFund.getId());
            } else {
                // 更新持仓记录
                log.debug("更新持仓记录，持仓ID：{}, 旧份额：{}, 新份额：{}", 
                        userFund.getId(), oldShares, newShares);
                double oldTotalCost = userFund.getTotalCost();
                double newTotalCost = oldTotalCost + (sharesChange * price);
                
                userFund.setTotalShares(newShares);
                userFund.setAverageCost(newTotalCost / newShares);
                userFund.setTotalCost(newTotalCost);
                userFund.setCurrentValue(newShares * fund.getLatestNav());
                userFund.setProfitLoss(userFund.getCurrentValue() - userFund.getTotalCost());
                userFund.setProfitLossRatio((userFund.getProfitLoss() / userFund.getTotalCost()) * 100);
                
                userFundMapper.update(userFund);
                log.debug("持仓记录更新成功，持仓ID：{}, 最新市值：{}", 
                        userFund.getId(), userFund.getCurrentValue());
            }
        } else {
            // 首次购买，创建持仓记录
            log.debug("首次购买，创建持仓记录，用户ID：{}, 基金代码：{}", userId, fund.getFundCode());
            UserFund userFund = new UserFund();
            userFund.setUserId(userId);
            userFund.setFund(fund);
            userFund.setTotalShares(sharesChange);
            userFund.setAverageCost(price);
            userFund.setTotalCost(sharesChange * price);
            userFund.setCurrentValue(sharesChange * fund.getLatestNav());
            userFund.setProfitLoss(userFund.getCurrentValue() - userFund.getTotalCost());
            userFund.setProfitLossRatio((userFund.getProfitLoss() / userFund.getTotalCost()) * 100);
            
            userFundMapper.insert(userFund);
            log.debug("持仓记录创建成功，持仓ID：{}, 初始市值：{}", 
                    userFund.getId(), userFund.getCurrentValue());
        }
        
        log.debug("用户基金持仓更新完成，用户ID：{}, 基金代码：{}", userId, fund.getFundCode());
    }

    /**
     * 更新定投计划状态
     */
    private FixedInvestment updateFixedInvestmentStatus(Long id, String status) {
        FixedInvestment plan = fixedInvestmentMapper.selectById(id);
        if (plan == null) {
            throw new RuntimeException("定投计划不存在");
        }
        
        plan.setStatus(status);
        fixedInvestmentMapper.update(plan);
        log.info("定投计划{}状态更新为{}", id, status);
        return plan;
    }

    /**
     * 计算下次执行时间
     */
    private LocalDateTime calculateNextExecutionDate(LocalDateTime current, String frequency) {
        switch (frequency) {
            case "DAILY":
                return current.plusDays(1);
            case "WEEKLY":
                return current.plusWeeks(1);
            case "MONTHLY":
                return current.plusMonths(1);
            default:
                return current.plusDays(1);
        }
    }
}