package com.example.myfunds.controller;

import com.example.myfunds.entity.FixedInvestment;
import com.example.myfunds.entity.FundTransaction;
import com.example.myfunds.entity.UserFund;
import com.example.myfunds.service.FundTradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 基金交易控制器
 * 提供基金持仓管理、交易操作、定投计划等API接口
 */
@RestController
@RequestMapping("/api/trade")
@Slf4j
public class FundTradeController {

    @Autowired
    private FundTradeService fundTradeService;

    /**
     * 手动添加基金持仓
     * @param userId 用户ID
     * @param fundCode 基金代码
     * @param shares 持有份额
     * @param costPrice 成本价
     * @return 用户持仓信息
     */
    @PostMapping("/add-holding")
    public ResponseEntity<UserFund> addFundHolding(
            @RequestParam Long userId,
            @RequestParam String fundCode,
            @RequestParam Double shares,
            @RequestParam Double costPrice) {
        log.info("开始手动添加基金持仓，用户ID：{}, 基金代码：{}, 份额：{}, 成本价：{}", 
                userId, fundCode, shares, costPrice);
        
        try {
            UserFund userFund = fundTradeService.addFundHolding(userId, fundCode, shares, costPrice);
            log.info("手动添加基金持仓成功，用户ID：{}, 基金代码：{}, 持仓ID：{}", 
                    userId, fundCode, userFund.getId());
            return ResponseEntity.ok(userFund);
        } catch (Exception e) {
            log.error("手动添加基金持仓失败，用户ID：{}, 基金代码：{}, 错误原因：{}", 
                    userId, fundCode, e.getMessage());
            throw e;
        }
    }

    /**
     * 加仓操作
     * @param userId 用户ID
     * @param fundCode 基金代码
     * @param amount 加仓金额
     * @return 交易记录
     */
    @PostMapping("/buy")
    public ResponseEntity<FundTransaction> buyFund(
            @RequestParam Long userId,
            @RequestParam String fundCode,
            @RequestParam Double amount) {
        log.info("开始基金加仓操作，用户ID：{}, 基金代码：{}, 加仓金额：{}", 
                userId, fundCode, amount);
        
        try {
            FundTransaction transaction = fundTradeService.buyFund(userId, fundCode, amount);
            log.info("基金加仓操作成功，用户ID：{}, 基金代码：{}, 交易ID：{}, 交易金额：{}", 
                    userId, fundCode, transaction.getId(), transaction.getTransactionAmount());
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            log.error("基金加仓操作失败，用户ID：{}, 基金代码：{}, 错误原因：{}", 
                    userId, fundCode, e.getMessage());
            throw e;
        }
    }

    /**
     * 减仓操作
     * @param userId 用户ID
     * @param fundCode 基金代码
     * @param shares 减仓份额
     * @return 交易记录
     */
    @PostMapping("/sell")
    public ResponseEntity<FundTransaction> sellFund(
            @RequestParam Long userId,
            @RequestParam String fundCode,
            @RequestParam Double shares) {
        log.info("开始基金减仓操作，用户ID：{}, 基金代码：{}, 减仓份额：{}", 
                userId, fundCode, shares);
        
        try {
            FundTransaction transaction = fundTradeService.sellFund(userId, fundCode, shares);
            log.info("基金减仓操作成功，用户ID：{}, 基金代码：{}, 交易ID：{}, 交易金额：{}", 
                    userId, fundCode, transaction.getId(), transaction.getTransactionAmount());
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            log.error("基金减仓操作失败，用户ID：{}, 基金代码：{}, 错误原因：{}", 
                    userId, fundCode, e.getMessage());
            throw e;
        }
    }

    /**
     * 设置定投计划
     * @param userId 用户ID
     * @param fundCode 基金代码
     * @param amount 定投金额
     * @param frequency 定投频率
     * @return 定投计划
     */
    @PostMapping("/fixed-investment/set")
    public ResponseEntity<FixedInvestment> setFixedInvestment(
            @RequestParam Long userId,
            @RequestParam String fundCode,
            @RequestParam Double amount,
            @RequestParam String frequency) {
        log.info("开始设置定投计划，用户ID：{}, 基金代码：{}, 定投金额：{}, 定投频率：{}", 
                userId, fundCode, amount, frequency);
        
        try {
            FixedInvestment plan = fundTradeService.setFixedInvestment(userId, fundCode, amount, frequency);
            log.info("设置定投计划成功，用户ID：{}, 基金代码：{}, 定投计划ID：{}", 
                    userId, fundCode, plan.getId());
            return ResponseEntity.ok(plan);
        } catch (Exception e) {
            log.error("设置定投计划失败，用户ID：{}, 基金代码：{}, 错误原因：{}", 
                    userId, fundCode, e.getMessage());
            throw e;
        }
    }

