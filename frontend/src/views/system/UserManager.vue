<template>
  <div class="user-manager">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">用户管理</h1>
      <p class="page-desc">管理系统用户，设置角色和权限</p>
    </div>
    
    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button type="primary" :icon="Plus" @click="showAddDialog = true">
          添加用户
        </el-button>
        <el-button :icon="Refresh" @click="loadUsers">刷新</el-button>
      </div>
      
      <div class="toolbar-right">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索用户名、邮箱..."
          style="width: 250px"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
      </div>
    </div>
    
    <!-- 用户列表 -->
    <el-card class="user-table-card">
      <el-table
        :data="filteredUsers"
        v-loading="loading"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column label="头像" width="80">
          <template #default="{ row }">
            <el-avatar :size="40" :src="row.avatar">
              {{ getUserInitials(row.nickname || row.username) }}
            </el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="nickname" label="昵称" min-width="120" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'danger'">
              {{ row.status === 'active' ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="角色" width="120">
          <template #default="{ row }">
            <el-tag v-for="role in row.roles" :key="role.id" size="small" class="role-tag">
              {{ role.name }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastLoginTime" label="最后登录" width="180">
          <template #default="{ row }">
            {{ row.lastLoginTime ? formatTime(row.lastLoginTime) : '从未登录' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="text" size="small" @click="editUser(row)">
              编辑
            </el-button>
            <el-button type="text" size="small" @click="resetPassword(row)">
              重置密码
            </el-button>
            <el-button
              type="text"
              size="small"
              :type="row.status === 'active' ? 'danger' : 'success'"
              @click="toggleUserStatus(row)"
            >
              {{ row.status === 'active' ? '禁用' : '启用' }}
            </el-button>
            <el-button type="text" size="small" @click="deleteUser(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 添加/编辑用户对话框 -->
    <el-dialog
      v-model="showUserDialog"
      :title="isEdit ? '编辑用户' : '添加用户'"
      width="600px"
    >
      <el-form
        ref="userFormRef"
        :model="userForm"
        :rules="userRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="userForm.username" :disabled="isEdit" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="userForm.nickname" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="userForm.email" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="userForm.phone" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="userForm.gender" placeholder="请选择性别">
                <el-option label="男" value="male" />
                <el-option label="女" value="female" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生日" prop="birthday">
              <el-date-picker
                v-model="userForm.birthday"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="角色" prop="roleIds">
          <el-select
            v-model="userForm.roleIds"
            multiple
            placeholder="请选择角色"
            style="width: 100%"
          >
            <el-option
              v-for="role in roleList"
              :key="role.id"
              :label="role.name"
              :value="role.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="userForm.status">
            <el-radio label="active">正常</el-radio>
            <el-radio label="inactive">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item v-if="!isEdit" label="密码" prop="password">
          <el-input v-model="userForm.password" type="password" show-password />
        </el-form-item>
        
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="userForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showUserDialog = false">取消</el-button>
          <el-button type="primary" @click="saveUser" :loading="saving">
            {{ saving ? '保存中...' : '保存' }}
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import { formatTime } from '@/utils/format'
import type { FormInstance, FormRules } from 'element-plus'
import type { User, Role } from '@/api/types/auth'

// 响应式数据
const loading = ref(false)
const saving = ref(false)
const showUserDialog = ref(false)
const isEdit = ref(false)
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

const userList = ref<User[]>([])
const roleList = ref<Role[]>([])
const selectedUsers = ref<User[]>([])

const userForm = reactive({
  id: '',
  username: '',
  nickname: '',
  email: '',
  phone: '',
  gender: '',
  birthday: '',
  roleIds: [] as number[],
  status: 'active',
  password: '',
  remark: ''
})

// 表单验证规则
const userRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  roleIds: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ]
}

// 计算属性
const filteredUsers = computed(() => {
  if (!searchKeyword.value) return userList.value
  
  const keyword = searchKeyword.value.toLowerCase()
  return userList.value.filter(user => 
    user.username.toLowerCase().includes(keyword) ||
    user.nickname?.toLowerCase().includes(keyword) ||
    user.email.toLowerCase().includes(keyword)
  )
})

// 引用
const userFormRef = ref<FormInstance>()

// 方法
const loadUsers = async () => {
  loading.value = true
  try {
    // TODO: 调用真实API
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 模拟数据
    userList.value = [
      {
        id: 1,
        username: 'admin',
        nickname: '管理员',
        email: 'admin@example.com',
        phone: '13800138000',
        gender: 'male',
        birthday: '1990-01-01',
        status: 'active',
        avatar: '',
        lastLoginTime: new Date().toISOString(),
        roles: [{ id: 1, name: '超级管理员' }]
      },
      {
        id: 2,
        username: 'user1',
        nickname: '普通用户',
        email: 'user1@example.com',
        phone: '13800138001',
        gender: 'female',
        birthday: '1995-05-05',
        status: 'active',
        avatar: '',
        lastLoginTime: new Date(Date.now() - 86400000).toISOString(),
        roles: [{ id: 2, name: '普通用户' }]
      }
    ]
    
    total.value = userList.value.length
  } catch (error) {
    ElMessage.error('加载用户列表失败')
  } finally {
    loading.value = false
  }
}

const loadRoles = async () => {
  try {
    // TODO: 调用真实API
    roleList.value = [
      { id: 1, name: '超级管理员', code: 'SUPER_ADMIN' },
      { id: 2, name: '普通用户', code: 'USER' },
      { id: 3, name: '文件管理员', code: 'FILE_ADMIN' }
    ]
  } catch (error) {
    ElMessage.error('加载角色列表失败')
  }
}

const handleSearch = () => {
  currentPage.value = 1
  // 搜索逻辑已在计算属性中处理
}

const handleSelectionChange = (selection: User[]) => {
  selectedUsers.value = selection
}

const addUser = () => {
  isEdit.value = false
  resetUserForm()
  showUserDialog.value = true
}

const editUser = (user: User) => {
  isEdit.value = true
  Object.assign(userForm, {
    id: user.id,
    username: user.username,
    nickname: user.nickname,
    email: user.email,
    phone: user.phone,
    gender: user.gender,
    birthday: user.birthday,
    roleIds: user.roles.map(role => role.id),
    status: user.status,
    remark: ''
  })
  showUserDialog.value = true
}

const resetUserForm = () => {
  Object.assign(userForm, {
    id: '',
    username: '',
    nickname: '',
    email: '',
    phone: '',
    gender: '',
    birthday: '',
    roleIds: [],
    status: 'active',
    password: '',
    remark: ''
  })
}

const saveUser = async () => {
  if (!userFormRef.value) return
  
  try {
    await userFormRef.value.validate()
    saving.value = true
    
    // TODO: 调用真实API
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success(isEdit.value ? '用户更新成功' : '用户添加成功')
    showUserDialog.value = false
    loadUsers()
  } catch (error) {
    // 验证失败
  } finally {
    saving.value = false
  }
}

const resetPassword = async (user: User) => {
  try {
    await ElMessageBox.confirm(
      `确定要重置用户 "${user.username}" 的密码吗？`,
      '确认重置密码',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // TODO: 调用重置密码API
    ElMessage.success('密码重置成功')
  } catch {
    // 用户取消
  }
}

const toggleUserStatus = async (user: User) => {
  const action = user.status === 'active' ? '禁用' : '启用'
  try {
    await ElMessageBox.confirm(
      `确定要${action}用户 "${user.username}" 吗？`,
      `确认${action}`,
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // TODO: 调用更新状态API
    user.status = user.status === 'active' ? 'inactive' : 'active'
    ElMessage.success(`用户${action}成功`)
  } catch {
    // 用户取消
  }
}

const deleteUser = async (user: User) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户 "${user.username}" 吗？此操作不可恢复。`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // TODO: 调用删除用户API
    const index = userList.value.findIndex(u => u.id === user.id)
    if (index > -1) {
      userList.value.splice(index, 1)
      total.value--
    }
    ElMessage.success('用户删除成功')
  } catch {
    // 用户取消
  }
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
}

const getUserInitials = (name: string) => {
  if (!name) return 'U'
  return name.charAt(0).toUpperCase()
}

// 生命周期
onMounted(async () => {
  await Promise.all([loadUsers(), loadRoles()])
})
</script>

<style scoped lang="scss">
.user-manager {
  .page-header {
    margin-bottom: $spacing-xl;
    
    .page-title {
      font-size: $font-size-extra-large;
      font-weight: bold;
      color: $text-primary;
      margin-bottom: $spacing-sm;
    }
    
    .page-desc {
      color: $text-secondary;
      margin: 0;
    }
  }
  
  .toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: $spacing-lg;
    
    .toolbar-left,
    .toolbar-right {
      display: flex;
      align-items: center;
      gap: $spacing-md;
    }
  }
  
  .user-table-card {
    .role-tag {
      margin-right: $spacing-xs;
      
      &:last-child {
        margin-right: 0;
      }
    }
    
    .pagination-wrapper {
      display: flex;
      justify-content: center;
      margin-top: $spacing-lg;
    }
  }
}

// 响应式设计
@media (max-width: $breakpoint-md) {
  .user-manager {
    .toolbar {
      flex-direction: column;
      gap: $spacing-md;
      align-items: stretch;
      
      .toolbar-left,
      .toolbar-right {
        justify-content: center;
      }
    }
  }
}
</style>
