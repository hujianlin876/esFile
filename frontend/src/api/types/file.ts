/**
 * 文件信息接口 - 与后端FileInfo实体匹配
 */
export interface FileInfo {
  id: number
  fileName: string                    // 文件名
  originalFileName: string            // 原始文件名
  filePath: string                    // 文件路径
  fileUrl: string                     // 文件URL
  fileSize: number                    // 文件大小
  fileType: string                    // 文件类型
  fileExtension: string               // 文件扩展名
  fileMd5: string                     // 文件MD5值
  parentFolderId?: number             // 父级文件夹ID
  uploadUserId: number                // 上传用户ID
  uploadUserName: string              // 上传用户名
  status: number                      // 文件状态：0-删除，1-正常
  isPublic: number                    // 是否公开：0-私有，1-公开
  description?: string                // 文件描述
  tags: string                        // 标签（逗号分隔的字符串）
  downloadCount: number               // 下载次数
  previewCount: number                // 预览次数
  bucketName?: string                 // MinIO存储桶名称
  objectName?: string                 // MinIO对象名称
  createTime: string                  // 创建时间
  updateTime: string                  // 更新时间
}

/**
 * 文件信息接口 - 带默认值的版本
 */
export interface FileInfoWithDefaults extends Omit<FileInfo, 'status' | 'isPublic' | 'downloadCount' | 'previewCount'> {
  status?: number                      // 文件状态：0-删除，1-正常
  isPublic?: number                    // 是否公开：0-私有，1-公开
  downloadCount?: number               // 下载次数
  previewCount?: number                // 预览次数
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
 * 文件搜索参数接口 - 与后端FileSearchDto匹配
 */
export interface FileSearchParams {
  keyword?: string
  fileType?: string
  minSize?: number
  maxSize?: number
  uploadUserId?: number
  uploadUserName?: string
  startTime?: string
  endTime?: string
  status?: number
  isPublic?: number
  tags?: string
  category?: string
  priority?: number
  orderBy?: string
  orderDirection?: 'asc' | 'desc'
  page?: number
  size?: number
  parentFolderId?: number
  includeSubFolders?: boolean
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
