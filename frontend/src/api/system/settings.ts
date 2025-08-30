import request from '@/api/request'

/**
 * 系统配置接口
 */
export interface SystemConfig {
  id: number
  key: string
  value: string
  description?: string
  category: string
  editable: boolean
  createTime: string
  updateTime: string
}

/**
 * 主题设置接口
 */
export interface ThemeSettings {
  primaryColor: string
  theme: 'light' | 'dark' | 'auto'
  sidebarCollapsed: boolean
  compactMode: boolean
}

/**
 * 语言设置接口
 */
export interface LanguageSettings {
  language: string
  timezone: string
  dateFormat: string
  timeFormat: string
}

/**
 * 用户偏好设置接口
 */
export interface UserPreferences {
  theme: ThemeSettings
  language: LanguageSettings
  notifications: {
    email: boolean
    browser: boolean
    mobile: boolean
  }
  privacy: {
    profileVisibility: 'public' | 'private' | 'friends'
    activityVisibility: 'public' | 'private' | 'friends'
  }
}

/**
 * 获取系统配置
 */
export const getSystemConfig = (category?: string) => {
  return request.get<SystemConfig[]>('/system/config', {
    params: { category }
  })
}

/**
 * 更新系统配置
 */
export const updateSystemConfig = (key: string, value: string) => {
  return request.put<SystemConfig>(`/system/config/${key}`, { value })
}

/**
 * 批量更新系统配置
 */
export const batchUpdateSystemConfig = (configs: Array<{ key: string; value: string }>) => {
  return request.put('/system/config/batch', { configs })
}

/**
 * 获取用户偏好设置
 */
export const getUserPreferences = () => {
  return request.get<UserPreferences>('/system/user/preferences')
}

/**
 * 更新用户偏好设置
 */
export const updateUserPreferences = (preferences: Partial<UserPreferences>) => {
  return request.put<UserPreferences>('/system/user/preferences', preferences)
}

/**
 * 获取主题设置
 */
export const getThemeSettings = () => {
  return request.get<ThemeSettings>('/system/theme')
}

/**
 * 更新主题设置
 */
export const updateThemeSettings = (settings: Partial<ThemeSettings>) => {
  return request.put<ThemeSettings>('/system/theme', settings)
}

/**
 * 获取语言设置
 */
export const getLanguageSettings = () => {
  return request.get<LanguageSettings>('/system/language')
}

/**
 * 更新语言设置
 */
export const updateLanguageSettings = (settings: Partial<LanguageSettings>) => {
  return request.put<LanguageSettings>('/system/language', settings)
}

/**
 * 获取可用语言列表
 */
export const getAvailableLanguages = () => {
  return request.get<Array<{ code: string; name: string; nativeName: string }>>('/system/languages')
}

/**
 * 获取可用主题列表
 */
export const getAvailableThemes = () => {
  return request.get<Array<{ id: string; name: string; preview: string }>>('/system/themes')
}

/**
 * 重置用户设置
 */
export const resetUserSettings = () => {
  return request.post('/system/user/reset-settings')
}

/**
 * 导出用户设置
 */
export const exportUserSettings = () => {
  return request.get('/system/user/export-settings', {
    responseType: 'blob'
  })
}

/**
 * 导入用户设置
 */
export const importUserSettings = (file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  
  return request.post('/system/user/import-settings', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
