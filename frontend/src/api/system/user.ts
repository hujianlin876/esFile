import request from '@/api/request'
import type { User } from '@/api/types/auth'
import type { PageRequest, PageResult } from '@/api/types/common'

/**
 * 获取用户列表
 */
export const getUserList = (params: PageRequest & {
  keyword?: string
  status?: string
  roleId?: number
}) => {
  return request.get<PageResult<User>>('/system/users', { params })
}

/**
 * 获取用户详情
 */
export const getUserDetail = (id: number) => {
  return request.get<User>(`/system/users/${id}`)
}

/**
 * 创建用户
 */
export const createUser = (data: Partial<User>) => {
  return request.post<User>('/system/users', data)
}

/**
 * 更新用户
 */
export const updateUser = (id: number, data: Partial<User>) => {
  return request.put<User>(`/system/users/${id}`, data)
}

/**
 * 删除用户
 */
export const deleteUser = (id: number) => {
  return request.delete(`/system/users/${id}`)
}

/**
 * 重置用户密码
 */
export const resetUserPassword = (id: number) => {
  return request.post(`/system/users/${id}/reset-password`)
}

/**
 * 修改用户密码
 */
export const changeUserPassword = (id: number, data: { oldPassword: string; newPassword: string }) => {
  return request.post(`/system/users/${id}/change-password`, data)
}

/**
 * 切换用户状态
 */
export const toggleUserStatus = (id: number) => {
  return request.post(`/system/users/${id}/toggle-status`)
}

/**
 * 批量删除用户
 */
export const batchDeleteUsers = (ids: number[]) => {
  return request.post('/system/users/batch-delete', { ids })
}

/**
 * 批量更新用户状态
 */
export const batchUpdateUserStatus = (ids: number[], status: string) => {
  return request.post('/system/users/batch-update-status', { ids, status })
}

/**
 * 导入用户
 */
export const importUsers = (file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/system/users/import', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 导出用户
 */
export const exportUsers = (params?: {
  keyword?: string
  status?: string
  roleId?: number
}) => {
  return request.get('/system/users/export', {
    params,
    responseType: 'blob'
  })
}
