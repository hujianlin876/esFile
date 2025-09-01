<template>
  <div class="database-manager">
    <div class="page-header">
      <h1 class="page-title">数据库管理</h1>
      <p class="page-desc">执行SQL语句、监控数据库状态、管理数据</p>
    </div>
    
    <!-- 数据库状态 -->
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6">
        <el-card class="status-card">
          <div class="status-content">
            <div class="status-icon" style="background: #67c23a;">
              <el-icon color="white"><DataBase /></el-icon>
            </div>
            <div class="status-info">
              <div class="status-number">{{ dbStats.connections }}</div>
              <div class="status-label">活跃连接</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="status-card">
          <div class="status-content">
            <div class="status-icon" style="background: #409eff;">
              <el-icon color="white"><Document /></el-icon>
            </div>
            <div class="status-info">
              <div class="status-number">{{ dbStats.tables }}</div>
              <div class="status-label">数据表</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="status-card">
          <div class="status-content">
            <div class="status-icon" style="background: #e6a23c;">
              <el-icon color="white"><TrendCharts /></el-icon>
            </div>
            <div class="status-info">
              <div class="status-number">{{ dbStats.queries }}/s</div>
              <div class="status-label">查询频率</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="status-card">
          <div class="status-content">
            <div class="status-icon" style="background: #f56c6c;">
              <el-icon color="white"><Warning /></el-icon>
            </div>
            <div class="status-info">
              <div class="status-number">{{ dbStats.slowQueries }}</div>
              <div class="status-label">慢查询</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- SQL执行器 -->
    <el-card class="sql-executor" style="margin-bottom: 20px;">
      <template #header>
        <div class="card-header">
          <span>SQL执行器</span>
          <div class="header-actions">
            <el-button size="small" @click="clearSql">清空</el-button>
            <el-button size="small" @click="handleFormatSql">格式化</el-button>
            <el-button type="primary" size="small" @click="executeSql" :loading="executing">
              {{ executing ? '执行中...' : '执行SQL' }}
            </el-button>
          </div>
        </div>
      </template>
      
      <el-form :model="sqlForm" label-width="80px">
        <el-form-item label="SQL语句">
          <el-input
            v-model="sqlForm.sql"
            type="textarea"
            :rows="6"
            placeholder="请输入SQL语句..."
            @keydown.ctrl.enter="executeSql"
          />
        </el-form-item>
        <el-form-item label="执行模式">
          <el-radio-group v-model="sqlForm.mode">
            <el-radio label="query">查询模式 (SELECT/SHOW/DESCRIBE)</el-radio>
            <el-radio label="update">更新模式 (INSERT/UPDATE/DELETE)</el-radio>
            <el-radio label="ddl">DDL模式 (CREATE/DROP/ALTER)</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="超时时间">
          <el-input-number
            v-model="sqlForm.timeout"
            :min="1"
            :max="300"
            style="width: 120px"
          />
          <span style="margin-left: 8px;">秒</span>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 执行结果 -->
    <el-card v-if="sqlResult" class="result-card">
      <template #header>
        <div class="card-header">
          <span>执行结果</span>
          <div class="header-actions">
            <el-button size="small" @click="handleExportResult">导出结果</el-button>
            <el-button size="small" @click="clearResult">清空结果</el-button>
          </div>
        </div>
      </template>
      
      <div class="result-info">
        <el-alert
          :title="sqlResult.success ? '执行成功' : '执行失败'"
          :type="sqlResult.success ? 'success' : 'error'"
          :description="sqlResult.message"
          show-icon
          style="margin-bottom: 16px;"
        />
        
        <div class="result-stats">
          <span>执行时间: {{ sqlResult.executionTime }}ms</span>
          <span v-if="sqlResult.affectedRows !== undefined">影响行数: {{ sqlResult.affectedRows }}</span>
          <span v-if="sqlResult.resultCount !== undefined">结果数量: {{ sqlResult.resultCount }}</span>
        </div>
      </div>
      
      <!-- 查询结果表格 -->
      <div v-if="sqlResult.data && sqlResult.data.length > 0" class="result-table">
        <el-table :data="sqlResult.data" style="width: 100%" max-height="400">
          <el-table-column
            v-for="column in resultColumns"
            :key="column"
            :prop="column"
            :label="column"
            min-width="120"
          />
        </el-table>
      </div>
      
      <!-- 错误信息 -->
      <div v-if="!sqlResult.success && sqlResult.error" class="error-info">
        <el-alert
          title="错误详情"
          type="error"
          :description="sqlResult.error"
          show-icon
        />
      </div>
    </el-card>
    
    <!-- 常用SQL模板 -->
    <el-card class="sql-templates">
      <template #header>
        <span>常用SQL模板</span>
      </template>
      
      <el-row :gutter="16">
        <el-col :span="6">
          <el-card class="template-card" shadow="hover">
            <template #header>
              <span>查询表结构</span>
            </template>
            <div class="template-content">
              <code>DESCRIBE table_name;</code>
              <el-button type="text" size="small" @click="useTemplate('DESCRIBE table_name;')">
                使用
              </el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="template-card" shadow="hover">
            <template #header>
              <span>查看表数据</span>
            </template>
            <div class="template-content">
              <code>SELECT * FROM table_name LIMIT 100;</code>
              <el-button type="text" size="small" @click="useTemplate('SELECT * FROM table_name LIMIT 100;')">
                使用
              </el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="template-card" shadow="hover">
            <template #header>
              <span>查看索引</span>
            </template>
            <div class="template-content">
              <code>SHOW INDEX FROM table_name;</code>
              <el-button type="text" size="small" @click="useTemplate('SHOW INDEX FROM table_name;')">
                使用
              </el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="template-card" shadow="hover">
            <template #header>
              <span>删除表</span>
            </template>
            <div class="template-content">
              <code>DROP TABLE table_name;</code>
              <el-button type="text" size="small" @click="useTemplate('DROP TABLE table_name;')">
                使用
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row :gutter="16" style="margin-top: 16px;">
        <el-col :span="6">
          <el-card class="template-card" shadow="hover">
            <template #header>
              <span>创建表</span>
            </template>
            <div class="template-content">
              <code>CREATE TABLE table_name (id INT PRIMARY KEY, name VARCHAR(50));</code>
              <el-button type="text" size="small" @click="useTemplate('CREATE TABLE table_name (id INT PRIMARY KEY, name VARCHAR(50));')">
                使用
              </el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="template-card" shadow="hover">
            <template #header>
              <span>插入数据</span>
            </template>
            <div class="template-content">
              <code>INSERT INTO table_name (name) VALUES ('test');</code>
              <el-button type="text" size="small" @click="useTemplate('INSERT INTO table_name (name) VALUES (test);')">
                使用
              </el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="template-card" shadow="hover">
            <template #header>
              <span>更新数据</span>
            </template>
            <div class="template-content">
              <code>UPDATE table_name SET name = 'new_name' WHERE id = 1;</code>
              <el-button type="text" size="small" @click="useTemplate('UPDATE table_name SET name = new_name WHERE id = 1;')">
                使用
              </el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="template-card" shadow="hover">
            <template #header>
              <span>删除数据</span>
            </template>
            <div class="template-content">
              <code>DELETE FROM table_name WHERE id = 1;</code>
              <el-button type="text" size="small" @click="useTemplate('DELETE FROM table_name WHERE id = 1;')">
                使用
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Document, TrendCharts, Warning } from '@element-plus/icons-vue'
import { executeSql as executeSqlApi, getDatabaseStats } from '@/api/database/database'
import { formatSql, exportQueryResult } from '@/api/database/tools'

