import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { useAuthStore } from '@/stores/modules/auth'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/Login.vue'),
    meta: { requiresAuth: false, title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/Register.vue'),
    meta: { requiresAuth: false, title: '注册' }
  },
  {
    path: '/',
    component: () => import('@/views/layout/MainLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        redirect: '/dashboard'
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/Dashboard.vue'),
        meta: { requiresAuth: true, title: '仪表板' }
      },
      {
        path: 'files',
        name: 'FileManager',
        component: () => import('@/views/file/FileManager.vue'),
        meta: { requiresAuth: true, title: '文件管理' }
      },
      {
        path: 'users',
        name: 'UserManager',
        component: () => import('@/views/system/UserManager.vue'),
        meta: { requiresAuth: true, title: '用户管理' }
      },
      {
        path: 'permissions',
        name: 'PermissionManager',
        component: () => import('@/views/system/PermissionManager.vue'),
        meta: { requiresAuth: true, title: '权限管理' }
      },
      {
        path: 'database',
        name: 'DatabaseManager',
        component: () => import('@/views/database/DatabaseManager.vue'),
        meta: { requiresAuth: true, title: '数据库管理' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/layout/ErrorLayout.vue'),
    meta: { requiresAuth: false, title: '页面不存在' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()
  
  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - ES文件管理系统`
  }
  
  // 检查是否需要认证
  if (to.meta.requiresAuth) {
    if (!authStore.isAuthenticated) {
      // 尝试恢复认证状态
      const isAuth = await authStore.checkAuth()
      if (!isAuth) {
        next('/login')
        return
      }
    }
  }
  
  // 如果已登录且访问登录页，重定向到首页
  if (to.path === '/login' && authStore.isAuthenticated) {
    next('/')
    return
  }
  
  next()
})

export default router
