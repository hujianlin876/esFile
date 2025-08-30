import request from '@/api/request'
import type { Role, Permission } from '@/api/types/auth'

/**
 * 获取角色列表
 */
export const getRoleList = () => {
  return request.get<Role[]>('/system/roles')
}

/**
 * 创建角色
 */
export const createRole = (data: Partial<Role>) => {
  return request.post<Role>('/system/roles', data)
}

/**
 * 更新角色
 */
export const updateRole = (id: number, data: Partial<Role>) => {
  return request.put<Role>(`/system/roles/${id}`, data)
}

/**
 * 删除角色
 */
export const deleteRole = (id: number) => {
  return request.delete(`/system/roles/${id}`)
}

/**
 * 获取权限列表
 */
export const getPermissionList = () => {
  return request.get<Permission[]>('/system/permissions')
}

/**
 * 创建权限
 */
export const createPermission = (data: Partial<Permission>) => {
  return request.post<Permission>('/system/permissions', data)
}

/**
 * 更新权限
 */
export const updatePermission = (id: number, data: Partial<Permission>) => {
  return request.put<Permission>(`/system/permissions/${id}`, data)
}

/**
 * 删除权限
 */
export const deletePermission = (id: number) => {
  return request.delete(`/system/permissions/${id}`)
}

/**
 * 获取角色权限
 */
export const getRolePermissions = (roleId: number) => {
  return request.get(`/system/roles/${roleId}/permissions`)
}

/**
 * 更新角色权限
 */
export const updateRolePermissions = (roleId: number, data: { permissionIds: number[] }) => {
  return request.put(`/system/roles/${roleId}/permissions`, data)
}
