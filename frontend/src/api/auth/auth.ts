import request from '@/api/request'
import type { 
  LoginRequest, 
  LoginResponse, 
  RegisterRequest, 
  ChangePasswordRequest,
  ResetPasswordRequest,
  UserProfileUpdate,
  User,
  Captcha
} from '@/api/types/auth'

/**
 * 用户登录
 */
export const login = (data: LoginRequest) => {
  return request.post<LoginResponse>('/auth/login', data)
}

/**
 * 用户注册
 */
export const register = (data: RegisterRequest) => {
  return request.post<User>('/auth/register', data)
}

/**
 * 用户登出
 */
export const logout = () => {
  return request.post('/auth/logout')
}

/**
 * 刷新Token
 */
export const refreshToken = (refreshToken: string) => {
  return request.post<{ token: string; expiresIn: number }>('/auth/refresh', { refreshToken })
}

/**
 * 获取当前用户信息
 */
export const getCurrentUser = () => {
  return request.get<User>('/auth/me')
}

/**
 * 修改密码
 */
export const changePassword = (data: ChangePasswordRequest) => {
  return request.post('/auth/change-password', data)
}

/**
 * 忘记密码
 */
export const forgotPassword = (email: string) => {
  return request.post('/auth/forgot-password', { email })
}

/**
 * 重置密码
 */
export const resetPassword = (data: ResetPasswordRequest) => {
  return request.post('/auth/reset-password', data)
}

/**
 * 更新用户资料
 */
export const updateProfile = (data: UserProfileUpdate) => {
  return request.put<User>('/auth/profile', data)
}

/**
 * 上传头像
 */
export const uploadAvatar = (file: File) => {
  const formData = new FormData()
  formData.append('avatar', file)
  
  return request.post<{ avatar: string }>('/auth/avatar', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 获取验证码
 */
export const getCaptcha = () => {
  return request.get<Captcha>('/auth/captcha')
}

/**
 * 验证验证码
 */
export const verifyCaptcha = (captchaId: string, captcha: string) => {
  return request.post('/auth/verify-captcha', { captchaId, captcha })
}

/**
 * 发送邮箱验证码
 */
export const sendEmailVerification = (email: string) => {
  return request.post('/auth/send-email-verification', { email })
}

/**
 * 验证邮箱
 */
export const verifyEmail = (token: string) => {
  return request.post('/auth/verify-email', { token })
}

/**
 * 发送手机验证码
 */
export const sendSmsVerification = (phone: string) => {
  return request.post('/auth/send-sms-verification', { phone })
}

/**
 * 验证手机号
 */
export const verifyPhone = (phone: string, code: string) => {
  return request.post('/auth/verify-phone', { phone, code })
}

/**
 * 绑定第三方账号
 */
export const bindThirdParty = (provider: 'github' | 'google' | 'wechat', code: string) => {
  return request.post('/auth/bind-third-party', { provider, code })
}

/**
 * 解绑第三方账号
 */
export const unbindThirdParty = (provider: 'github' | 'google' | 'wechat') => {
  return request.post('/auth/unbind-third-party', { provider })
}

/**
 * 获取登录历史
 */
export const getLoginHistory = (page = 1, size = 20) => {
  return request.get('/auth/login-history', {
    params: { page, size }
  })
}

/**
 * 获取活跃会话
 */
export const getActiveSessions = () => {
  return request.get('/auth/active-sessions')
}

/**
 * 终止会话
 */
export const terminateSession = (sessionId: string) => {
  return request.post('/auth/terminate-session', { sessionId })
}

/**
 * 终止所有其他会话
 */
export const terminateOtherSessions = () => {
  return request.post('/auth/terminate-other-sessions')
}
