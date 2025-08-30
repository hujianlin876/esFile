import { defineStore } from 'pinia'
import { ref, computed, watch } from 'vue'
import { getThemeSettings, updateThemeSettings, getLanguageSettings, updateLanguageSettings } from '@/api/system/settings'

export const useAppStore = defineStore('app', () => {
  // 状态
  const sidebarCollapsed = ref(localStorage.getItem('sidebarCollapsed') === 'true')
  const theme = ref<'light' | 'dark' | 'auto'>(localStorage.getItem('theme') as 'light' | 'dark' | 'auto' || 'light')
  const primaryColor = ref(localStorage.getItem('primaryColor') || '#409eff')
  const language = ref(localStorage.getItem('language') || 'zh-CN')
  const compactMode = ref(localStorage.getItem('compactMode') === 'true')

  // 计算属性
  const isDark = computed(() => {
    if (theme.value === 'auto') {
      return window.matchMedia('(prefers-color-scheme: dark)').matches
    }
    return theme.value === 'dark'
  })

  // 方法
  const toggleSidebar = () => {
    sidebarCollapsed.value = !sidebarCollapsed.value
    localStorage.setItem('sidebarCollapsed', sidebarCollapsed.value.toString())
  }

  const setSidebarCollapsed = (collapsed: boolean) => {
    sidebarCollapsed.value = collapsed
    localStorage.setItem('sidebarCollapsed', collapsed.toString())
  }

  const setTheme = async (newTheme: 'light' | 'dark' | 'auto') => {
    try {
      theme.value = newTheme
      localStorage.setItem('theme', newTheme)
      
      // 应用主题样式
      applyTheme()
      
      // 同步到后端
      await updateThemeSettings({ theme: newTheme })
    } catch (error) {
      console.error('设置主题失败:', error)
    }
  }

  const setPrimaryColor = async (color: string) => {
    try {
      primaryColor.value = color
      localStorage.setItem('primaryColor', color)
      
      // 应用主题样式
      applyTheme()
      
      // 同步到后端
      await updateThemeSettings({ primaryColor: color })
    } catch (error) {
      console.error('设置主色调失败:', error)
    }
  }

  const setLanguage = async (newLanguage: string) => {
    try {
      language.value = newLanguage
      localStorage.setItem('language', newLanguage)
      
      // 应用语言设置
      applyLanguage()
      
      // 同步到后端
      await updateLanguageSettings({ language: newLanguage })
    } catch (error) {
      console.error('设置语言失败:', error)
    }
  }

  const setCompactMode = async (compact: boolean) => {
    try {
      compactMode.value = compact
      localStorage.setItem('compactMode', compact.toString())
      
      // 应用紧凑模式样式
      applyCompactMode()
      
      // 同步到后端
      await updateThemeSettings({ compactMode: compact })
    } catch (error) {
      console.error('设置紧凑模式失败:', error)
    }
  }

  const applyTheme = () => {
    const root = document.documentElement
    
    // 设置CSS变量
    root.style.setProperty('--el-color-primary', primaryColor.value)
    
    // 应用暗色主题
    if (isDark.value) {
      root.classList.add('dark')
      document.body.classList.add('dark')
    } else {
      root.classList.remove('dark')
      document.body.classList.remove('dark')
    }
  }

  const applyLanguage = () => {
    // 设置HTML lang属性
    document.documentElement.lang = language.value
    
    // 这里可以添加国际化逻辑
    // 例如：i18n.global.locale.value = language.value
  }

  const applyCompactMode = () => {
    const root = document.documentElement
    
    if (compactMode.value) {
      root.classList.add('compact')
    } else {
      root.classList.remove('compact')
    }
  }

  // 监听系统主题变化
  const watchSystemTheme = () => {
    if (theme.value === 'auto') {
      const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)')
      mediaQuery.addEventListener('change', applyTheme)
    }
  }

  // 初始化设置
  const initSettings = async () => {
    try {
      // 从后端获取设置
      const [themeRes, languageRes] = await Promise.all([
        getThemeSettings(),
        getLanguageSettings()
      ])
      
      // 应用后端设置
      if (themeRes.data) {
        theme.value = themeRes.data.theme
        primaryColor.value = themeRes.data.primaryColor
        compactMode.value = themeRes.data.compactMode
        localStorage.setItem('theme', theme.value)
        localStorage.setItem('primaryColor', primaryColor.value)
        localStorage.setItem('compactMode', compactMode.value.toString())
      }
      
      if (languageRes.data) {
        language.value = languageRes.data.language
        localStorage.setItem('language', language.value)
      }
      
      // 应用设置
      applyTheme()
      applyLanguage()
      applyCompactMode()
      watchSystemTheme()
    } catch (error) {
      console.error('初始化设置失败:', error)
      // 使用本地设置作为后备
      applyTheme()
      applyLanguage()
      applyCompactMode()
      watchSystemTheme()
    }
  }

  // 监听主题变化
  watch(isDark, () => {
    applyTheme()
  })

  return {
    // 状态
    sidebarCollapsed,
    theme,
    primaryColor,
    language,
    compactMode,
    
    // 计算属性
    isDark,
    
    // 方法
    toggleSidebar,
    setSidebarCollapsed,
    setTheme,
    setPrimaryColor,
    setLanguage,
    setCompactMode,
    initSettings
  }
})
