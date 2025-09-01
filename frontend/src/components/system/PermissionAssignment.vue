<template>
  <div class="permission-assignment">
    <el-card class="permission-card">
      <template #header>
        <div class="card-header">
          <span>权限列表</span>
          <el-button type="primary" :icon="Plus" @click="showPermissionDialog = true">
            添加权限
          </el-button>
        </div>
      </template>
      
      <el-table :data="permissionList" v-loading="loading" style="width: 100%">
        <el-table-column prop="name" label="权限名称" min-width="150" />
        <el-table-column prop="code" label="权限编码" width="200" />
        <el-table-column prop="type" label="权限类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getPermissionTypeTag(row.type)">
              {{ getPermissionTypeText(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="resource" label="资源" width="150" />
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="text" size="small" @click="editPermission(row)">
              编辑
            </el-button>
            <el-button type="text" size="small" @click="deletePermission(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 权限编辑对话框 -->
    <el-dialog
      v-model="showPermissionDialog"
      :title="isEdit ? '编辑权限' : '添加权限'"
      width="500px"
    >
      <el-form
        ref="permissionFormRef"
        :model="permissionForm"
        :rules="permissionRules"
        label-width="80px"
      >
        <el-form-item label="权限名称" prop="name">
          <el-input v-model="permissionForm.name" placeholder="请输入权限名称" />
        </el-form-item>
        
        <el-form-item label="权限编码" prop="code">
          <el-input 
            v-model="permissionForm.code" 
            placeholder="请输入权限编码"
            :disabled="isEdit"
          />
        </el-form-item>
        
        <el-form-item label="权限类型" prop="type">
          <el-select v-model="permissionForm.type" placeholder="请选择权限类型" style="width: 100%">
            <el-option label="菜单权限" value="menu" />
            <el-option label="按钮权限" value="button" />
            <el-option label="API权限" value="api" />
            <el-option label="数据权限" value="data" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="资源路径" prop="resource">
          <el-input v-model="permissionForm.resource" placeholder="请输入资源路径" />
        </el-form-item>
        
        <el-form-item label="父级权限" prop="parentId">
          <el-tree-select
            v-model="permissionForm.parentId"
            :data="permissionTreeData"
            placeholder="请选择父级权限"
            style="width: 100%"
            clearable
            :props="{ label: 'name', value: 'id' }"
          />
        </el-form-item>
        
        <el-form-item label="权限描述" prop="description">
          <el-input
            v-model="permissionForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入权限描述"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showPermissionDialog = false">取消</el-button>
          <el-button type="primary" :loading="saving" @click="savePermission">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 角色权限分配对话框 -->
    <el-dialog
      v-model="showRolePermissionDialog"
      :title="`分配权限 - ${currentRole?.name}`"
      width="600px"
    >
      <div class="permission-tree-container">
        <el-tree
          ref="permissionTreeRef"
          :data="permissionTreeData"
          :props="{ label: 'name', children: 'children' }"
          show-checkbox
          node-key="id"
          :default-checked-keys="rolePermissions"
          :check-strictly="false"
        />
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showRolePermissionDialog = false">取消</el-button>
          <el-button type="primary" :loading="saving" @click="saveRolePermissions">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

interface Permission {
  id?: number
  name: string
  code: string
  type: 'menu' | 'button' | 'api' | 'data'
  resource: string
  parentId?: number
  description: string
  children?: Permission[]
}

interface Role {
  id?: number
  name: string
  code: string
}

interface Props {
  currentRole?: Role | null
}

interface Emits {
  (e: 'refresh'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// 响应式数据
const permissionList = ref<Permission[]>([])
const permissionTreeData = ref<Permission[]>([])
const rolePermissions = ref<number[]>([])
const loading = ref(false)
const saving = ref(false)
const showPermissionDialog = ref(false)
const showRolePermissionDialog = ref(false)
const isEdit = ref(false)

const permissionForm = reactive<Permission>({
  name: '',
  code: '',
  type: 'menu',
  resource: '',
  description: ''
})

const permissionFormRef = ref()
const permissionTreeRef = ref()

// 表单验证规则
const permissionRules = {
  name: [
    { required: true, message: '请输入权限名称', trigger: 'blur' },
    { min: 2, max: 30, message: '权限名称长度在 2 到 30 个字符', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入权限编码', trigger: 'blur' },
    { 
      pattern: /^[a-z_][a-z0-9_:]*$/, 
      message: '权限编码只能包含小写字母、数字、下划线和冒号，且以小写字母或下划线开头', 
      trigger: 'blur' 
    }
  ],
  type: [
    { required: true, message: '请选择权限类型', trigger: 'change' }
  ],
  resource: [
    { required: true, message: '请输入资源路径', trigger: 'blur' }
  ]
}

// 计算属性
const currentRole = computed(() => props.currentRole)

// 权限类型显示
const getPermissionTypeText = (type: string) => {
  const typeMap = {
    menu: '菜单',
    button: '按钮',
    api: 'API',
    data: '数据'
  }
  return typeMap[type as keyof typeof typeMap] || type
}

const getPermissionTypeTag = (type: string): 'primary' | 'success' | 'warning' | 'info' | 'danger' => {
  const tagMap = {
    menu: 'primary',
    button: 'success',
    api: 'warning',
    data: 'info'
  }
  return (tagMap[type as keyof typeof tagMap] || 'danger') as 'primary' | 'success' | 'warning' | 'info' | 'danger'
}

// 加载权限列表
const loadPermissions = async () => {
  try {
    loading.value = true
    // TODO: 调用API获取权限列表
    const mockPermissions: Permission[] = [
      {
        id: 1,
        name: '文件管理',
        code: 'file:manage',
        type: 'menu',
        resource: '/files',
        description: '文件管理菜单权限'
      },
      {
        id: 2,
        name: '文件上传',
        code: 'file:upload',
        type: 'button',
        resource: '/api/files/upload',
        description: '文件上传权限'
      },
      {
        id: 3,
        name: '文件下载',
        code: 'file:download',
        type: 'api',
        resource: '/api/files/*/download',
        description: '文件下载API权限'
      }
    ]
    
    permissionList.value = mockPermissions
    buildPermissionTree()
  } catch (error) {
    console.error('加载权限列表失败:', error)
    ElMessage.error('加载权限列表失败')
  } finally {
    loading.value = false
  }
}

// 构建权限树
const buildPermissionTree = () => {
  // TODO: 实现权限树构建逻辑
  permissionTreeData.value = permissionList.value.map(permission => ({
    ...permission,
    children: []
  }))
}

// 添加权限
const addPermission = () => {
  isEdit.value = false
  resetPermissionForm()
  showPermissionDialog.value = true
}

// 编辑权限
const editPermission = (permission: Permission) => {
  isEdit.value = true
  Object.assign(permissionForm, permission)
  showPermissionDialog.value = true
}

// 保存权限
const savePermission = async () => {
  if (!permissionFormRef.value) return
  
  try {
    await permissionFormRef.value.validate()
    saving.value = true
    
    // TODO: 调用API保存权限
    if (isEdit.value) {
      ElMessage.success('权限更新成功')
    } else {
      ElMessage.success('权限创建成功')
    }
    
    showPermissionDialog.value = false
    emit('refresh')
    loadPermissions()
  } catch (error) {
    console.error('保存权限失败:', error)
    if (error !== false) {
      ElMessage.error('保存权限失败')
    }
  } finally {
    saving.value = false
  }
}

// 删除权限
const deletePermission = async (permission: Permission) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除权限 "${permission.name}" 吗？`,
      '确认删除',
      { type: 'warning' }
    )
    
    // TODO: 调用API删除权限
    ElMessage.success('权限删除成功')
    emit('refresh')
    loadPermissions()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除权限失败:', error)
      ElMessage.error('删除权限失败')
    }
  }
}

// 分配角色权限
const assignRolePermissions = async (role: Role) => {
  try {
    // TODO: 加载角色的权限
    rolePermissions.value = [1, 2] // 模拟数据
    showRolePermissionDialog.value = true
  } catch (error) {
    console.error('加载角色权限失败:', error)
    ElMessage.error('加载角色权限失败')
  }
}

// 保存角色权限
const saveRolePermissions = async () => {
  if (!permissionTreeRef.value || !currentRole.value) return
  
  try {
    saving.value = true
    const checkedKeys = permissionTreeRef.value.getCheckedKeys()
    
    // TODO: 调用API保存角色权限
    ElMessage.success('权限分配成功')
    showRolePermissionDialog.value = false
    emit('refresh')
  } catch (error) {
    console.error('保存角色权限失败:', error)
    ElMessage.error('保存角色权限失败')
  } finally {
    saving.value = false
  }
}

// 重置表单
const resetPermissionForm = () => {
  Object.assign(permissionForm, {
    name: '',
    code: '',
    type: 'menu',
    resource: '',
    description: ''
  })
  permissionFormRef.value?.clearValidate()
}

// 初始化
onMounted(() => {
  loadPermissions()
})

// 暴露方法
defineExpose({
  loadPermissions,
  addPermission,
  assignRolePermissions
})
</script>

<style scoped lang="scss">
@import '@/assets/styles/variables.scss';

.permission-assignment {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .permission-tree-container {
    max-height: 400px;
    overflow-y: auto;
    border: 1px solid $border-light;
    border-radius: 4px;
    padding: $spacing-md;
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: $spacing-sm;
  }
}
</style>

