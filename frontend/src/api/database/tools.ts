import request from '@/api/request'

/**
 * SQL格式化请求接口
 */
export interface SqlFormatRequest {
  sql: string
  dialect: 'mysql' | 'postgresql' | 'oracle' | 'sqlserver' | 'sqlite'
  indentSize: number
  uppercase: boolean
  linesBetweenQueries: number
}

/**
 * SQL格式化响应接口
 */
export interface SqlFormatResponse {
  formattedSql: string
  originalSql: string
  errors: string[]
  warnings: string[]
  statistics: {
    lineCount: number
    wordCount: number
    complexity: number
  }
}

/**
 * 结果导出请求接口
 */
export interface ExportRequest {
  data: any[]
  format: 'excel' | 'csv' | 'json' | 'xml'
  filename?: string
  includeHeaders?: boolean
  sheetName?: string
  delimiter?: string
}

/**
 * 结果导出响应接口
 */
export interface ExportResponse {
  downloadUrl: string
  filename: string
  size: number
  expiresAt: string
}

/**
 * SQL分析请求接口
 */
export interface SqlAnalysisRequest {
  sql: string
  includeExecutionPlan?: boolean
  includeStatistics?: boolean
}

/**
 * SQL分析响应接口
 */
export interface SqlAnalysisResponse {
  analysis: {
    complexity: 'low' | 'medium' | 'high'
    estimatedCost: number
    estimatedRows: number
    warnings: string[]
    suggestions: string[]
  }
  executionPlan?: any
  statistics?: any
}

/**
 * 格式化SQL语句
 */
export const formatSql = (data: SqlFormatRequest) => {
  return request.post<SqlFormatResponse>('/database/format-sql', data)
}

/**
 * 导出查询结果
 */
export const exportQueryResult = (data: ExportRequest) => {
  return request.post<ExportResponse>('/database/export-result', data)
}

/**
 * 分析SQL语句
 */
export const analyzeSql = (data: SqlAnalysisRequest) => {
  return request.post<SqlAnalysisResponse>('/database/analyze-sql', data)
}

/**
 * 获取SQL模板列表
 */
export const getSqlTemplates = (category?: string) => {
  return request.get<Array<{
    id: number
    name: string
    description: string
    category: string
    sql: string
    tags: string[]
    createTime: string
    updateTime: string
  }>>('/database/sql-templates', {
    params: { category }
  })
}

/**
 * 创建SQL模板
 */
export const createSqlTemplate = (data: {
  name: string
  description: string
  category: string
  sql: string
  tags: string[]
}) => {
  return request.post('/database/sql-templates', data)
}

/**
 * 更新SQL模板
 */
export const updateSqlTemplate = (id: number, data: {
  name?: string
  description?: string
  category?: string
  sql?: string
  tags?: string[]
}) => {
  return request.put(`/database/sql-templates/${id}`, data)
}

/**
 * 删除SQL模板
 */
export const deleteSqlTemplate = (id: number) => {
  return request.delete(`/database/sql-templates/${id}`)
}

/**
 * 获取SQL历史记录
 */
export const getSqlHistory = (page = 1, size = 20) => {
  return request.get<{
    list: Array<{
      id: number
      sql: string
      executionTime: number
      affectedRows?: number
      resultCount?: number
      status: 'success' | 'error'
      errorMessage?: string
      userId: number
      username: string
      createTime: string
    }>
    total: number
  }>('/database/sql-history', {
    params: { page, size }
  })
}

/**
 * 清理SQL历史记录
 */
export const clearSqlHistory = (beforeDate?: string) => {
  return request.delete('/database/sql-history', {
    params: { beforeDate }
  })
}

/**
 * 获取数据库连接池状态
 */
export const getConnectionPoolStatus = () => {
  return request.get<{
    activeConnections: number
    idleConnections: number
    totalConnections: number
    maxConnections: number
    minConnections: number
    connectionTimeout: number
    idleTimeout: number
  }>('/database/connection-pool-status')
}

/**
 * 重置数据库连接池
 */
export const resetConnectionPool = () => {
  return request.post('/database/connection-pool-reset')
}
