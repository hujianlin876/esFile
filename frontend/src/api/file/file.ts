import request from '@/api/request'
import type { FileInfo } from '@/api/types/file'
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
  return request.get<PageResult<FileInfo>>('/files', { params })
}

/**
 * 获取文件详情
 */
export const getFileDetail = (id: number) => {
  return request.get<FileInfo>(`/files/${id}`)
}

/**
 * 上传文件
 */
export const uploadFile = (file: File, onProgress?: (progress: number) => void) => {
  const formData = new FormData()
  formData.append('file', file)
  
  return request.post<FileInfo>('/files/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    onUploadProgress: (progressEvent) => {
      if (onProgress && progressEvent.total) {
        const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total)
        onProgress(progress)
      }
    }
  })
}

/**
 * 批量上传文件
 */
export const batchUploadFiles = (files: File[], onProgress?: (progress: number) => void) => {
  const formData = new FormData()
  files.forEach(file => {
    formData.append('files', file)
  })
  
  return request.post<FileInfo[]>('/files/batch-upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    onUploadProgress: (progressEvent) => {
      if (onProgress && progressEvent.total) {
        const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total)
        onProgress(progress)
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
  return request.post(`/files/${id}/move`, { targetFolderId })
}

/**
 * 复制文件
 */
export const copyFile = (id: number, targetFolderId: number) => {
  return request.post(`/files/${id}/copy`, { targetFolderId })
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
export const getFileThumbnail = (id: number, size = 'medium') => {
  return request.get(`/files/${id}/thumbnail`, {
    params: { size },
    responseType: 'blob'
  })
}

/**
 * 搜索文件
 */
export const searchFiles = (params: {
  keyword: string
  fileType?: string
  dateRange?: string
  sizeRange?: string
  uploader?: string
  tags?: string[]
  page?: number
  size?: number
}) => {
  return request.get<PageResult<FileInfo>>('/files/search', { params })
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
  }>('/files/stats')
}

/**
 * 获取文件夹结构
 */
export const getFolderStructure = (folderId?: number) => {
  return request.get('/files/folders', {
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
