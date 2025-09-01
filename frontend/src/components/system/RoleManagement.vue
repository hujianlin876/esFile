<template>
  <div class="role-management">
    <el-card class="role-card">
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <el-button type="primary" :icon="Plus" @click="showRoleDialog = true">
            添加角色
          </el-button>
        </div>
      </template>
      
      <el-table :data="roleList" v-loading="loading" style="width: 100%">
        <el-table-column prop="name" label="角色名称" min-width="120" />
        <el-table-column prop="code" label="角色编码" width="150" />
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'danger'">
              {{ row.status === 'active' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="text" size="small" @click="editRole(row)">
              编辑
            </el-button>
            <el-button type="text" size="small" @click="managePermissions(row)">
              权限设置
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="toggleRoleStatus(row)"
            >
              {{ row.status === 'active' ? '禁用' : '启用' }}
            </el-button>
            <el-button type="text" size="small" @click="deleteRole(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 角色编辑对话框 -->
    <el-dialog
      v-model="showRoleDialog"
      :title="isEdit ? '编辑角色' : '添加角色'"
      width="500px"
    >
      <el-form
        ref="roleFormRef"
        :model="roleForm"
        :rules="roleRules"
        label-width="80px"
      >
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="roleForm.name" placeholder="请输入角色名称" />
        </el-form-item>
        
        <el-form-item label="角色编码" prop="code">
          <el-input 
            v-model="roleForm.code" 
            placeholder="请输入角色编码"
            :disabled="isEdit"
          />
        </el-form-item>
        
        <el-form-item label="角色描述" prop="description">
          <el-input
            v-model="roleForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入角色描述"
          />
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="roleForm.status">
            <el-radio label="active">启用</el-radio>
            <el-radio label="inactive">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showRoleDialog = false">取消</el-button>
          <el-button type="primary" :loading="saving" @click="saveRole">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { formatTime } from '@/utils/format'

interface Role {
  id?: number
  name: string
  code: string
  description: string
  status: 'active' | 'inactive'
  createTime?: string
}

interface Emits {
  (e: 'manage-permissions', role: Role): void
  (e: 'refresh'): void
}

const emit = defineEmits<Emits>()

// 响应式数据
const roleList = ref<Role[]>([])
const loading = ref(false)
const saving = ref(false)
const showRoleDialog = ref(false)
const isEdit = ref(false)

const roleForm = reactive<Role>({
  name: '',
  code: '',
  description: '',
  status: 'active'
})

const roleFormRef = ref()

// 表单验证规则
const roleRules = {
  name: [
    { required: true, message: '请输入角色名称', trigger: 'blur' },
    { min: 2, max: 20, message: '角色名称长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入角色编码', trigger: 'blur' },
    { 
      pattern: /^[A-Z_][A-Z0-9_]*$/, 
      message: '角色编码只能包含大写字母、数字和下划线，且以大写字母或下划线开头', 
      trigger: 'blur' 
    }
  ],
  description: [
    { max: 200, message: '角色描述不能超过 200 个字符', trigger: 'blur' }
  ]
}

// 加载角色列表
const loadRoles = async () => {
  try {
    loading.value = true
    // TODO: 调用API获取角色列表
    roleList.value = [
      {
        id: 1,
        name: '管理员',
        code: 'ADMIN',
        description: '系统管理员，拥有所有权限',
        status: 'active',
        createTime: '2024-01-01 10:00:00'
      },
      {
        id: 2,
        name: '普通用户',
        code: 'USER',
        description: '普通用户，拥有基本权限',
        status: 'active',
        createTime: '2024-01-01 10:00:00'
      }
    ]
  } catch (error) {
    console.error('加载角色列表失败:', error)
    ElMessage.error('加载角色列表失败')
  } finally {
    loading.value = false
  }
}

// 添加角色
const addRole = () => {
  isEdit.value = false
  resetRoleForm()
  showRoleDialog.value = true
}

// 编辑角色
const editRole = (role: Role) => {
  isEdit.value = true
  Object.assign(roleForm, role)
  showRoleDialog.value = true
}

// 保存角色
const saveRole = async () => {
  if (!roleFormRef.value) return
  
  try {
    await roleFormRef.value.validate()
    saving.value = true
    
    // TODO: 调用API保存角色
    if (isEdit.value) {
      ElMessage.success('角色更新成功')
    } else {
      ElMessage.success('角色创建成功')
    }
    
    showRoleDialog.value = false
    emit('refresh')
    loadRoles()
  } catch (error) {
    console.error('保存角色失败:', error)
    if (error !== false) { // 验证失败时不显示错误消息
      ElMessage.error('保存角色失败')
    }
  } finally {
    saving.value = false
  }
}

// 删除角色
const deleteRole = async (role: Role) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除角色 "${role.name}" 吗？`,
      '确认删除',
      { type: 'warning' }
    )
    
    // TODO: 调用API删除角色
    ElMessage.success('角色删除成功')
    emit('refresh')
    loadRoles()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除角色失败:', error)
      ElMessage.error('删除角色失败')
    }
  }
}

// 切换角色状态
const toggleRoleStatus = async (role: Role) => {
  try {
    const action = role.status === 'active' ? '禁用' : '启用'
    await ElMessageBox.confirm(
      `确定要${action}角色 "${role.name}" 吗？`,
      `确认${action}`,
      { type: 'warning' }
    )
    
    // TODO: 调用API切换状态
    role.status = role.status === 'active' ? 'inactive' : 'active'
    ElMessage.success(`角色${action}成功`)
    emit('refresh')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('切换角色状态失败:', error)
      ElMessage.error('操作失败')
    }
  }
}

// 管理权限
const managePermissions = (role: Role) => {
  emit('manage-permissions', role)
}

// 重置表单
const resetRoleForm = () => {
  Object.assign(roleForm, {
    name: '',
    code: '',
    description: '',
    status: 'active'
  })
  roleFormRef.value?.clearValidate()
}

// 初始化
onMounted(() => {
  loadRoles()
})

// 暴露方法
defineExpose({
  loadRoles,
  addRole
})
</script>

<style scoped lang="scss">
@import '@/assets/styles/variables.scss';

.role-management {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: $spacing-sm;
  }
}
</style>

