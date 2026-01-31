<template>
  <div class="user-funds-container">
    <h2>我的基金持仓</h2>
    
    <div class="user-info">
      <div class="info-item">
        <span class="label">用户ID：</span>
        <span class="value">{{ userId }}</span>
      </div>
      <button @click="fetchUserFunds">刷新持仓</button>
    </div>
    
    <!-- 手动添加持仓 -->
    <div class="add-holding-section">
      <h3>添加基金持仓</h3>
      <div class="form-row">
        <input
          type="text"
          v-model="addForm.fundCode"
          placeholder="基金代码"
        />
        <input
          type="number"
          v-model.number="addForm.shares"
          placeholder="持有份额"
          step="0.001"
        />
        <input
          type="number"
          v-model.number="addForm.costPrice"
          placeholder="成本价"
          step="0.001"
        />
        <button @click="addFundHolding">添加</button>
      </div>
    </div>
    
    <!-- 我的持仓列表 -->
    <div class="holdings-section">
      <h3>持仓列表</h3>
      <table class="holdings-table">
        <thead>
          <tr>
            <th>基金名称</th>
            <th>基金代码</th>
            <th>持有份额</th>
            <th>成本价</th>
            <th>最新净值</th>
            <th>预估涨跌幅</th>
            <th>预估收益</th>
            <th>总市值</th>
            <th>盈亏</th>
            <th>盈亏率</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="userFund in userFunds" :key="userFund.id">
            <td>{{ userFund.fund.fundName }}</td>
            <td>{{ userFund.fund.fundCode }}</td>
            <td>{{ userFund.totalShares.toFixed(3) }}</td>
            <td>{{ userFund.averageCost.toFixed(4) }}</td>
            <td>{{ userFund.fund.latestNav.toFixed(4) }}</td>
            <!-- 预估涨跌幅 -->
            <td :class="{ 
              'positive': userFund.fund.estimatedDayGrowth > 0, 
              'negative': userFund.fund.estimatedDayGrowth < 0 
            }">
              {{ (userFund.fund.estimatedDayGrowth || 0).toFixed(2) }}%
            </td>
            <!-- 预估收益：根据持有份额和预估净值变化计算 -->
            <td :class="{ 
              'positive': calculatedEstimatedProfit(userFund) > 0, 
              'negative': calculatedEstimatedProfit(userFund) < 0 
            }">
              {{ calculatedEstimatedProfit(userFund).toFixed(2) }}
            </td>
            <td>{{ userFund.currentValue.toFixed(2) }}</td>
            <td :class="{ 'positive': userFund.profitLoss > 0, 'negative': userFund.profitLoss < 0 }">
              {{ userFund.profitLoss.toFixed(2) }}
            </td>
            <td :class="{ 'positive': userFund.profitLossRatio > 0, 'negative': userFund.profitLossRatio < 0 }">
              {{ userFund.profitLossRatio.toFixed(2) }}%
            </td>
            <td class="action-buttons">
              <button @click="showBuyModal(userFund)" class="buy-btn">加仓</button>
              <button @click="showSellModal(userFund)" class="sell-btn">减仓</button>
              <button @click="showFixedInvestmentModal(userFund)" class="fixed-btn">定投</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    
    <!-- 交易记录 -->
    <div class="transactions-section">
      <h3>交易记录</h3>
      <table class="transactions-table">
        <thead>
          <tr>
            <th>基金名称</th>
            <th>交易类型</th>
            <th>交易金额</th>
            <th>交易份额</th>
            <th>交易价格</th>
            <th>手续费</th>
            <th>交易时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="transaction in transactions" :key="transaction.id">
            <td>{{ transaction.fund.fundName }}</td>
            <td :class="{ 'buy-type': transaction.transactionType === 'BUY', 'sell-type': transaction.transactionType === 'SELL' }">
              {{ transaction.transactionType === 'BUY' ? '买入' : transaction.transactionType === 'SELL' ? '卖出' : '定投' }}
            </td>
            <td>{{ transaction.transactionAmount.toFixed(2) }}</td>
            <td>{{ transaction.transactionShares.toFixed(3) }}</td>
            <td>{{ transaction.transactionPrice.toFixed(4) }}</td>
            <td>{{ transaction.fee.toFixed(2) }}</td>
            <td>{{ formatDate(transaction.transactionTime) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    
    <!-- 定投计划 -->
    <div class="fixed-investments-section">
      <h3>定投计划</h3>
      <table class="fixed-investments-table">
        <thead>
          <tr>
            <th>基金名称</th>
            <th>定投金额</th>
            <th>定投频率</th>
            <th>下次执行</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="plan in fixedInvestments" :key="plan.id">
            <td>{{ plan.fund.fundName }}</td>
            <td>{{ plan.amount.toFixed(2) }}</td>
            <td>{{ formatFrequency(plan.frequency) }}</td>
            <td>{{ formatDate(plan.nextExecutionDate) }}</td>
            <td :class="{ 
              'active': plan.status === 'ACTIVE', 
              'paused': plan.status === 'PAUSED', 
              'stopped': plan.status === 'STOPPED' 
            }">
              {{ formatStatus(plan.status) }}
            </td>
            <td class="action-buttons">
              <button 
                v-if="plan.status === 'ACTIVE'" 
                @click="pauseFixedInvestment(plan)" 
                class="pause-btn"
              >
                暂停
              </button>
              <button 
                v-else-if="plan.status === 'PAUSED'" 
                @click="resumeFixedInvestment(plan)" 
                class="resume-btn"
              >
                恢复
              </button>
              <button 
                @click="stopFixedInvestment(plan)" 
                class="stop-btn"
              >
                停止
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    
    <!-- 加仓模态框 -->
    <div v-if="showBuyModalVisible" class="modal-overlay">
      <div class="modal">
        <div class="modal-header">
          <h3>加仓 - {{ buyModalData.fund?.fundName }}</h3>
          <button @click="showBuyModalVisible = false" class="close-btn">×</button>
        </div>
        <div class="modal-body">
          <div class="form-row">
            <span class="label">当前净值：</span>
            <span class="value">{{ buyModalData.fund?.latestNav.toFixed(4) }}</span>
          </div>
          <div class="form-row">
            <input
              type="number"
              v-model.number="buyModalData.amount"
              placeholder="加仓金额"
              step="0.01"
            />
            <button @click="buyFund" class="buy-btn">确认加仓</button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 减仓模态框 -->
    <div v-if="showSellModalVisible" class="modal-overlay">
      <div class="modal">
        <div class="modal-header">
          <h3>减仓 - {{ sellModalData.fund?.fundName }}</h3>
          <button @click="showSellModalVisible = false" class="close-btn">×</button>
        </div>
        <div class="modal-body">
          <div class="form-row">
            <span class="label">当前净值：</span>
            <span class="value">{{ sellModalData.fund?.latestNav.toFixed(4) }}</span>
          </div>
          <div class="form-row">
            <span class="label">可用份额：</span>
            <span class="value">{{ sellModalData.totalShares.toFixed(3) }}</span>
          </div>
          <div class="form-row">
            <input
              type="number"
              v-model.number="sellModalData.shares"
              placeholder="减仓份额"
              step="0.001"
            />
            <button @click="sellFund" class="sell-btn">确认减仓</button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 定投模态框 -->
    <div v-if="showFixedInvestmentModalVisible" class="modal-overlay">
      <div class="modal">
        <div class="modal-header">
          <h3>设置定投 - {{ fixedInvestmentModalData.fund?.fundName }}</h3>
          <button @click="showFixedInvestmentModalVisible = false" class="close-btn">×</button>
        </div>
        <div class="modal-body">
          <div class="form-row">
            <input
              type="number"
              v-model.number="fixedInvestmentModalData.amount"
              placeholder="定投金额"
              step="0.01"
            />
            <select v-model="fixedInvestmentModalData.frequency">
              <option value="DAILY">每日</option>
              <option value="WEEKLY">每周</option>
              <option value="MONTHLY">每月</option>
            </select>
            <button @click="setFixedInvestment" class="fixed-btn">确认设置</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { tradeApi } from '../services/api';

export default {
  name: 'UserFunds',
  data() {
    return {
      userId: localStorage.getItem('userId'), // 从本地存储获取用户ID
      userFunds: [],
      transactions: [],
      fixedInvestments: [],
      
      // 添加持仓表单
      addForm: {
        fundCode: '',
        shares: 0,
        costPrice: 0
      },
      
      // 加仓模态框数据
      showBuyModalVisible: false,
      buyModalData: {
        fund: null,
        amount: 0
      },
      
      // 减仓模态框数据
      showSellModalVisible: false,
      sellModalData: {
        fund: null,
        totalShares: 0,
        shares: 0
      },
      
      // 定投模态框数据
      showFixedInvestmentModalVisible: false,
      fixedInvestmentModalData: {
        fund: null,
        amount: 0,
        frequency: 'WEEKLY'
      }
    };
  },
  mounted() {
    this.fetchUserFunds();
    this.fetchTransactions();
    this.fetchFixedInvestments();
  },
  methods: {
    async fetchUserFunds() {
      try {
        this.userFunds = await tradeApi.getUserFunds(this.userId);
      } catch (err) {
        console.error('获取用户持仓失败', err);
      }
    },
    
    async fetchTransactions() {
      try {
        this.transactions = await tradeApi.getUserTransactions(this.userId);
      } catch (err) {
        console.error('获取交易记录失败', err);
      }
    },
    
    async fetchFixedInvestments() {
      try {
        this.fixedInvestments = await tradeApi.getUserFixedInvestments(this.userId);
      } catch (err) {
        console.error('获取定投计划失败', err);
      }
    },
    
    async addFundHolding() {
      try {
        await tradeApi.addFundHolding({
          userId: this.userId,
          fundCode: this.addForm.fundCode,
          shares: this.addForm.shares,
          costPrice: this.addForm.costPrice
        });
        // 重置表单
        this.addForm = { fundCode: '', shares: 0, costPrice: 0 };
        // 刷新数据
        this.fetchUserFunds();
      } catch (err) {
        console.error('添加持仓失败', err);
      }
    },
    
    showBuyModal(userFund) {
      this.buyModalData = {
        fund: userFund.fund,
        amount: 0
      };
      this.showBuyModalVisible = true;
    },
    
    async buyFund() {
      try {
        await tradeApi.buyFund({
          userId: this.userId,
          fundCode: this.buyModalData.fund.fundCode,
          amount: this.buyModalData.amount
        });
        // 关闭模态框
        this.showBuyModalVisible = false;
        // 刷新数据
        this.fetchUserFunds();
        this.fetchTransactions();
      } catch (err) {
        console.error('加仓失败', err);
      }
    },
    
    showSellModal(userFund) {
      this.sellModalData = {
        fund: userFund.fund,
        totalShares: userFund.totalShares,
        shares: 0
      };
      this.showSellModalVisible = true;
    },
    
    async sellFund() {
      try {
        await tradeApi.sellFund({
          userId: this.userId,
          fundCode: this.sellModalData.fund.fundCode,
          shares: this.sellModalData.shares
        });
        // 关闭模态框
        this.showSellModalVisible = false;
        // 刷新数据
        this.fetchUserFunds();
        this.fetchTransactions();
      } catch (err) {
        console.error('减仓失败', err);
      }
    },
    
    showFixedInvestmentModal(userFund) {
      this.fixedInvestmentModalData = {
        fund: userFund.fund,
        amount: 0,
        frequency: 'WEEKLY'
      };
      this.showFixedInvestmentModalVisible = true;
    },
    
    async setFixedInvestment() {
      try {
        await tradeApi.setFixedInvestment({
          userId: this.userId,
          fundCode: this.fixedInvestmentModalData.fund.fundCode,
          amount: this.fixedInvestmentModalData.amount,
          frequency: this.fixedInvestmentModalData.frequency
        });
        // 关闭模态框
        this.showFixedInvestmentModalVisible = false;
        // 刷新数据
        this.fetchFixedInvestments();
      } catch (err) {
        console.error('设置定投失败', err);
      }
    },
    
    async pauseFixedInvestment(plan) {
      try {
        await tradeApi.pauseFixedInvestment(plan.id);
        this.fetchFixedInvestments();
      } catch (err) {
        console.error('暂停定投失败', err);
      }
    },
    
    async resumeFixedInvestment(plan) {
      try {
        await tradeApi.resumeFixedInvestment(plan.id);
        this.fetchFixedInvestments();
      } catch (err) {
        console.error('恢复定投失败', err);
      }
    },
    
    async stopFixedInvestment(plan) {
      try {
        await tradeApi.stopFixedInvestment(plan.id);
        this.fetchFixedInvestments();
      } catch (err) {
        console.error('停止定投失败', err);
      }
    },
    
    formatDate(dateStr) {
      const date = new Date(dateStr);
      return date.toLocaleString();
    },
    
    formatFrequency(frequency) {
      const map = {
        'DAILY': '每日',
        'WEEKLY': '每周',
        'MONTHLY': '每月'
      };
      return map[frequency] || frequency;
    },
    
    formatStatus(status) {
      const map = {
        'ACTIVE': '运行中',
        'PAUSED': '已暂停',
        'STOPPED': '已停止'
      };
      return map[status] || status;
    },
    
    // 计算用户持仓的预估收益
    calculatedEstimatedProfit(userFund) {
      if (!userFund.fund.estimatedDayGrowth || !userFund.totalShares) {
        return 0;
      }
      
      // 计算预估净值变化
      const navChange = userFund.fund.latestNav * (userFund.fund.estimatedDayGrowth / 100);
      // 根据持有份额计算预估收益
      return userFund.totalShares * navChange;
    }
  }
};
</script>

<style scoped>
.user-funds-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0;
}

