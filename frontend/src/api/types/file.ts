/**
 * 文件信息接口
 */
export interface FileInfo {
  id: number
  name: string
  originalName: string
  path: string
  size: number
  type: string
  mimeType: string
  extension: string
  hash: string
  uploader: string
  uploaderId: number
  createTime: string
  updateTime: string
  accessTime: string
  status: 'active' | 'deleted' | 'archived'
  isFolder: boolean
  parentId?: number
  tags: string[]
  description?: string
  thumbnail?: string
  preview?: string
  downloadCount: number
  viewCount: number
  permissions: FilePermission[]
}

/**
 * 文件权限接口
 */
export interface FilePermission {
  id: number
  userId: number
  username: string
  permission: 'read' | 'write' | 'delete' | 'admin'
  grantedBy: string
  grantedTime: string
  expireTime?: string
}

/**
 * 文件上传进度接口
 */
export interface UploadProgress {
  uid: string
  name: string
  percentage: number
  status: 'uploading' | 'success' | 'error'
  response?: any
  error?: Error
}

/**
 * 文件搜索参数接口
 */
export interface FileSearchParams {
  keyword?: string
  fileType?: string
  dateRange?: string
  sizeRange?: string
  uploader?: string
  tags?: string[]
  sortBy?: string
  sortOrder?: 'asc' | 'desc'
  page?: number
  size?: number
}

/**
 * 文件上传配置接口
 */
export interface UploadConfig {
  maxFileSize: number
  allowedTypes: string[]
  maxFiles: number
  chunkSize: number
  retryCount: number
  concurrentUploads: number
}

/**
 * 文件预览信息接口
 */
export interface FilePreview {
  id: number
  type: 'image' | 'pdf' | 'video' | 'audio' | 'text' | 'office' | 'archive' | 'other'
  url: string
  thumbnail?: string
  width?: number
  height?: number
  duration?: number
  pages?: number
  content?: string
  metadata?: Record<string, any>
}

/**
 * 文件夹信息接口
 */
export interface FolderInfo {
  id: number
  name: string
  path: string
  parentId?: number
  level: number
  children: FolderInfo[]
  fileCount: number
  totalSize: number
  createTime: string
  updateTime: string
  permissions: FilePermission[]
}

/**
 * 文件操作结果接口
 */
export interface FileOperationResult {
  success: boolean
  message: string
  data?: any
  errors?: string[]
}

/**
 * 批量操作参数接口
 */
export interface BatchOperationParams {
  fileIds: number[]
  operation: 'delete' | 'move' | 'copy' | 'download' | 'update'
  targetFolderId?: number
  updates?: Partial<FileInfo>
}

/**
 * 文件统计信息接口
 */
export interface FileStats {
  totalFiles: number
  totalFolders: number
  totalSize: number
  fileTypeDistribution: Record<string, number>
  sizeDistribution: Record<string, number>
  recentUploads: number
  recentDownloads: number
  storageUsage: {
    used: number
    total: number
    percentage: number
  }
}
