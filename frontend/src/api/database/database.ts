import request from '@/api/request'

export interface SqlExecuteRequest {
  sql: string
  mode: 'query' | 'update' | 'ddl'
  timeout: number
}

export interface SqlExecuteResponse {
  success: boolean
  message: string
  executionTime: number
  affectedRows?: number
  resultCount?: number
  data?: any[]
  error?: string
}

export interface DatabaseStats {
  connections: number
  tables: number
  queries: number
  slowQueries: number
  version: string
  uptime: number
  size: number
}

/**
 * 执行SQL语句
 */
export const executeSql = (data: SqlExecuteRequest) => {
  return request.post<SqlExecuteResponse>('/api/database/execute', data)
}

/**
 * 获取数据库状态
 */
export const getDatabaseStats = () => {
  return request.get<DatabaseStats>('/api/database/stats')
}

/**
 * 获取数据库表列表
 */
export const getTableList = () => {
  return request.get<string[]>('/api/database/tables')
}

/**
 * 获取表结构
 */
export const getTableStructure = (tableName: string) => {
  return request.get(`/api/database/tables/${tableName}/structure`)
}

/**
 * 获取表数据
 */
export const getTableData = (tableName: string, page = 1, size = 100) => {
  return request.get(`/api/database/tables/${tableName}/data`, {
    params: { page, size }
  })
}

/**
 * 备份数据库
 */
export const backupDatabase = (data: { name: string; description?: string }) => {
  return request.post('/api/database/backup', data)
}

/**
 * 恢复数据库
 */
export const restoreDatabase = (backupId: number) => {
  return request.post(`/api/database/restore/${backupId}`)
}