.user-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.user-info button {
  padding: 8px 16px;
  background-color: #409EFF;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.user-info button:hover {
  background-color: #66B1FF;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.label {
  font-weight: bold;
  color: #666;
}

.value {
  color: #333;
}

/* 添加持仓区域 */
.add-holding-section {
  margin-bottom: 30px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.add-holding-section h3 {
  margin-top: 0;
  margin-bottom: 15px;
  color: #333;
}

.form-row {
  display: flex;
  gap: 10px;
  align-items: center;
}

.form-row input {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  flex: 1;
}

.form-row button {
  padding: 8px 16px;
  background-color: #67C23A;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.form-row button:hover {
  background-color: #85ce61;
}

/* 表格样式 */
.holdings-section,
.transactions-section,
.fixed-investments-section {
  margin-bottom: 30px;
}

h3 {
  margin-top: 0;
  margin-bottom: 15px;
  color: #333;
}

.holdings-table,
.transactions-table,
.fixed-investments-table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.holdings-table th,
.transactions-table th,
.fixed-investments-table th {
  background-color: #f5f7fa;
  padding: 12px;
  text-align: left;
  font-weight: bold;
  color: #333;
  border-bottom: 1px solid #eee;
}

.holdings-table td,
.transactions-table td,
.fixed-investments-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 5px;
}

