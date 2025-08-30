import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User } from '@/types/auth'

export const useAuthStore = defineStore('auth', () => {
  // 状态
  const token = ref<string>('')
  const user = ref<User | null>(null)
  const isAuthenticated = ref<boolean>(false)
  const loading = ref<boolean>(false)

  // 计算属性
  const hasRole = computed(() => (role: string) => {
    return user.value?.roles?.some(r => r.name === role) || false
  })

  const hasPermission = computed(() => (permission: string) => {
    return user.value?.permissions?.some(p => p.name === permission) || false
  })

  // 方法
  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setUser = (newUser: User) => {
    user.value = newUser
    isAuthenticated.value = true
  }

  const login = async (credentials: { username: string; password: string }) => {
    loading.value = true
    try {
      // TODO: 调用登录API
      // const response = await authApi.login(credentials)
      // setToken(response.data.token)
      // setUser(response.data.user)
      return { success: true }
    } catch (error) {
      return { success: false, error }
    } finally {
      loading.value = false
    }
  }

  const logout = () => {
    token.value = ''
    user.value = null
    isAuthenticated.value = false
    localStorage.removeItem('token')
  }

  const checkAuth = async () => {
    const savedToken = localStorage.getItem('token')
    if (savedToken) {
      token.value = savedToken
      try {
        // TODO: 调用获取用户信息API
        // const response = await authApi.getUserInfo()
        // setUser(response.data)
        return true
      } catch (error) {
        logout()
        return false
      }
    }
    return false
  }

  const initAuth = async () => {
    await checkAuth()
  }

  return {
    // 状态
    token,
    user,
    isAuthenticated,
    loading,
    
    // 计算属性
    hasRole,
    hasPermission,
    
    // 方法
    setToken,
    setUser,
    login,
    logout,
    checkAuth,
    initAuth
  }
})
