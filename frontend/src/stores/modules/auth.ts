import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, getCurrentUser as getCurrentUserApi, logout as logoutApi } from '@/api/auth/auth'
import type { User, LoginRequest, LoginResponse } from '@/api/types/auth'

export const useAuthStore = defineStore('auth', () => {
  // 状态
  const user = ref<User | null>(null)
  const token = ref<string | null>(localStorage.getItem('token'))
  const refreshToken = ref<string | null>(localStorage.getItem('refreshToken'))
  const loading = ref(false)

  // 计算属性
  const isAuthenticated = computed(() => !!token.value && !!user.value)
  const userRoles = computed(() => user.value?.roles.map(role => role.code) || [])
  const userPermissions = computed(() => user.value?.permissions.map(perm => perm.code) || [])

  // 方法
  const login = async (credentials: LoginRequest): Promise<boolean> => {
    loading.value = true
    try {
      const response = await loginApi(credentials)
      const { token: newToken, refreshToken: newRefreshToken, user: userData } = response.data
      
      // 保存认证信息
      token.value = newToken
      refreshToken.value = newRefreshToken
      user.value = userData
      
      // 保存到localStorage
      localStorage.setItem('token', newToken)
      localStorage.setItem('refreshToken', newRefreshToken)
      
      return true
    } catch (error) {
      console.error('登录失败:', error)
      return false
    } finally {
      loading.value = false
    }
  }

  const logout = async (): Promise<void> => {
    try {
      if (token.value) {
        await logoutApi()
      }
    } catch (error) {
      console.error('登出API调用失败:', error)
    } finally {
      // 清除本地状态
      clearAuth()
    }
  }

  const getCurrentUser = async (): Promise<boolean> => {
    if (!token.value) return false
    
    try {
      const response = await getCurrentUserApi()
      user.value = response.data
      return true
    } catch (error) {
      console.error('获取用户信息失败:', error)
      // 如果获取用户信息失败，可能是token过期
      clearAuth()
      return false
    }
  }

  const checkAuth = async (): Promise<boolean> => {
    if (!token.value) return false
    
    try {
      return await getCurrentUser()
    } catch (error) {
      return false
    }
  }

  const clearAuth = (): void => {
    user.value = null
    token.value = null
    refreshToken.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('refreshToken')
  }

  const hasPermission = (permission: string): boolean => {
    return userPermissions.value.includes(permission)
  }

  const hasRole = (role: string): boolean => {
    return userRoles.value.includes(role)
  }

  const hasAnyRole = (roles: string[]): boolean => {
    return roles.some(role => userRoles.value.includes(role))
  }

  const hasAllRoles = (roles: string[]): boolean => {
    return roles.every(role => userRoles.value.includes(role))
  }

  // 初始化时尝试恢复认证状态
  const initAuth = async (): Promise<void> => {
    if (token.value) {
      await checkAuth()
    }
  }

  return {
    // 状态
    user,
    token,
    refreshToken,
    loading,
    
    // 计算属性
    isAuthenticated,
    userRoles,
    userPermissions,
    
    // 方法
    login,
    logout,
    getCurrentUser,
    checkAuth,
    clearAuth,
    hasPermission,
    hasRole,
    hasAnyRole,
    hasAllRoles,
    initAuth
  }
})
