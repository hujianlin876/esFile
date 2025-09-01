<template>
  <div class="permission-manager">
    <div class="page-header">
      <h1 class="page-title">权限管理</h1>
      <p class="page-desc">管理系统角色和权限，控制用户访问权限</p>
    </div>
    
    <!-- 角色管理组件 -->
    <RoleManagement
      ref="roleManagementRef"
      @manage-permissions="handleManagePermissions"
      @refresh="handleRefresh"
    />
    
    <!-- 权限管理组件 -->
    <PermissionAssignment
      ref="permissionAssignmentRef"
      :current-role="currentRole"
      @refresh="handleRefresh"
      style="margin-top: 20px;"
    />

    <!-- 快捷操作面板 -->
    <el-card class="quick-actions" style="margin-top: 20px;">
      <template #header>
        <span>快捷操作</span>
      </template>
      
      <div class="action-buttons">
        <el-button type="primary" :icon="Plus" @click="addRole">
          添加角色
        </el-button>
        
        <el-button type="success" :icon="Setting" @click="addPermission">
          添加权限
        </el-button>
        
        <el-button type="info" :icon="Refresh" @click="refreshAll">
          刷新数据
        </el-button>
        
        <el-button type="warning" :icon="Download" @click="exportData">
          导出配置
        </el-button>
      </div>
    </el-card>

    <!-- 系统统计信息 -->
    <el-card class="statistics" style="margin-top: 20px;">
      <template #header>
        <span>系统统计</span>
      </template>
      
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-value">{{ statistics.roleCount }}</div>
            <div class="stat-label">角色总数</div>
          </div>
        </el-col>
        
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-value">{{ statistics.permissionCount }}</div>
            <div class="stat-label">权限总数</div>
          </div>
        </el-col>
        
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-value">{{ statistics.activeRoleCount }}</div>
            <div class="stat-label">启用角色</div>
          </div>
        </el-col>
        
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-value">{{ statistics.userCount }}</div>
            <div class="stat-label">用户总数</div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 权限变更日志 -->
    <el-card class="change-log" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <span>权限变更日志</span>
          <el-button type="text" @click="loadChangeLogs">
            <el-icon><Refresh /></el-icon>
          </el-button>
        </div>
      </template>
      
      <el-table :data="changeLogs" v-loading="logLoading" max-height="300">
        <el-table-column prop="action" label="操作类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getActionTag(row.action)">
              {{ getActionText(row.action) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="target" label="操作对象" min-width="150" />
        <el-table-column prop="operator" label="操作人" width="120" />
        <el-table-column prop="createTime" label="操作时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="200" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Setting, Refresh, Download } from '@element-plus/icons-vue'
import RoleManagement from '@/components/system/RoleManagement.vue'
import PermissionAssignment from '@/components/system/PermissionAssignment.vue'
import { formatTime } from '@/utils/format'

interface Role {
  id?: number
  name: string
  code: string
}

interface ChangeLog {
  id: number
  action: string
  target: string
  operator: string
  createTime: string
  description: string
}

interface Statistics {
  roleCount: number
  permissionCount: number
  activeRoleCount: number
  userCount: number
}

// 响应式数据
const currentRole = ref<Role | null>(null)
const logLoading = ref(false)

const statistics = reactive<Statistics>({
  roleCount: 0,
  permissionCount: 0,
  activeRoleCount: 0,
  userCount: 0
})

const changeLogs = ref<ChangeLog[]>([])

// 组件引用
const roleManagementRef = ref()
const permissionAssignmentRef = ref()

// 处理权限管理
const handleManagePermissions = (role: Role) => {
  currentRole.value = role
  permissionAssignmentRef.value?.assignRolePermissions(role)
}

// 刷新处理
const handleRefresh = () => {
  loadStatistics()
  loadChangeLogs()
}

// 快捷操作
const addRole = () => {
  roleManagementRef.value?.addRole()
}

const addPermission = () => {
  permissionAssignmentRef.value?.addPermission()
}

const refreshAll = () => {
  roleManagementRef.value?.loadRoles()
  permissionAssignmentRef.value?.loadPermissions()
  handleRefresh()
  ElMessage.success('数据刷新成功')
}

const exportData = () => {
  // TODO: 实现导出功能
  ElMessage.info('导出功能开发中')
}

// 加载统计信息
const loadStatistics = async () => {
  try {
    // TODO: 调用API获取统计信息
    Object.assign(statistics, {
      roleCount: 5,
      permissionCount: 25,
      activeRoleCount: 4,
      userCount: 100
    })
  } catch (error) {
    console.error('加载统计信息失败:', error)
  }
}

// 加载变更日志
const loadChangeLogs = async () => {
  try {
    logLoading.value = true
    // TODO: 调用API获取变更日志
    changeLogs.value = [
      {
        id: 1,
        action: 'create',
        target: '管理员角色',
        operator: '系统管理员',
        createTime: '2024-01-01 10:00:00',
        description: '创建管理员角色'
      },
      {
        id: 2,
        action: 'update',
        target: '文件管理权限',
        operator: '系统管理员',
        createTime: '2024-01-01 10:05:00',
        description: '修改文件管理权限描述'
      },
      {
        id: 3,
        action: 'assign',
        target: '普通用户角色',
        operator: '系统管理员',
        createTime: '2024-01-01 10:10:00',
        description: '为普通用户角色分配权限'
      }
    ]
  } catch (error) {
    console.error('加载变更日志失败:', error)
    ElMessage.error('加载变更日志失败')
  } finally {
    logLoading.value = false
  }
}

// 日志操作类型显示
const getActionText = (action: string) => {
  const actionMap = {
    create: '创建',
    update: '更新',
    delete: '删除',
    assign: '分配',
    revoke: '撤销'
  }
  return actionMap[action as keyof typeof actionMap] || action
}

const getActionTag = (action: string): 'success' | 'primary' | 'danger' | 'warning' | 'info' => {
  const tagMap = {
    create: 'success',
    update: 'primary',
    delete: 'danger',
    assign: 'warning',
    revoke: 'info'
  }
  return (tagMap[action as keyof typeof tagMap] || 'info') as 'success' | 'primary' | 'danger' | 'warning' | 'info'
}

// 初始化
onMounted(() => {
  loadStatistics()
  loadChangeLogs()
})
</script>

<style scoped lang="scss">
@import './PermissionManager.scss';

.permission-manager {
  .quick-actions {
    .action-buttons {
      display: flex;
      gap: $spacing-md;
      flex-wrap: wrap;
    }
  }

  .statistics {
    .stat-item {
      text-align: center;
      padding: $spacing-lg;
      
      .stat-value {
        font-size: 2em;
        font-weight: bold;
        color: $primary-color;
        margin-bottom: $spacing-sm;
      }
      
      .stat-label {
        color: $text-secondary;
        font-size: $font-size-small;
      }
    }
  }

  .change-log {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
}
</style>