interface SqlResult {
  success: boolean
  message: string
  executionTime: number
  affectedRows?: number
  resultCount?: number
  data?: any[]
  error?: string
}

const executing = ref(false)
const sqlResult = ref<SqlResult | null>(null)

const sqlForm = reactive({
  sql: '',
  mode: 'query',
  timeout: 30
})

const dbStats = reactive({
  connections: 0,
  tables: 0,
  queries: 0,
  slowQueries: 0
})

// 计算属性
const resultColumns = computed(() => {
  if (!sqlResult.value?.data || sqlResult.value.data.length === 0) return []
  return Object.keys(sqlResult.value.data[0])
})

// 方法
const executeSql = async () => {
  if (!sqlForm.sql.trim()) {
    ElMessage.warning('请输入SQL语句')
    return
  }
  
  executing.value = true
  try {
    const response = await executeSqlApi({
      sql: sqlForm.sql,
      mode: sqlForm.mode as 'query' | 'update' | 'ddl',
      timeout: sqlForm.timeout
    })
    
    const resultData = response.data.data as any
    sqlResult.value = {
      success: resultData?.success || false,
      message: resultData?.message || '',
      executionTime: resultData?.executionTime || 0,
      affectedRows: resultData?.affectedRows,
      resultCount: resultData?.resultCount,
      data: resultData?.data,
      error: resultData?.error
    }
    
    if (response.data.success) {
      ElMessage.success('SQL执行成功')
    } else {
      ElMessage.error('SQL执行失败')
    }
  } catch (error) {
    sqlResult.value = {
      success: false,
      message: 'SQL执行失败',
      executionTime: 0,
      error: error instanceof Error ? error.message : '未知错误'
    }
    ElMessage.error('SQL执行失败')
  } finally {
    executing.value = false
  }
}

const clearSql = () => {
  sqlForm.sql = ''
}

const handleFormatSql = async () => {
  if (!sqlForm.sql.trim()) {
    ElMessage.warning('请先输入SQL语句')
    return
  }
  
  try {
    const response = await formatSql({
      sql: sqlForm.sql,
      dialect: 'mysql', // 可以根据实际数据库类型调整
      indentSize: 2,
      uppercase: true,
      linesBetweenQueries: 1
    })
    
    sqlForm.sql = (response.data as any)?.formattedSql || sqlForm.sql
    ElMessage.success('SQL格式化成功')
  } catch (error) {
    ElMessage.error('SQL格式化失败')
  }
}

const handleExportResult = async () => {
  if (!sqlResult.value?.data || sqlResult.value.data.length === 0) {
    ElMessage.warning('没有可导出的数据')
    return
  }
  
  try {
    const response = await exportQueryResult({
      data: sqlResult.value?.data || [],
      format: 'excel',
      filename: `查询结果_${new Date().toISOString().slice(0, 19).replace(/:/g, '-')}`,
      includeHeaders: true,
      sheetName: '查询结果'
    })
    
    // 下载文件
    const exportData = response.data as any
    const link = document.createElement('a')
    link.href = exportData?.downloadUrl || '#'
    link.download = exportData?.filename || 'export.xlsx'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    
    ElMessage.success('结果导出成功')
  } catch (error) {
    ElMessage.error('结果导出失败')
  }
}

const clearResult = () => {
  sqlResult.value = null
}

const useTemplate = (sql: string) => {
  sqlForm.sql = sql
}

const loadDbStats = async () => {
  try {
    const response = await getDatabaseStats()
    const statsData = response.data as any
    if (statsData) {
      Object.assign(dbStats, statsData)
    }
  } catch (error) {
    console.error('加载数据库状态失败:', error)
  }
}

// 生命周期
onMounted(() => {
  loadDbStats()
  // 定时更新数据库状态
  setInterval(loadDbStats, 30000)
})
</script>

<style lang="scss" scoped>
@import './DatabaseManager.scss';
</style>
