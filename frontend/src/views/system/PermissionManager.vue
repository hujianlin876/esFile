<template>
  <div class="permission-manager">
    <div class="page-header">
      <h1 class="page-title">权限管理</h1>
      <p class="page-desc">管理系统角色和权限，控制用户访问权限</p>
    </div>
    
    <!-- 角色管理 -->
    <el-card class="role-card" style="margin-bottom: 20px;">
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <el-button type="primary" :icon="Plus" @click="showRoleDialog = true">
            添加角色
          </el-button>
        </div>
      </template>
      
      <el-table :data="roleList" v-loading="roleLoading" style="width: 100%">
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
              :type="row.status === 'active' ? 'danger' : 'success'"
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
    
    <!-- 权限列表 -->
    <el-card class="permission-card">
      <template #header>
        <div class="card-header">
          <span>权限列表</span>
          <el-button type="primary" :icon="Plus" @click="showPermissionDialog = true">
            添加权限
          </el-button>
        </div>
      </template>
      
      <el-table :data="permissionList" v-loading="permissionLoading" style="width: 100%">
        <el-table-column prop="name" label="权限名称" min-width="120" />
        <el-table-column prop="code" label="权限编码" width="150" />
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getPermissionTypeColor(row.type)">
              {{ getPermissionTypeLabel(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路径" min-width="150" />
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'danger'">
              {{ row.status === 'active' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="text" size="small" @click="editPermission(row)">
              编辑
            </el-button>
            <el-button
              type="text"
              size="small"
              :type="row.status === 'active' ? 'danger' : 'success'"
              @click="togglePermissionStatus(row)"
            >
              {{ row.status === 'active' ? '禁用' : '启用' }}
            </el-button>
            <el-button type="text" size="small" @click="deletePermission(row)">
              删除
            </el-button>
        </template>
      </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 角色对话框 -->
    <el-dialog
      v-model="showRoleDialog"
      :title="isEditRole ? '编辑角色' : '添加角色'"
      width="500px"
    >
      <el-form
        ref="roleFormRef"
        :model="roleForm"
        :rules="roleRules"
        label-width="100px"
      >
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="roleForm.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="roleForm.code" placeholder="请输入角色编码" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
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
          <el-button type="primary" @click="saveRole" :loading="saving">
            {{ saving ? '保存中...' : '保存' }}
          </el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 权限对话框 -->
    <el-dialog
      v-model="showPermissionDialog"
      :title="isEditPermission ? '编辑权限' : '添加权限'"
      width="500px"
    >
      <el-form
        ref="permissionFormRef"
        :model="permissionForm"
        :rules="permissionRules"
        label-width="100px"
      >
        <el-form-item label="权限名称" prop="name">
          <el-input v-model="permissionForm.name" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="权限编码" prop="code">
          <el-input v-model="permissionForm.code" placeholder="请输入权限编码" />
        </el-form-item>
        <el-form-item label="权限类型" prop="type">
          <el-select v-model="permissionForm.type" placeholder="请选择权限类型">
            <el-option label="菜单权限" value="menu" />
            <el-option label="功能权限" value="function" />
            <el-option label="数据权限" value="data" />
          </el-select>
        </el-form-item>
        <el-form-item label="路径" prop="path">
          <el-input v-model="permissionForm.path" placeholder="请输入权限路径" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="permissionForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入权限描述"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="permissionForm.status">
            <el-radio label="active">启用</el-radio>
            <el-radio label="inactive">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showPermissionDialog = false">取消</el-button>
          <el-button type="primary" @click="savePermission" :loading="saving">
            {{ saving ? '保存中...' : '保存' }}
          </el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 权限设置对话框 -->
    <el-dialog
      v-model="showPermissionSettingDialog"
      title="权限设置"
      width="600px"
    >
      <div class="permission-setting">
        <h4>角色：{{ currentRole?.name }}</h4>
        <el-tree
          ref="permissionTreeRef"
          :data="permissionTreeData"
          :props="treeProps"
          show-checkbox
          node-key="id"
          :default-checked-keys="checkedPermissions"
        />
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showPermissionSettingDialog = false">取消</el-button>
          <el-button type="primary" @click="saveRolePermissions" :loading="saving">
            {{ saving ? '保存中...' : '保存' }}
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
import { getRoleList, createRole, updateRole, deleteRole, getPermissionList, createPermission, updatePermission, deletePermission, getRolePermissions, updateRolePermissions } from '@/api/system/permission'
import type { FormInstance, FormRules } from 'element-plus'
import type { Role, Permission } from '@/api/types/auth'

// 响应式数据
const roleLoading = ref(false)
const permissionLoading = ref(false)
const saving = ref(false)

const showRoleDialog = ref(false)
const showPermissionDialog = ref(false)
const showPermissionSettingDialog = ref(false)

const isEditRole = ref(false)
const isEditPermission = ref(false)

const roleList = ref<Role[]>([])
const permissionList = ref<Permission[]>([])
const currentRole = ref<Role | null>(null)
const checkedPermissions = ref<number[]>([])

const roleForm = reactive({
  id: '',
  name: '',
  code: '',
  description: '',
  status: 'active'
})

const permissionForm = reactive({
  id: '',
  name: '',
  code: '',
  type: 'function',
  path: '',
  description: '',
  status: 'active'
})

// 表单验证规则
const roleRules: FormRules = {
  name: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入角色编码', trigger: 'blur' }
  ]
}

const permissionRules: FormRules = {
  name: [
    { required: true, message: '请输入权限名称', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入权限编码', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择权限类型', trigger: 'change' }
  ]
}

// 树形配置
const treeProps = {
  children: 'children',
  label: 'label'
}

const permissionTreeData = ref<any[]>([])

// 引用
const roleFormRef = ref<FormInstance>()
const permissionFormRef = ref<FormInstance>()
const permissionTreeRef = ref()

// 方法
const loadRoles = async () => {
  roleLoading.value = true
  try {
    const response = await getRoleList()
    roleList.value = response.data
  } catch (error) {
    ElMessage.error('加载角色列表失败')
  } finally {
    roleLoading.value = false
  }
}

const loadPermissions = async () => {
  permissionLoading.value = true
  try {
    const response = await getPermissionList()
    permissionList.value = response.data
    buildPermissionTree()
  } catch (error) {
    ElMessage.error('加载权限列表失败')
  } finally {
    permissionLoading.value = false
  }
}

const buildPermissionTree = () => {
  // 构建权限树形结构
  const treeMap = new Map()
  
  permissionList.value.forEach(permission => {
    if (permission.parentId) {
      if (!treeMap.has(permission.parentId)) {
        treeMap.set(permission.parentId, { id: permission.parentId, label: permission.parentName, children: [] })
      }
      treeMap.get(permission.parentId).children.push({
        id: permission.id,
        label: permission.name
      })
    } else {
      treeMap.set(permission.id, { id: permission.id, label: permission.name, children: [] })
    }
  })
  
  permissionTreeData.value = Array.from(treeMap.values())
}

const addRole = () => {
  isEditRole.value = false
  resetRoleForm()
  showRoleDialog.value = true
}

const editRole = (role: Role) => {
  isEditRole.value = true
  Object.assign(roleForm, role)
  showRoleDialog.value = true
}

const resetRoleForm = () => {
  Object.assign(roleForm, {
    id: '',
    name: '',
    code: '',
    description: '',
    status: 'active'
  })
}

const saveRole = async () => {
  if (!roleFormRef.value) return
  
  try {
    await roleFormRef.value.validate()
    saving.value = true
    
    if (isEditRole.value) {
      await updateRole(roleForm.id, roleForm)
      ElMessage.success('角色更新成功')
    } else {
      await createRole(roleForm)
      ElMessage.success('角色添加成功')
    }
    
    showRoleDialog.value = false
    loadRoles()
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

const deleteRole = async (role: Role) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除角色 "${role.name}" 吗？此操作不可恢复。`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await deleteRole(role.id)
    ElMessage.success('角色删除成功')
    loadRoles()
  } catch {
    // 用户取消
  }
}

const toggleRoleStatus = async (role: Role) => {
  const action = role.status === 'active' ? '禁用' : '启用'
  try {
    await ElMessageBox.confirm(
      `确定要${action}角色 "${role.name}" 吗？`,
      `确认${action}`,
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await updateRole(role.id, { status: role.status === 'active' ? 'inactive' : 'active' })
    ElMessage.success(`角色${action}成功`)
    loadRoles()
  } catch {
    // 用户取消
  }
}

const addPermission = () => {
  isEditPermission.value = false
  resetPermissionForm()
  showPermissionDialog.value = true
}

const editPermission = (permission: Permission) => {
  isEditPermission.value = true
  Object.assign(permissionForm, permission)
  showPermissionDialog.value = true
}

const resetPermissionForm = () => {
  Object.assign(permissionForm, {
    id: '',
    name: '',
    code: '',
    type: 'function',
    path: '',
    description: '',
    status: 'active'
  })
}

const savePermission = async () => {
  if (!permissionFormRef.value) return
  
  try {
    await permissionFormRef.value.validate()
    saving.value = true
    
    if (isEditPermission.value) {
      await updatePermission(permissionForm.id, permissionForm)
      ElMessage.success('权限更新成功')
    } else {
      await createPermission(permissionForm)
      ElMessage.success('权限添加成功')
    }
    
    showPermissionDialog.value = false
    loadPermissions()
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

const deletePermission = async (permission: Permission) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除权限 "${permission.name}" 吗？此操作不可恢复。`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await deletePermission(permission.id)
    ElMessage.success('权限删除成功')
    loadPermissions()
  } catch {
    // 用户取消
  }
}

const togglePermissionStatus = async (permission: Permission) => {
  const action = permission.status === 'active' ? '禁用' : '启用'
  try {
    await ElMessageBox.confirm(
      `确定要${action}权限 "${permission.name}" 吗？`,
      `确认${action}`,
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await updatePermission(permission.id, { status: permission.status === 'active' ? 'inactive' : 'active' })
    ElMessage.success(`权限${action}成功`)
    loadPermissions()
  } catch {
    // 用户取消
  }
}

const managePermissions = async (role: Role) => {
  currentRole.value = role
  try {
    const response = await getRolePermissions(role.id)
    checkedPermissions.value = response.data.map((p: any) => p.permissionId)
    showPermissionSettingDialog.value = true
  } catch (error) {
    ElMessage.error('获取角色权限失败')
  }
}

const saveRolePermissions = async () => {
  if (!permissionTreeRef.value || !currentRole.value) return
  
  try {
    saving.value = true
    const checkedKeys = permissionTreeRef.value.getCheckedKeys()
    const halfCheckedKeys = permissionTreeRef.value.getHalfCheckedKeys()
    
    await updateRolePermissions(currentRole.value.id, {
      permissionIds: [...checkedKeys, ...halfCheckedKeys]
    })
    
    ElMessage.success('权限设置保存成功')
    showPermissionSettingDialog.value = false
  } catch (error) {
    ElMessage.error('权限设置保存失败')
  } finally {
    saving.value = false
  }
}

const getPermissionTypeColor = (type: string) => {
  const colorMap: Record<string, string> = {
    menu: 'primary',
    function: 'success',
    data: 'warning'
  }
  return colorMap[type] || 'info'
}

const getPermissionTypeLabel = (type: string) => {
  const labelMap: Record<string, string> = {
    menu: '菜单权限',
    function: '功能权限',
    data: '数据权限'
  }
  return labelMap[type] || '未知'
}

// 生命周期
onMounted(async () => {
  await Promise.all([loadRoles(), loadPermissions()])
})
</script>

<style lang="scss" scoped>
@import './PermissionManager.scss';
</style>
