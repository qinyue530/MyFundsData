<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const currentUser = ref(null)

// 获取当前用户信息
onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    currentUser.value = JSON.parse(userStr)
  }
})

// 跳转到登录页面
const goToLogin = () => {
  router.push('/login')
}

// 跳转到注册页面
const goToRegister = () => {
  router.push('/register')
}

// 跳转到首页
const goToHome = () => {
  router.push('/')
}

// 登出功能
const logout = () => {
  // 清除本地存储
  localStorage.removeItem('user')
  localStorage.removeItem('userId')
  // 跳转到首页
  router.push('/')
}
</script>

<template>
  <div class="app-layout">
    <!-- 顶部导航 -->
    <header class="app-header">
      <div class="header-left">
        <h1 @click="goToHome" class="home-title">我的基金</h1>
      </div>
      <div class="header-right">
        <!-- 未登录状态 -->
        <div v-if="!currentUser" class="auth-buttons">
          <button class="login-btn" @click="goToLogin">登录</button>
          <button class="register-btn" @click="goToRegister">注册</button>
        </div>
        <!-- 已登录状态 -->
        <div v-else class="user-info">
          <span class="welcome-text">欢迎，{{ currentUser.nickname || currentUser.username }}</span>
          <button class="logout-btn" @click="logout">退出登录</button>
        </div>
      </div>
    </header>
    
    <!-- 路由出口 -->
    <main class="app-main">
      <router-view />
    </main>
  </div>
</template>

<style>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  background-color: #f0f2f5;
  color: #303133;
  line-height: 1.6;
}
</style>

<style scoped>
.app-layout {
  min-height: 100vh;
}

.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background-color: white;
  border-bottom: 1px solid #e4e7ed;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.header-left h1 {
  color: #409EFF;
  font-size: 24px;
  margin: 0;
}

.home-title {
  cursor: pointer;
  transition: opacity 0.3s;
}

.home-title:hover {
  opacity: 0.8;
}

/* 登录注册按钮样式 */
.auth-buttons {
  display: flex;
  gap: 10px;
}

.login-btn, .register-btn {
  padding: 8px 16px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s;
}

.login-btn {
  background-color: white;
  color: #409EFF;
  border-color: #409EFF;
}

.login-btn:hover {
  background-color: #ecf5ff;
}

.register-btn {
  background-color: #409EFF;
  color: white;
  border-color: #409EFF;
}

.register-btn:hover {
  background-color: #66B1FF;
}

/* 用户信息样式 */
.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.welcome-text {
  color: #606266;
  font-size: 14px;
}

.logout-btn {
  padding: 6px 12px;
  background-color: #F56C6C;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.logout-btn:hover {
  background-color: #f78989;
}

/* 主要内容区域 */
.app-main {
  min-height: calc(100vh - 80px);
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
