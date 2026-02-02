package com.example.myfunds.service.impl;

import com.example.myfunds.entity.Fund;
import com.example.myfunds.entity.FundStock;
import com.example.myfunds.mapper.FundMapper;
import com.example.myfunds.mapper.FundStockMapper;
import com.example.myfunds.service.FundDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FundDataServiceImpl implements FundDataService {

    @Autowired
    private FundMapper fundMapper;

    @Autowired
    private FundStockMapper fundStockMapper;

    @Override
    public Fund getFundBasicInfo(String fundCode) {
        log.debug("开始获取基金基本信息，基金代码：{}", fundCode);
        Optional<Fund> fundOptional = fundMapper.selectByFundCode(fundCode);
        if (fundOptional.isPresent()) {
            log.debug("从数据库获取基金基本信息成功，基金代码：{}", fundCode);
            return fundOptional.get();
        }
        // 如果数据库中没有，从外部API获取
        log.debug("数据库中未找到基金信息，从外部API获取，基金代码：{}", fundCode);
        return fetchFundDataFromApi(fundCode);
    }

    @Override
    public List<FundStock> getFundStockHoldings(String fundCode) {
        log.debug("开始获取基金持仓股票数据，基金代码：{}", fundCode);
        Optional<Fund> fundOptional = fundMapper.selectByFundCode(fundCode);
        if (fundOptional.isPresent()) {
            List<FundStock> holdings = fundStockMapper.selectByFundId(fundOptional.get().getId());
            log.debug("获取基金持仓股票数据成功，基金代码：{}, 持仓数量：{}", fundCode, holdings.size());
            return holdings;
        }
        log.debug("未找到基金信息，无法获取持仓数据，基金代码：{}", fundCode);
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public Fund refreshFundData(String fundCode) {
        log.info("开始刷新基金数据，基金代码：{}", fundCode);
        
        // 1. 获取基金基本信息
        log.debug("步骤1：获取基金基本信息，基金代码：{}", fundCode);
        Fund fund = fetchFundDataFromApi(fundCode);
        
        // 2. 保存或更新基金基本信息
        log.debug("步骤2：保存或更新基金基本信息，基金代码：{}", fundCode);
        Optional<Fund> existingFund = fundMapper.selectByFundCode(fundCode);
        Fund savedFund;
        if (existingFund.isPresent()) {
            fund.setId(existingFund.get().getId());
            fundMapper.update(fund);
            savedFund = fund;
            log.debug("更新基金基本信息成功，基金代码：{}", fundCode);
        } else {
            fundMapper.insert(fund);
            savedFund = fund;
            log.debug("插入基金基本信息成功，基金代码：{}", fundCode);
        }
        
        // 3. 获取并保存基金持仓数据
        log.debug("步骤3：获取并保存基金持仓数据，基金代码：{}", fundCode);
        List<FundStock> stockHoldings = fetchFundStockHoldingsFromApi(fundCode);
        
        // 4. 删除旧的持仓数据
        log.debug("步骤4：删除旧的持仓数据，基金ID：{}", savedFund.getId());
        fundStockMapper.deleteByFundId(savedFund.getId());
        
        // 5. 保存新的持仓数据
        log.debug("步骤5：保存新的持仓数据，基金ID：{}, 持仓数量：{}", 
                savedFund.getId(), stockHoldings.size());
        for (FundStock stock : stockHoldings) {
            stock.setFund(savedFund);
            fundStockMapper.insert(stock);
        }
        
        // 6. 计算并更新基金估值
        log.debug("步骤6：计算并更新基金估值，基金ID：{}", savedFund.getId());
        calculateFundEstimate(savedFund, stockHoldings);
        
        log.info("基金数据刷新完成，基金代码：{}, 基金名称：{}", 
                fundCode, savedFund.getFundName());
        return savedFund;
    }
    
    /**
     * 基于持仓股票计算基金估值
     * @param fund 基金信息
     * @param stockHoldings 持仓股票列表
     */
    private void calculateFundEstimate(Fund fund, List<FundStock> stockHoldings) {
        log.debug("开始计算基金估值，基金ID：{}", fund.getId());
        
        // 计算预估涨跌幅：根据各股票的持仓比例和涨跌幅加权平均
        double estimatedGrowth = 0.0;
        for (FundStock stock : stockHoldings) {
            estimatedGrowth += (stock.getHoldingRatio() / 100) * stock.getDayGrowth();
        }
        log.debug("计算完成预估涨跌幅：{}%，基金ID：{}", estimatedGrowth, fund.getId());
        
        // 计算预估净值
        double estimatedNav = fund.getLatestNav() * (1 + estimatedGrowth / 100);
        log.debug("计算完成预估净值：{}，基金ID：{}", estimatedNav, fund.getId());
        
        // 计算预估收益（假设以当前最新净值买入10000元的收益）
        double investmentAmount = 10000.0;
        double shares = investmentAmount / fund.getLatestNav();
        double estimatedProfit = shares * (estimatedNav - fund.getLatestNav());
        log.debug("计算完成预估收益：{}元（投资10000元），基金ID：{}", estimatedProfit, fund.getId());
        
        // 更新基金估值信息
        fund.setEstimatedDayGrowth(estimatedGrowth);
        fund.setEstimatedNav(estimatedNav);
        fund.setEstimatedProfit(estimatedProfit);
        
        // 保存到数据库
        fundMapper.update(fund);
        log.debug("基金估值计算完成并保存，基金ID：{}", fund.getId());
    }

    @Override
    @Transactional
    public void updateAllFundsData() {
        List<Fund> allFunds = fundMapper.selectAll();
        for (Fund fund : allFunds) {
            refreshFundData(fund.getFundCode());
        }
        log.info("所有基金数据更新完成");
    }

    /**
     * 从外部API获取基金基本信息
     * 实际项目中替换为真实API调用
     */
    private Fund fetchFundDataFromApi(String fundCode) {
        // 模拟API调用，返回基金数据
        Fund fund = new Fund();
        fund.setFundCode(fundCode);
        fund.setFundName("模拟基金 - " + fundCode);
        fund.setFundType("股票型");
        fund.setManager("张经理");
        fund.setEstablishDate(LocalDateTime.now().minusYears(5));
        fund.setLatestNav(1.5 + Math.random() * 1.0);
        fund.setDayGrowth((Math.random() - 0.5) * 10);
        fund.setWeekGrowth((Math.random() - 0.5) * 15);
        fund.setMonthGrowth((Math.random() - 0.5) * 30);
        fund.setQuarterGrowth((Math.random() - 0.5) * 50);
        fund.setYearGrowth((Math.random() - 0.5) * 100);
        return fund;
    }

    /**
     * 从外部API获取基金持仓股票数据
     * 实际项目中替换为真实API调用
     */
    private List<FundStock> fetchFundStockHoldingsFromApi(String fundCode) {
        // 模拟API调用，返回基金持仓数据
        List<FundStock> stockHoldings = new ArrayList<>();
        
        // 模拟10只股票持仓
        for (int i = 1; i <= 10; i++) {
            FundStock stock = new FundStock();
            stock.setStockCode("60000" + i);
            stock.setStockName("模拟股票" + i);
            stock.setHoldingRatio(10.0 - i * 0.5);
            stock.setStockPrice(10 + Math.random() * 90);
            stock.setDayGrowth((Math.random() - 0.5) * 10);
            stock.setHoldingValue(10000000.0 * (11 - i));
            stock.setReportDate(LocalDateTime.now().minusMonths(1));
            stockHoldings.add(stock);
        }
        
        return stockHoldings;
    }
}