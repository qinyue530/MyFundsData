<template>
  <div class="login-container">
    <div class="login-form">
      <h2>基金管理系统 - 登录</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">用户名</label>
          <input
            type="text"
            id="username"
            v-model="loginForm.username"
            placeholder="请输入用户名"
            required
          />
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input
            type="password"
            id="password"
            v-model="loginForm.password"
            placeholder="请输入密码"
            required
          />
        </div>
        <div class="form-actions">
          <button type="submit" class="login-btn" :disabled="loading">
            {{ loading ? '登录中...' : '登录' }}
          </button>
          <p class="register-link">
            还没有账号？<a @click="switchToRegister">立即注册</a>
          </p>
        </div>
      </form>
      <div v-if="error" class="error-message">{{ error }}</div>
    </div>
  </div>
</template>

<script>
import { userApi } from '../services/api';

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loading: false,
      error: ''
    };
  },
  methods: {
    async handleLogin() {
      this.loading = true;
      this.error = '';
      try {
        // 调用登录API
        const user = await userApi.login(this.loginForm);
        
        // 保存用户信息到本地存储
        localStorage.setItem('user', JSON.stringify(user));
        localStorage.setItem('userId', user.id);
        
        // 刷新页面，确保App组件重新获取用户信息
        window.location.reload();
        // 跳转到主页面
        this.$router.push('/');
      } catch (err) {
        this.error = err.message || '登录失败，请检查用户名和密码';
        console.error('登录失败:', err.originalError || err);
      } finally {
        this.loading = false;
      }
    },
    switchToRegister() {
      // 重置表单
      this.loginForm = { username: '', password: '' };
      // 切换到注册页面
      this.$router.push('/register');
    },
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.login-form {
  background-color: white;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

.login-form h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #666;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  transition: border-color 0.3s;
}

.form-group input:focus {
  outline: none;
  border-color: #409EFF;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.form-actions {
  margin-top: 30px;
}

.login-btn {
  width: 100%;
  padding: 12px;
  background-color: #409EFF;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login-btn:hover {
  background-color: #66B1FF;
}

.login-btn:disabled {
  background-color: #C0C4CC;
  cursor: not-allowed;
}

.register-link {
  text-align: center;
  margin-top: 15px;
  color: #666;
}

.register-link a {
  color: #409EFF;
  text-decoration: none;
  cursor: pointer;
}

.register-link a:hover {
  text-decoration: underline;
}

.error-message {
  margin-top: 15px;
  color: #F56C6C;
  text-align: center;
}
</style>