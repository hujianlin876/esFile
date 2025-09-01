<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h1 class="login-title">ES文件管理系统</h1>
        <p class="login-subtitle">Enterprise File Management System</p>
      </div>
      
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        @submit.prevent="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            size="large"
            :prefix-icon="User"
            clearable
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            :prefix-icon="Lock"
            show-password
            clearable
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <el-checkbox v-model="loginForm.rememberMe">
            记住密码
          </el-checkbox>
          <el-link type="primary" class="forgot-password">
            忘记密码？
          </el-link>
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-button"
            :loading="loading"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>
        
        <el-form-item class="register-link">
          <span>还没有账号？</span>
          <el-link type="primary" @click="goToRegister">
            立即注册
          </el-link>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/modules/auth'
import type { FormInstance, FormRules } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore()

// 响应式数据
const loginFormRef = ref<FormInstance>()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: '',
  rememberMe: false
})

// 表单验证规则
const loginRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

// 方法
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
    loading.value = true
    
    const success = await authStore.login({
      username: loginForm.username,
      password: loginForm.password
    })
    
    if (success) {
      ElMessage.success('登录成功')
      router.push('/')
    } else {
      ElMessage.error('登录失败：用户名或密码错误')
    }
  } catch (error: any) {
    if (error.message) {
      ElMessage.error(error.message)
    }
  } finally {
    loading.value = false
  }
}

const goToRegister = () => {
  router.push('/register')
}

// 生命周期
onMounted(() => {
  // 检查是否已登录
  if (authStore.isAuthenticated) {
    router.push('/')
  }
  
  // 从localStorage恢复用户名
  const savedUsername = localStorage.getItem('rememberedUsername')
  if (savedUsername) {
    loginForm.username = savedUsername
    loginForm.rememberMe = true
  }
})
</script>

<style scoped lang="scss">
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: $spacing-lg;
}

.login-box {
  width: 100%;
  max-width: 400px;
  background: $background-white;
  border-radius: $border-radius-large;
  box-shadow: $box-shadow-dark;
  padding: $spacing-xxl;
}

.login-header {
  text-align: center;
  margin-bottom: $spacing-xl;
  
  .login-title {
    font-size: $font-size-extra-large;
    font-weight: bold;
    color: $text-primary;
    margin-bottom: $spacing-sm;
  }
  
  .login-subtitle {
    font-size: $font-size-base;
    color: $text-secondary;
    margin: 0;
  }
}

.login-form {
  .el-form-item {
    margin-bottom: $spacing-lg;
  }
  
  .el-input {
    .el-input__wrapper {
      border-radius: $border-radius-base;
      box-shadow: 0 0 0 1px $border-color inset;
      
      &:hover {
        box-shadow: 0 0 0 1px $primary-color inset;
      }
      
      &.is-focus {
        box-shadow: 0 0 0 1px $primary-color inset;
      }
    }
  }
  
  .forgot-password {
    float: right;
    font-size: $font-size-small;
  }
  
  .login-button {
    width: 100%;
    border-radius: $border-radius-base;
    font-size: $font-size-medium;
    font-weight: $font-weight-primary;
    height: 44px;
  }
  
  .register-link {
    text-align: center;
    margin-bottom: 0;
    font-size: $font-size-small;
    
    .el-link {
      margin-left: $spacing-xs;
    }
  }
}

// 响应式设计
@media (max-width: $breakpoint-sm) {
  .login-box {
    padding: $spacing-xl;
    margin: $spacing-md;
  }
  
  .login-container {
    padding: $spacing-md;
  }
}
</style>
