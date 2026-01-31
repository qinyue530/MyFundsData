<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import FundList from './FundList.vue'
import UserFunds from './UserFunds.vue'

const router = useRouter()
const activeTab = ref('fundList')

// 检查登录状态
const checkLogin = () => {
  const userId = localStorage.getItem('userId')
  return !!userId
}

// 切换标签
const switchTab = (tab) => {
  // 检查是否登录，未登录则跳转登录页面
  if (tab === 'userFunds' && !checkLogin()) {
    router.push('/login')
    return
  }
  activeTab.value = tab
}
</script>

<template>
  <div class="main-content-container">
    <!-- 标签页导航 -->
    <div class="tabs-navigation">
      <button 
        :class="{ active: activeTab === 'fundList' }" 
        @click="switchTab('fundList')"
      >
        基金查询
      </button>
      <button 
        :class="{ active: activeTab === 'userFunds' }" 
        @click="switchTab('userFunds')"
      >
        我的持仓
      </button>
    </div>
    
    <!-- 主要内容 -->
    <main class="content">
      <FundList v-if="activeTab === 'fundList'" />
      <UserFunds v-else-if="activeTab === 'userFunds'" />
    </main>
  </div>
</template>

<style scoped>
.main-content-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
}

.tabs-navigation {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.tabs-navigation button {
  padding: 10px 20px;
  background-color: white;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
  color: #606266;
  transition: all 0.3s;
}

.tabs-navigation button:hover {
  color: #409EFF;
  border-color: #c6e2ff;
  background-color: #ecf5ff;
}

.tabs-navigation button.active {
  color: white;
  background-color: #409EFF;
  border-color: #409EFF;
  font-weight: 600;
}

.content {
  min-height: calc(100vh - 120px);
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>