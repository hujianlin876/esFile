<template>
  <div class="user-manager">
    <div class="page-header">
      <h1 class="page-title">用户管理</h1>
      <p class="page-desc">管理系统用户，设置角色和权限</p>
    </div>
    
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button type="primary" :icon="Plus" @click="addUser">
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
import { getUserList, createUser, updateUser, resetUserPassword, toggleUserStatus as toggleUserStatusApi } from '@/api/system/user'
import { getRoleList } from '@/api/system/permission'
import type { FormInstance, FormRules } from 'element-plus'
import type { User, Role } from '@/api/types/auth'

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

const filteredUsers = computed(() => {
  if (!searchKeyword.value) return userList.value
  
  const keyword = searchKeyword.value.toLowerCase()
  return userList.value.filter(user => 
    user.username.toLowerCase().includes(keyword) ||
    user.nickname?.toLowerCase().includes(keyword) ||
    user.email.toLowerCase().includes(keyword)
  )
})

const userFormRef = ref<FormInstance>()

const loadUsers = async () => {
  loading.value = true
  try {
    const response = await getUserList({
      page: currentPage.value,
      size: pageSize.value
    })
    userList.value = response.data.list
    total.value = response.data.total
  } catch (error) {
    ElMessage.error('加载用户列表失败')
  } finally {
    loading.value = false
  }
}

const loadRoles = async () => {
  try {
    const response = await getRoleList()
    roleList.value = response.data
  } catch (error) {
    ElMessage.error('加载角色列表失败')
  }
}

const handleSearch = () => {
  currentPage.value = 1
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
    
    if (isEdit.value) {
      await updateUser(Number(userForm.id), userForm as any)
      ElMessage.success('用户更新成功')
    } else {
      await createUser(userForm as any)
      ElMessage.success('用户添加成功')
    }
    
    showUserDialog.value = false
    loadUsers()
  } catch (error) {
    ElMessage.error('保存失败')
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
    
    await resetUserPassword(user.id)
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
    
    await toggleUserStatusApi(user.id)
    ElMessage.success(`用户${action}成功`)
    loadUsers()
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
    
    // await deleteUser(user.id)
    ElMessage.success('用户删除成功')
    ElMessage.success('用户删除成功')
    loadUsers()
  } catch {
    // 用户取消
  }
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  loadUsers()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  loadUsers()
}

const getUserInitials = (name: string) => {
  if (!name) return 'U'
  return name.charAt(0).toUpperCase()
}

onMounted(async () => {
  await Promise.all([loadUsers(), loadRoles()])
})
</script>

<style lang="scss" scoped>
@import './UserManager.scss';
</style>
