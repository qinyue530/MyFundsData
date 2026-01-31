package com.example.myfunds.controller;

import com.example.myfunds.entity.Fund;
import com.example.myfunds.entity.FundStock;
import com.example.myfunds.service.FundDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 基金数据控制器
 * 提供基金基本信息、持仓数据、数据刷新等API接口
 */
@RestController
@RequestMapping("/api/fund")
@Slf4j
public class FundDataController {

    @Autowired
    private FundDataService fundDataService;

    /**
     * 获取基金基本信息
     * @param fundCode 基金代码
     * @return 基金基本信息
     */
    @GetMapping("/info/{fundCode}")
    public ResponseEntity<Fund> getFundInfo(@PathVariable String fundCode) {
        log.info("开始获取基金基本信息，基金代码：{}", fundCode);
        
        try {
            Fund fund = fundDataService.getFundBasicInfo(fundCode);
            log.info("获取基金基本信息成功，基金代码：{}, 基金名称：{}, 最新净值：{}", 
                    fundCode, fund.getFundName(), fund.getLatestNav());
            return ResponseEntity.ok(fund);
        } catch (Exception e) {
            log.error("获取基金基本信息失败，基金代码：{}, 错误原因：{}", fundCode, e.getMessage());
            throw e;
        }
    }

    /**
     * 获取基金持仓股票数据
     * @param fundCode 基金代码
     * @return 基金持仓股票列表
     */
    @GetMapping("/holdings/{fundCode}")
    public ResponseEntity<List<FundStock>> getFundHoldings(@PathVariable String fundCode) {
        log.info("开始获取基金持仓股票数据，基金代码：{}", fundCode);
        
        try {
            List<FundStock> holdings = fundDataService.getFundStockHoldings(fundCode);
            log.info("获取基金持仓股票数据成功，基金代码：{}, 持仓股票数量：{}", 
                    fundCode, holdings.size());
            return ResponseEntity.ok(holdings);
        } catch (Exception e) {
            log.error("获取基金持仓股票数据失败，基金代码：{}, 错误原因：{}", fundCode, e.getMessage());
            throw e;
        }
    }

    /**
     * 刷新基金数据
     * @param fundCode 基金代码
     * @return 刷新后的基金信息
     */
    @PostMapping("/refresh/{fundCode}")
    public ResponseEntity<Fund> refreshFundData(@PathVariable String fundCode) {
        log.info("开始刷新基金数据，基金代码：{}", fundCode);
        
        try {
            Fund fund = fundDataService.refreshFundData(fundCode);
            log.info("刷新基金数据成功，基金代码：{}, 基金名称：{}", 
                    fundCode, fund.getFundName());
            return ResponseEntity.ok(fund);
        } catch (Exception e) {
            log.error("刷新基金数据失败，基金代码：{}, 错误原因：{}", fundCode, e.getMessage());
            throw e;
        }
    }

    /**
     * 更新所有基金数据
     * @return 更新结果
     */
    @PostMapping("/update-all")
    public ResponseEntity<String> updateAllFunds() {
        log.info("开始更新所有基金数据");
        
        try {
            fundDataService.updateAllFundsData();
            log.info("所有基金数据更新完成");
            return ResponseEntity.ok("所有基金数据更新完成");
        } catch (Exception e) {
            log.error("更新所有基金数据失败，错误原因：{}", e.getMessage());
            throw e;
        }
    }
}