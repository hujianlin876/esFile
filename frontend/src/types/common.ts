// 分页请求参数
export interface PageRequest {
  page: number
  size: number
  sort?: string
  order?: 'asc' | 'desc'
}

// 分页响应结果
export interface PageResult<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
  numberOfElements: number
}

// 统一响应结果
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
  success: boolean
  timestamp: number
}

// 列表响应结果
export interface ListResult<T> {
  list: T[]
  total: number
}

// 选项类型
export interface Option {
  label: string
  value: string | number
  disabled?: boolean
  children?: Option[]
}

// 树形数据
export interface TreeNode<T = any> {
  id: string | number
  label: string
  children?: TreeNode<T>[]
  parentId?: string | number
  data?: T
}

// 文件信息
export interface FileInfo {
  id: string
  name: string
  size: number
  type: string
  url: string
  thumbnail?: string
  uploadTime: string
  uploader: string
  status: string
}

// 上传进度
export interface UploadProgress {
  loaded: number
  total: number
  percentage: number
  status: 'uploading' | 'success' | 'error'
}

// 搜索条件
export interface SearchCondition {
  keyword?: string
  type?: string
  dateRange?: [string, string]
  sizeRange?: [number, number]
  uploader?: string
  status?: string
}

// 排序条件
export interface SortCondition {
  field: string
  order: 'asc' | 'desc'
}

// 筛选条件
export interface FilterCondition {
  field: string
  operator: 'eq' | 'ne' | 'gt' | 'gte' | 'lt' | 'lte' | 'like' | 'in' | 'notIn'
  value: any
}
