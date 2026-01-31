<template>
  <div class="fund-list-container">
    <h2>基金列表</h2>
    
    <!-- 搜索栏 -->
    <div class="search-section">
      <div class="search-bar">
        <input
          type="text"
          v-model="fundCode"
          placeholder="输入基金代码"
          @keyup.enter="searchFund"
        />
        <button @click="searchFund">搜索</button>
        <button @click="refreshFundData">刷新数据</button>
      </div>
    </div>
    
    <!-- 基金卡片 -->
    <div v-if="fund" class="fund-section">
      <div class="fund-card">
      <h3>{{ fund.fundName }} ({{ fund.fundCode }})</h3>
      <div class="fund-info">
        <div class="info-item">
          <span class="label">基金类型：</span>
          <span class="value">{{ fund.fundType }}</span>
        </div>
        <div class="info-item">
          <span class="label">基金经理：</span>
          <span class="value">{{ fund.manager }}</span>
        </div>
        <div class="info-item">
          <span class="label">最新净值：</span>
          <span class="value">{{ fund.latestNav.toFixed(4) }}</span>
        </div>
        <div class="info-item">
          <span class="label">日涨幅：</span>
          <span class="value" :class="{ 'rise': fund.dayGrowth > 0, 'fall': fund.dayGrowth < 0 }">
            {{ fund.dayGrowth.toFixed(2) }}%
          </span>
        </div>
        <div class="info-item">
          <span class="label">预估涨跌幅：</span>
          <span class="value" :class="{ 'rise': fund.estimatedDayGrowth > 0, 'fall': fund.estimatedDayGrowth < 0 }">
            {{ fund.estimatedDayGrowth.toFixed(2) }}%
          </span>
        </div>
        <div class="info-item">
          <span class="label">预估净值：</span>
          <span class="value">{{ fund.estimatedNav.toFixed(4) }}</span>
        </div>
        <div class="info-item">
          <span class="label">预估收益(1万)：</span>
          <span class="value" :class="{ 'rise': fund.estimatedProfit > 0, 'fall': fund.estimatedProfit < 0 }">
            {{ fund.estimatedProfit.toFixed(2) }}元
          </span>
        </div>
      </div>
      
      <div class="holdings-section">
        <h4>持仓股票</h4>
        <table class="holdings-table">
          <thead>
            <tr>
              <th>股票代码</th>
              <th>股票名称</th>
              <th>持仓比例</th>
              <th>股票价格</th>
              <th>日涨幅</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="stock in holdings" :key="stock.id">
              <td>{{ stock.stockCode }}</td>
              <td>{{ stock.stockName }}</td>
              <td>{{ (stock.holdingRatio || 0).toFixed(2) }}%</td>
              <td>{{ (stock.stockPrice || 0).toFixed(2) }}</td>
              <td :class="{ 'positive': stock.dayGrowth > 0, 'negative': stock.dayGrowth < 0 }">
                {{ (stock.dayGrowth || 0).toFixed(2) }}%
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    </div>
    
    <div v-else-if="loading" class="loading">
      加载中...
    </div>
    
    <div v-else-if="error" class="error">
      {{ error }}
    </div>
  </div>
</template>

<script>
import { fundApi } from '../services/api';

export default {
  name: 'FundList',
  data() {
    return {
      fundCode: '',
      fund: null,
      holdings: [],
      loading: false,
      error: ''
    };
  },
  methods: {
    async searchFund() {
      if (!this.fundCode) {
        this.error = '请输入基金代码';
        return;
      }
      
      this.loading = true;
      this.error = '';
      this.fund = null;
      this.holdings = [];
      
      try {
        // 获取基金基本信息
        const fundInfo = await fundApi.getFundInfo(this.fundCode);
        this.fund = fundInfo;
        
        // 获取基金持仓数据
        const fundHoldings = await fundApi.getFundHoldings(this.fundCode);
        this.holdings = fundHoldings;
      } catch (err) {
        this.error = err.message || '获取基金数据失败';
        console.error('获取基金数据失败:', err.originalError || err);
      } finally {
        this.loading = false;
      }
    },
    
    async refreshFundData() {
      if (!this.fundCode) {
        this.error = '请先搜索基金';
        return;
      }
      
      this.loading = true;
      this.error = '';
      
      try {
        // 刷新基金数据
        const refreshedFund = await fundApi.refreshFundData(this.fundCode);
        this.fund = refreshedFund;
        
        // 重新获取持仓数据
        const fundHoldings = await fundApi.getFundHoldings(this.fundCode);
        this.holdings = fundHoldings;
      } catch (err) {
        this.error = err.message || '刷新基金数据失败';
        console.error('刷新基金数据失败:', err.originalError || err);
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>

<style scoped>
.fund-list-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0;
}

.fund-list-container h2 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #333;
  font-size: 20px;
}

.search-section {
  margin-bottom: 30px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 0;
}

.search-bar input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.search-bar button {
  padding: 10px 20px;
  background-color: #409EFF;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-bar button:hover {
  background-color: #66B1FF;
}

.fund-section {
  margin-bottom: 30px;
}

.fund-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
}

.fund-card h3 {
  margin-top: 0;
  color: #333;
}

.fund-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.info-item {
  display: flex;
  justify-content: space-between;
}

.label {
  font-weight: bold;
  color: #666;
}

.value {
  color: #333;
}

/* 涨跌颜色设置：涨红跌绿 */
.rise, .positive {
  color: #F56C6C;
}

.fall, .negative {
  color: #67C23A;
}

.holdings-section {
  margin-top: 20px;
}

.holdings-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

.holdings-table th,
.holdings-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.holdings-table th {
  background-color: #f5f7fa;
  font-weight: bold;
  color: #333;
}

.loading,
.error {
  text-align: center;
  padding: 20px;
  font-size: 16px;
}

.error {
  color: #F56C6C;
}
</style>