    /**
     * 暂停定投计划
     * @param id 定投计划ID
     * @return 更新后的定投计划
     */
    @PostMapping("/fixed-investment/pause/{id}")
    public ResponseEntity<FixedInvestment> pauseFixedInvestment(@PathVariable Long id) {
        log.info("开始暂停定投计划，定投计划ID：{}", id);
        
        try {
            FixedInvestment plan = fundTradeService.pauseFixedInvestment(id);
            log.info("暂停定投计划成功，定投计划ID：{}, 用户ID：{}, 新状态：{}", 
                    id, plan.getUserId(), plan.getStatus());
            return ResponseEntity.ok(plan);
        } catch (Exception e) {
            log.error("暂停定投计划失败，定投计划ID：{}, 错误原因：{}", id, e.getMessage());
            throw e;
        }
    }

    /**
     * 恢复定投计划
     * @param id 定投计划ID
     * @return 更新后的定投计划
     */
    @PostMapping("/fixed-investment/resume/{id}")
    public ResponseEntity<FixedInvestment> resumeFixedInvestment(@PathVariable Long id) {
        log.info("开始恢复定投计划，定投计划ID：{}", id);
        
        try {
            FixedInvestment plan = fundTradeService.resumeFixedInvestment(id);
            log.info("恢复定投计划成功，定投计划ID：{}, 用户ID：{}, 新状态：{}", 
                    id, plan.getUserId(), plan.getStatus());
            return ResponseEntity.ok(plan);
        } catch (Exception e) {
            log.error("恢复定投计划失败，定投计划ID：{}, 错误原因：{}", id, e.getMessage());
            throw e;
        }
    }

    /**
     * 停止定投计划
     * @param id 定投计划ID
     * @return 更新后的定投计划
     */
    @PostMapping("/fixed-investment/stop/{id}")
    public ResponseEntity<FixedInvestment> stopFixedInvestment(@PathVariable Long id) {
        log.info("开始停止定投计划，定投计划ID：{}", id);
        
        try {
            FixedInvestment plan = fundTradeService.stopFixedInvestment(id);
            log.info("停止定投计划成功，定投计划ID：{}, 用户ID：{}, 新状态：{}", 
                    id, plan.getUserId(), plan.getStatus());
            return ResponseEntity.ok(plan);
        } catch (Exception e) {
            log.error("停止定投计划失败，定投计划ID：{}, 错误原因：{}", id, e.getMessage());
            throw e;
        }
    }

    /**
     * 获取用户基金持仓列表
     * @param userId 用户ID
     * @return 用户持仓列表
     */
    @GetMapping("/user-funds/{userId}")
    public ResponseEntity<List<UserFund>> getUserFunds(@PathVariable Long userId) {
        log.info("开始获取用户基金持仓列表，用户ID：{}", userId);
        
        try {
            List<UserFund> userFunds = fundTradeService.getUserFunds(userId);
            log.info("获取用户基金持仓列表成功，用户ID：{}, 持仓数量：{}", userId, userFunds.size());
            return ResponseEntity.ok(userFunds);
        } catch (Exception e) {
            log.error("获取用户基金持仓列表失败，用户ID：{}, 错误原因：{}", userId, e.getMessage());
            throw e;
        }
    }

    /**
     * 获取用户交易记录
     * @param userId 用户ID
     * @return 用户交易记录列表
     */
    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<FundTransaction>> getUserTransactions(@PathVariable Long userId) {
        log.info("开始获取用户交易记录，用户ID：{}", userId);
        
        try {
            List<FundTransaction> transactions = fundTradeService.getUserTransactions(userId);
            log.info("获取用户交易记录成功，用户ID：{}, 交易记录数量：{}", userId, transactions.size());
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            log.error("获取用户交易记录失败，用户ID：{}, 错误原因：{}", userId, e.getMessage());
            throw e;
        }
    }

    /**
     * 获取用户定投计划
     * @param userId 用户ID
     * @return 用户定投计划列表
     */
    @GetMapping("/fixed-investments/{userId}")
    public ResponseEntity<List<FixedInvestment>> getUserFixedInvestments(@PathVariable Long userId) {
        log.info("开始获取用户定投计划，用户ID：{}", userId);
        
        try {
            List<FixedInvestment> plans = fundTradeService.getUserFixedInvestments(userId);
            log.info("获取用户定投计划成功，用户ID：{}, 定投计划数量：{}", userId, plans.size());
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            log.error("获取用户定投计划失败，用户ID：{}, 错误原因：{}", userId, e.getMessage());
            throw e;
        }
    }

    /**
     * 执行定投计划
     * @return 执行结果
     */
    @PostMapping("/fixed-investment/execute")
    public ResponseEntity<String> executeFixedInvestments() {
        log.info("开始执行定投计划");
        
        try {
            fundTradeService.executeFixedInvestments();
            log.info("定投计划执行完成");
            return ResponseEntity.ok("定投计划执行完成");
        } catch (Exception e) {
            log.error("执行定投计划失败，错误原因：{}", e.getMessage());
            throw e;
        }
    }
}