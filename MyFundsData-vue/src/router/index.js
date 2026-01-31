import { createRouter, createWebHistory } from 'vue-router';

// 导入组件
import App from '../App.vue';
import MainContent from '../components/MainContent.vue';
import Login from '../components/Login.vue';
import Register from '../components/Register.vue';

// 路由配置
const routes = [
  {
    path: '/',
    name: 'MainContent',
    component: MainContent,
    // 移除强制登录要求，允许未登录用户访问
    meta: {
      requiresAuth: false
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  }
];

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes
});

// 导航守卫：检查用户是否已登录（只对特定路由生效）
router.beforeEach((to, from, next) => {
  // 检查路由是否需要认证
  if (to.matched.some(record => record.meta.requiresAuth)) {
    // 检查是否有用户信息
    const userId = localStorage.getItem('userId');
    if (!userId) {
      // 未登录，跳转到登录页面
      next({ name: 'Login' });
    } else {
      // 已登录，允许访问
      next();
    }
  } else {
    // 不需要认证的路由，直接放行
    next();
  }
});

export default router;