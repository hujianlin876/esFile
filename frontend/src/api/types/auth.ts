/**
 * 用户信息接口
 */
export interface User {
  id: number
  username: string
  nickname?: string
  email: string
  phone?: string
  avatar?: string
  gender?: 'male' | 'female' | 'other'
  birthday?: string
  status: 'active' | 'inactive' | 'locked'
  lastLoginTime?: string
  lastLoginIp?: string
  createTime: string
  updateTime: string
  roles: Role[]
  permissions: Permission[]
}

/**
 * 角色信息接口
 */
export interface Role {
  id: number
  name: string
  code: string
  description?: string
  status: 'active' | 'inactive'
  createTime: string
  updateTime: string
  permissions: Permission[]
  userCount: number
}

/**
 * 权限信息接口
 */
export interface Permission {
  id: number
  name: string
  code: string
  type: 'menu' | 'function' | 'data'
  path?: string
  component?: string
  icon?: string
  parentId?: number
  description?: string
  status: 'active' | 'inactive'
  sort: number
  createTime: string
  updateTime: string
  children?: Permission[]
}

/**
 * 登录请求参数接口
 */
export interface LoginRequest {
  username: string
  password: string
  captcha?: string
  rememberMe?: boolean
}

/**
 * 登录响应接口
 */
export interface LoginResponse {
  token: string
  refreshToken: string
  expiresIn: number
  user: User
  permissions: string[]
}

/**
 * 注册请求参数接口
 */
export interface RegisterRequest {
  username: string
  email: string
  password: string
  confirmPassword: string
  nickname?: string
  captcha?: string
}

/**
 * 修改密码请求参数接口
 */
export interface ChangePasswordRequest {
  oldPassword: string
  newPassword: string
  confirmPassword: string
}

/**
 * 重置密码请求参数接口
 */
export interface ResetPasswordRequest {
  email: string
  token: string
  newPassword: string
  confirmPassword: string
}

/**
 * 用户资料更新接口
 */
export interface UserProfileUpdate {
  nickname?: string
  email?: string
  phone?: string
  gender?: 'male' | 'female' | 'other'
  birthday?: string
  avatar?: string
}

/**
 * 用户状态更新接口
 */
export interface UserStatusUpdate {
  status: 'active' | 'inactive' | 'locked'
  reason?: string
}

/**
 * 角色权限分配接口
 */
export interface RolePermissionAssignment {
  roleId: number
  permissionIds: number[]
}

/**
 * 用户角色分配接口
 */
export interface UserRoleAssignment {
  userId: number
  roleIds: number[]
}

/**
 * 权限树节点接口
 */
export interface PermissionTreeNode {
  id: number
  label: string
  children?: PermissionTreeNode[]
  disabled?: boolean
  checked?: boolean
  indeterminate?: boolean
}

/**
 * 菜单项接口
 */
export interface MenuItem {
  id: number
  name: string
  path: string
  component: string
  icon?: string
  parentId?: number
  sort: number
  hidden: boolean
  children?: MenuItem[]
  permissions?: string[]
}

/**
 * 认证状态接口
 */
export interface AuthState {
  isAuthenticated: boolean
  user: User | null
  token: string | null
  refreshToken: string | null
  permissions: string[]
  roles: string[]
}

/**
 * 验证码接口
 */
export interface Captcha {
  id: string
  image: string
  expiresIn: number
}

/**
 * 忘记密码请求接口
 */
export interface ForgotPasswordRequest {
  email: string
  captcha: string
}

/**
 * 验证码验证接口
 */
export interface VerifyCaptchaRequest {
  captchaId: string
  captcha: string
}
