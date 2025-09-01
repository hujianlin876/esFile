/**
 * 与后端ResponseResult匹配的前端响应类型
 */

/**
 * 通用API响应格式
 */
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
  timestamp: string
  path?: string
  total?: number
  pageNum?: number
  pageSize?: number
}

/**
 * 分页请求参数
 */
export interface PageRequest {
  page: number
  size: number
  [key: string]: any
}

/**
 * 分页响应结果
 */
export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}

/**
 * 用户信息（后端格式）
 */
export interface BackendUser {
  id: number
  username: string
  nickname?: string
  email: string
  phone?: string
  avatar?: string
  gender?: string
  status: number
  deptId?: number
  createTime: string
  updateTime: string
  roles: string[]
  permissions: string[]
}

/**
 * 登录响应（后端格式）
 */
export interface BackendLoginResponse {
  token: string
  tokenType: string
  expiresIn: number
}

/**
 * 文件信息（后端格式）
 */
export interface BackendFileInfo {
  id: number
  fileName: string
  originalName: string
  filePath: string
  fileSize: number
  fileType: string
  mimeType: string
  description?: string
  tags?: string
  isPublic: number
  uploadUserId: number
  uploadUserName: string
  downloadCount: number
  createTime: string
  updateTime: string
}

/**
 * 仪表板统计数据（后端格式）
 */
export interface BackendDashboardStats {
  totalFiles: number
  totalUsers: number
  totalSize: number
  todayUploads: number
  recentUploads: number
  recentDownloads: number
  activeUsers: number
}

/**
 * 图表数据（后端格式）
 */
export interface BackendChartData {
  fileTypeDistribution: Array<{ name: string; value: number }>
  uploadTrends: { dates: string[]; values: number[] }
  userActivity: Array<{ date: string; uploads: number; downloads: number }>
  storageUsage: { used: number; total: number; percentage: number }
}

/**
 * 活动记录（后端格式）
 */
export interface BackendActivity {
  id: number
  type: string
  title: string
  description: string
  createTime: string
}
