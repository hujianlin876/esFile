/**
 * 分页请求参数接口
 */
export interface PageRequest {
  page: number
  size: number
}

/**
 * 分页结果接口
 */
export interface PageResult<T> {
  list: T[]
  total: number
  page: number
  size: number
  pages: number
}

/**
 * API响应基础接口
 */
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
  success: boolean
  timestamp: number
}

/**
 * 列表响应接口
 */
export interface ListResponse<T> {
  list: T[]
  total: number
}

/**
 * 操作结果接口
 */
export interface OperationResult {
  success: boolean
  message: string
  data?: any
  errors?: string[]
}

/**
 * 排序参数接口
 */
export interface SortParams {
  sortBy: string
  sortOrder: 'asc' | 'desc'
}

/**
 * 搜索参数接口
 */
export interface SearchParams {
  keyword?: string
  filters?: Record<string, any>
  sortBy?: string
  sortOrder?: 'asc' | 'desc'
}

/**
 * 时间范围接口
 */
export interface TimeRange {
  startTime: string
  endTime: string
}

/**
 * 文件上传响应接口
 */
export interface UploadResponse {
  filename: string
  originalName: string
  size: number
  url: string
  thumbnail?: string
}

/**
 * 批量操作响应接口
 */
export interface BatchOperationResponse {
  success: number
  failed: number
  total: number
  results: Array<{
    id: number
    success: boolean
    message: string
  }>
}

/**
 * 统计信息接口
 */
export interface Statistics {
  total: number
  today: number
  week: number
  month: number
  growth: number
}

/**
 * 图表数据接口
 */
export interface ChartData {
  labels: string[]
  datasets: Array<{
    label: string
    data: number[]
    backgroundColor?: string | string[]
    borderColor?: string | string[]
    borderWidth?: number
  }>
}

/**
 * 导出参数接口
 */
export interface ExportParams {
  format: 'excel' | 'csv' | 'pdf'
  filename?: string
  filters?: Record<string, any>
  columns?: string[]
}

/**
 * 导入结果接口
 */
export interface ImportResult {
  total: number
  success: number
  failed: number
  errors: Array<{
    row: number
    field: string
    message: string
  }>
}
