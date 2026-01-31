package com.example.myfunds.service;

import com.example.myfunds.entity.Fund;
import com.example.myfunds.entity.FundStock;

import java.util.List;

public interface FundDataService {
    /**
     * 根据基金代码获取基金基本信息
     */
    Fund getFundBasicInfo(String fundCode);

    /**
     * 根据基金代码获取基金持仓股票数据
     */
    List<FundStock> getFundStockHoldings(String fundCode);

    /**
     * 刷新基金数据并保存到数据库
     */
    Fund refreshFundData(String fundCode);

    /**
     * 更新所有基金的最新数据
     */
    void updateAllFundsData();
}