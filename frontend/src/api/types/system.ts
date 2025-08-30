/**
 * 系统配置接口
 */
export interface SystemConfig {
  id: number
  key: string
  value: string
  description?: string
  category: string
  editable: boolean
  createTime: string
  updateTime: string
}

/**
 * 系统日志接口
 */
export interface SystemLog {
  id: number
  level: 'DEBUG' | 'INFO' | 'WARN' | 'ERROR'
  message: string
  stackTrace?: string
  userId?: number
  username?: string
  ipAddress?: string
  userAgent?: string
  createTime: string
}

/**
 * 系统监控接口
 */
export interface SystemMonitor {
  cpu: {
    usage: number
    cores: number
    temperature?: number
  }
  memory: {
    total: number
    used: number
    free: number
    usage: number
  }
  disk: {
    total: number
    used: number
    free: number
    usage: number
  }
  network: {
    bytesIn: number
    bytesOut: number
    packetsIn: number
    packetsOut: number
  }
  uptime: number
  loadAverage: number[]
  timestamp: string
}

/**
 * 系统通知接口
 */
export interface SystemNotification {
  id: number
  title: string
  content: string
  type: 'info' | 'success' | 'warning' | 'error'
  priority: 'low' | 'normal' | 'high' | 'urgent'
  read: boolean
  userId?: number
  createTime: string
  readTime?: string
}

/**
 * 系统备份接口
 */
export interface SystemBackup {
  id: number
  name: string
  description?: string
  type: 'full' | 'incremental'
  size: number
  status: 'pending' | 'running' | 'completed' | 'failed'
  createTime: string
  completeTime?: string
  downloadUrl?: string
}

/**
 * 系统更新接口
 */
export interface SystemUpdate {
  id: number
  version: string
  description: string
  releaseNotes: string
  downloadUrl: string
  size: number
  checksum: string
  releaseDate: string
  isLatest: boolean
  isInstalled: boolean
}
