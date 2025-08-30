import request from '@/api/request'

/**
 * 仪表板统计数据接口
 */
export interface DashboardStats {
  totalFiles: number
  totalUsers: number
  totalSize: number
  recentUploads: number
  recentDownloads: number
  activeUsers: number
}

/**
 * 图表数据接口
 */
export interface ChartData {
  fileTypeDistribution: Array<{ name: string; value: number }>
  uploadTrends: { dates: string[]; values: number[] }
  userActivity: Array<{ date: string; uploads: number; downloads: number }>
  storageUsage: { used: number; total: number; percentage: number }
}

/**
 * 活动记录接口
 */
export interface Activity {
  id: number
  type: 'upload' | 'download' | 'delete' | 'edit' | 'share'
  description: string
  userId: number
  username: string
  fileName?: string
  fileSize?: number
  createTime: string
}

/**
 * 获取仪表板统计数据
 */
export const getDashboardStats = () => {
  return request.get<DashboardStats>('/dashboard/stats')
}

/**
 * 获取图表数据
 */
export const getChartData = (period: 'day' | 'week' | 'month' = 'week') => {
  return request.get<ChartData>('/dashboard/charts', {
    params: { period }
  })
}

/**
 * 获取最近活动
 */
export const getRecentActivities = (limit = 10) => {
  return request.get<Activity[]>('/dashboard/activities', {
    params: { limit }
  })
}

/**
 * 获取系统状态
 */
export const getSystemStatus = () => {
  return request.get<{
    cpu: number
    memory: number
    disk: number
    network: { in: number; out: number }
    uptime: number
  }>('/dashboard/system-status')
}

/**
 * 获取用户活跃度统计
 */
export const getUserActivityStats = (days = 7) => {
  return request.get<Array<{ date: string; activeUsers: number; totalActions: number }>>('/dashboard/user-activity', {
    params: { days }
  })
}

/**
 * 获取文件上传趋势
 */
export const getUploadTrends = (period: 'day' | 'week' | 'month' = 'week') => {
  return request.get<{ dates: string[]; uploads: number[]; downloads: number[] }>('/dashboard/upload-trends', {
    params: { period }
  })
}

/**
 * 获取存储使用情况
 */
export const getStorageUsage = () => {
  return request.get<{
    total: number
    used: number
    available: number
    usageByType: Record<string, number>
  }>('/dashboard/storage-usage')
}
