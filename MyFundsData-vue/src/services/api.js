import axios from 'axios';

// 创建Axios实例
const api = axios.create({
  baseURL: 'http://localhost:8080/myfunds/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 添加用户ID到请求Header
    const userId = localStorage.getItem('userId');
    if (userId) {
      config.headers['X-User-Id'] = userId;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response.data;
  },
  error => {
    console.error('API Error:', error);
    
    // 提取友好的错误信息
    let errorMessage = '操作失败，请稍后重试';
    if (error.response) {
      // 服务器返回了错误响应
      const data = error.response.data;
      if (data.message) {
        errorMessage = data.message;
      } else if (data.error) {
        errorMessage = data.error;
      } else if (data.path) {
        // Spring Boot默认错误格式
        errorMessage = data.error || '服务器错误';
      }
    } else if (error.request) {
      // 请求已发送但没有收到响应
      errorMessage = '网络错误，请检查您的网络连接';
    } else {
      // 请求配置时发生错误
      errorMessage = error.message;
    }
    
    // 返回友好的错误信息
    return Promise.reject({ message: errorMessage, originalError: error });
  }
);

// 基金数据相关API
export const fundApi = {
  // 获取基金基本信息
  getFundInfo: (fundCode) => api.get(`/fund/info/${fundCode}`),
  // 获取基金持仓股票
  getFundHoldings: (fundCode) => api.get(`/fund/holdings/${fundCode}`),
  // 刷新基金数据
  refreshFundData: (fundCode) => api.post(`/fund/refresh/${fundCode}`),
  // 更新所有基金数据
  updateAllFunds: () => api.post('/fund/update-all')
};

// 基金交易相关API
export const tradeApi = {
  // 手动添加基金持仓
  addFundHolding: (params) => api.post('/trade/add-holding', null, { params }),
  // 加仓
  buyFund: (params) => api.post('/trade/buy', null, { params }),
  // 减仓
  sellFund: (params) => api.post('/trade/sell', null, { params }),
  // 设置定投计划
  setFixedInvestment: (params) => api.post('/trade/fixed-investment/set', null, { params }),
  // 暂停定投
  pauseFixedInvestment: (id) => api.post(`/trade/fixed-investment/pause/${id}`),
  // 恢复定投
  resumeFixedInvestment: (id) => api.post(`/trade/fixed-investment/resume/${id}`),
  // 停止定投
  stopFixedInvestment: (id) => api.post(`/trade/fixed-investment/stop/${id}`),
  // 获取用户基金持仓
  getUserFunds: (userId) => api.get(`/trade/user-funds/${userId}`),
  // 获取用户交易记录
  getUserTransactions: (userId) => api.get(`/trade/transactions/${userId}`),
  // 获取用户定投计划
  getUserFixedInvestments: (userId) => api.get(`/trade/fixed-investments/${userId}`),
  // 执行定投计划
  executeFixedInvestments: () => api.post('/trade/fixed-investment/execute')
};

// 用户相关API
export const userApi = {
  // 用户注册
  register: (userData) => api.post('/user/register', userData),
  // 用户登录
  login: (credentials) => api.post('/user/login', credentials),
  // 获取用户信息
  getUserInfo: (userId) => api.get(`/user/${userId}`),
  // 更新用户信息
  updateUser: (userId, userData) => api.put(`/user/${userId}`, userData)
};

export default api;