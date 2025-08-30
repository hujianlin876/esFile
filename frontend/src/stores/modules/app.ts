import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  // 状态
  const sidebarCollapsed = ref<boolean>(false)
  const theme = ref<string>('light')
  const language = ref<string>('zh-CN')
  const loading = ref<boolean>(false)

  // 方法
  const toggleSidebar = () => {
    sidebarCollapsed.value = !sidebarCollapsed.value
  }

  const setSidebarCollapsed = (collapsed: boolean) => {
    sidebarCollapsed.value = collapsed
  }

  const setTheme = (newTheme: string) => {
    theme.value = newTheme
    localStorage.setItem('theme', newTheme)
    // TODO: 应用主题样式
  }

  const setLanguage = (newLanguage: string) => {
    language.value = newLanguage
    localStorage.setItem('language', newLanguage)
    // TODO: 应用语言设置
  }

  const setLoading = (isLoading: boolean) => {
    loading.value = isLoading
  }

  const initApp = () => {
    // 从localStorage恢复设置
    const savedTheme = localStorage.getItem('theme')
    const savedLanguage = localStorage.getItem('language')
    
    if (savedTheme) {
      theme.value = savedTheme
    }
    
    if (savedLanguage) {
      language.value = savedLanguage
    }
  }

  return {
    // 状态
    sidebarCollapsed,
    theme,
    language,
    loading,
    
    // 方法
    toggleSidebar,
    setSidebarCollapsed,
    setTheme,
    setLanguage,
    setLoading,
    initApp
  }
})
