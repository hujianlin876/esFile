// 用户角色
export interface Role {
  id: number
  name: string
  code: string
  description?: string
  status: string
  createTime: string
  updateTime: string
}

// 权限
export interface Permission {
  id: number
  name: string
  code: string
  type: string
  url?: string
  method?: string
  status: string
  createTime: string
  updateTime: string
}

// 用户信息
export interface User {
  id: number
  username: string
  nickname?: string
  realName?: string
  email?: string
  phone?: string
  avatar?: string
  gender?: string
  birthday?: string
  status: string
  lastLoginTime?: string
  lastLoginIp?: string
  deptId?: number
  position?: string
  roles: Role[]
  permissions: Permission[]
  createTime: string
  updateTime: string
}

// 登录请求
export interface LoginRequest {
  username: string
  password: string
  rememberMe?: boolean
  captcha?: string
}

// 登录响应
export interface LoginResponse {
  token: string
  user: User
  expiresIn: number
}

// 注册请求
export interface RegisterRequest {
  username: string
  password: string
  confirmPassword: string
  email: string
  nickname?: string
  captcha?: string
}

// 修改密码请求
export interface ChangePasswordRequest {
  oldPassword: string
  newPassword: string
  confirmPassword: string
}

// 重置密码请求
export interface ResetPasswordRequest {
  email: string
  captcha?: string
}