.action-buttons button {
  padding: 5px 10px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  font-size: 12px;
}

.buy-btn {
  background-color: #67C23A;
  color: white;
}

.buy-btn:hover {
  background-color: #85ce61;
}

.sell-btn {
  background-color: #F56C6C;
  color: white;
}

.sell-btn:hover {
  background-color: #f78989;
}

.fixed-btn {
  background-color: #E6A23C;
  color: white;
}

.fixed-btn:hover {
  background-color: #ebb563;
}

.pause-btn {
  background-color: #E6A23C;
  color: white;
}

.pause-btn:hover {
  background-color: #ebb563;
}

.resume-btn {
  background-color: #67C23A;
  color: white;
}

.resume-btn:hover {
  background-color: #85ce61;
}

.stop-btn {
  background-color: #F56C6C;
  color: white;
}

.stop-btn:hover {
  background-color: #f78989;
}

/* 涨跌颜色样式：涨红跌绿 */
.positive {
  color: #F56C6C;
}

.negative {
  color: #67C23A;
}

.buy-type {
  color: #67C23A;
  font-weight: bold;
}

.sell-type {
  color: #F56C6C;
  font-weight: bold;
}

.active {
  color: #67C23A;
  font-weight: bold;
}

.paused {
  color: #E6A23C;
  font-weight: bold;
}

.stopped {
  color: #909399;
  font-weight: bold;
}

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  width: 500px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.modal-header h3 {
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #909399;
}

.close-btn:hover {
  color: #333;
}
</style>