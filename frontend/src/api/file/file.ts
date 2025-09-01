import request from '@/api/request'
import type { FileInfo, FileSearchParams } from '@/api/types/file'
import type { PageRequest, PageResult } from '@/api/types/common'

/**
 * 获取文件列表
 */
export const getFileList = (params: PageRequest & {
  keyword?: string
  fileType?: string
  dateRange?: string
  sizeRange?: string
  uploader?: string
  tags?: string[]
  sortBy?: string
  sortOrder?: 'asc' | 'desc'
}) => {
  // 转换前端参数为后端参数格式
  const backendParams: any = {
    page: params.page,
    size: params.size,
    keyword: params.keyword,
    fileType: params.fileType,
    orderBy: params.sortBy,
    orderDirection: params.sortOrder
  }
  
  // 处理时间范围
  if (params.dateRange) {
    // TODO: 解析时间范围字符串，设置startTime和endTime
  }
  
  // 处理大小范围
  if (params.sizeRange) {
    // TODO: 解析大小范围字符串，设置minSize和maxSize
  }
  
  // 处理标签
  if (params.tags && params.tags.length > 0) {
    backendParams.tags = params.tags.join(',')
  }
  
  // 处理上传者
  if (params.uploader) {
    backendParams.uploadUserName = params.uploader
  }
  
  return request.get<PageResult<FileInfo>>('/files/query/list', { params: backendParams })
}

/**
 * 获取文件详情
 */
export const getFileDetail = (id: number) => {
  return request.get<FileInfo>(`/files/query/${id}`)
}

/**
 * 上传文件
 */
export const uploadFile = (file: File, options?: {
  description?: string
  tags?: string
  isPublic?: number
  parentFolderId?: number
  onProgress?: (progress: number) => void
}) => {
  const formData = new FormData()
  formData.append('file', file)
  
  if (options?.description) {
    formData.append('description', options.description)
  }
  if (options?.tags) {
    formData.append('tags', options.tags)
  }
  if (options?.isPublic !== undefined) {
    formData.append('isPublic', options.isPublic.toString())
  }
  if (options?.parentFolderId) {
    formData.append('parentFolderId', options.parentFolderId.toString())
  }
  
  return request.post<FileInfo>('/files/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    onUploadProgress: (progressEvent) => {
      if (options?.onProgress && progressEvent.total) {
        const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total)
        options.onProgress(progress)
      }
    }
  })
}

/**
 * 批量上传文件
 */
export const batchUploadFiles = (files: File[], options?: {
  description?: string
  tags?: string
  isPublic?: number
  parentFolderId?: number
  onProgress?: (progress: number) => void
}) => {
  const formData = new FormData()
  files.forEach(file => {
    formData.append('files', file)
  })
  
  if (options?.description) {
    formData.append('description', options.description)
  }
  if (options?.tags) {
    formData.append('tags', options.tags)
  }
  if (options?.isPublic !== undefined) {
    formData.append('isPublic', options.isPublic.toString())
  }
  if (options?.parentFolderId) {
    formData.append('parentFolderId', options.parentFolderId.toString())
  }
  
  return request.post<FileInfo[]>('/files/batch-upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    onUploadProgress: (progressEvent) => {
      if (options?.onProgress && progressEvent.total) {
        const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total)
        options.onProgress(progress)
      }
    }
  })
}

/**
 * 下载文件
 */
export const downloadFile = (id: number) => {
  return request.get(`/files/${id}/download`, {
    responseType: 'blob'
  })
}

/**
 * 批量下载文件
 */
export const batchDownloadFiles = (ids: number[]) => {
  return request.post('/files/batch-download', { ids }, {
    responseType: 'blob'
  })
}

/**
 * 删除文件
 */
export const deleteFile = (id: number) => {
  return request.delete(`/files/${id}`)
}

/**
 * 批量删除文件
 */
export const batchDeleteFiles = (ids: number[]) => {
  return request.post('/files/batch-delete', { ids })
}

/**
 * 更新文件信息
 */
export const updateFile = (id: number, data: Partial<FileInfo>) => {
  return request.put<FileInfo>(`/files/${id}`, data)
}

/**
 * 移动文件
 */
export const moveFile = (id: number, targetFolderId: number) => {
  return request.post(`/files/${id}/move`, null, {
    params: { targetFolderId }
  })
}

/**
 * 复制文件
 */
export const copyFile = (id: number, targetFolderId: number) => {
  return request.post(`/files/${id}/copy`, null, {
    params: { targetFolderId }
  })
}

/**
 * 获取文件预览信息
 */
export const getFilePreview = (id: number) => {
  return request.get(`/files/${id}/preview`)
}

/**
 * 获取文件内容（用于文本文件）
 */
export const getFileContent = (id: number) => {
  return request.get<{ content: string }>(`/files/${id}/content`)
}

/**
 * 获取文件缩略图
 */
export const getFileThumbnail = (id: number, width = 200, height = 200) => {
  return request.get(`/files/${id}/thumbnail`, {
    params: { width, height },
    responseType: 'blob'
  })
}

/**
 * 搜索文件
 */
export const searchFiles = (params: FileSearchParams) => {
  return request.post<PageResult<FileInfo>>('/files/query/search', params)
}

/**
 * 获取文件统计信息
 */
export const getFileStats = () => {
  return request.get<{
    totalFiles: number
    totalSize: number
    fileTypeDistribution: Record<string, number>
    recentUploads: number
  }>('/files/query/stats')
}

/**
 * 获取文件夹结构
 */
export const getFolderStructure = (folderId?: number) => {
  return request.get('/files/query/folders/structure', {
    params: { parentId: folderId }
  })
}

/**
 * 创建文件夹
 */
export const createFolder = (data: { name: string; parentId?: number; description?: string }) => {
  return request.post<FileInfo>('/files/folders', data)
}

/**
 * 删除文件夹
 */
export const deleteFolder = (id: number, recursive = false) => {
  return request.delete(`/files/folders/${id}`, {
    params: { recursive }
  })
}
