<template>
  <div class="register-container">
    <div class="register-form">
      <h2>基金管理系统 - 注册</h2>
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label for="username">用户名</label>
          <input
            type="text"
            id="username"
            v-model="registerForm.username"
            placeholder="请输入用户名"
            required
            minlength="4"
            maxlength="20"
          />
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input
            type="password"
            id="password"
            v-model="registerForm.password"
            placeholder="请输入密码"
            required
            minlength="6"
            maxlength="20"
          />
        </div>
        <div class="form-group">
          <label for="confirmPassword">确认密码</label>
          <input
            type="password"
            id="confirmPassword"
            v-model="confirmPassword"
            placeholder="请确认密码"
            required
            minlength="6"
            maxlength="20"
          />
          <div v-if="confirmPassword && registerForm.password !== confirmPassword" class="password-match-error">
            两次输入的密码不一致
          </div>
        </div>
        <div class="form-group">
          <label for="email">邮箱（可选）</label>
          <input
            type="email"
            id="email"
            v-model="registerForm.email"
            placeholder="请输入邮箱"
            maxlength="50"
          />
        </div>
        <div class="form-group">
          <label for="phone">手机号（可选）</label>
          <input
            type="tel"
            id="phone"
            v-model="registerForm.phone"
            placeholder="请输入手机号"
            maxlength="11"
          />
        </div>
        <div class="form-group">
          <label for="nickname">昵称（可选）</label>
          <input
            type="text"
            id="nickname"
            v-model="registerForm.nickname"
            placeholder="请输入昵称"
            maxlength="20"
          />
        </div>
        <div class="form-actions">
          <button type="submit" class="register-btn" :disabled="loading || (confirmPassword && registerForm.password !== confirmPassword)">
            {{ loading ? '注册中...' : '注册' }}
          </button>
          <p class="login-link">
            已有账号？<a @click="switchToLogin">立即登录</a>
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
  name: 'Register',
  data() {
    return {
      registerForm: {
        username: '',
        password: '',
        email: '',
        phone: '',
        nickname: ''
      },
      confirmPassword: '',
      loading: false,
      error: ''
    };
  },
  methods: {
    async handleRegister() {
      // 验证密码是否一致
      if (this.registerForm.password !== this.confirmPassword) {
        this.error = '两次输入的密码不一致';
        return;
      }
      
      this.loading = true;
      this.error = '';
      
      try {
        // 调用注册API
        await userApi.register(this.registerForm);
        
        // 注册成功，跳转到登录页面
        this.$router.push('/login');
      } catch (err) {
        this.error = err.response?.data || '注册失败，请稍后重试';
        console.error('注册失败:', err);
      } finally {
        this.loading = false;
      }
    },
    switchToLogin() {
      // 重置表单
      this.registerForm = { username: '', password: '', email: '', phone: '', nickname: '' };
      this.confirmPassword = '';
      // 切换到登录页面
      this.$router.push('/login');
    },
  }
};
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.register-form {
  background-color: white;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 450px;
}

.register-form h2 {
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

.password-match-error {
  margin-top: 5px;
  color: #F56C6C;
  font-size: 12px;
}

.form-actions {
  margin-top: 30px;
}

.register-btn {
  width: 100%;
  padding: 12px;
  background-color: #67C23A;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.register-btn:hover {
  background-color: #85ce61;
}

.register-btn:disabled {
  background-color: #C0C4CC;
  cursor: not-allowed;
}

.login-link {
  text-align: center;
  margin-top: 15px;
  color: #666;
}

.login-link a {
  color: #409EFF;
  text-decoration: none;
  cursor: pointer;
}

.login-link a:hover {
  text-decoration: underline;
}

.error-message {
  margin-top: 15px;
  color: #F56C6C;
  text-align: center;
}
</style>