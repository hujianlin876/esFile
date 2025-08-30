<template>
  <div class="register-container">
    <div class="register-box">
      <div class="register-header">
        <h1 class="register-title">用户注册</h1>
        <p class="register-subtitle">创建您的账户</p>
      </div>
      
      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        class="register-form"
        @submit.prevent="handleRegister"
      >
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="请输入用户名"
            size="large"
            :prefix-icon="User"
            clearable
          />
        </el-form-item>
        
        <el-form-item prop="email">
          <el-input
            v-model="registerForm.email"
            placeholder="请输入邮箱"
            size="large"
            :prefix-icon="Message"
            clearable
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            :prefix-icon="Lock"
            show-password
            clearable
          />
        </el-form-item>
        
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请确认密码"
            size="large"
            :prefix-icon="Lock"
            show-password
            clearable
          />
        </el-form-item>
        
        <el-form-item prop="nickname">
          <el-input
            v-model="registerForm.nickname"
            placeholder="请输入昵称（可选）"
            size="large"
            :prefix-icon="UserFilled"
            clearable
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="register-button"
            :loading="loading"
            @click="handleRegister"
          >
            {{ loading ? '注册中...' : '注册' }}
          </el-button>
        </el-form-item>
        
        <el-form-item class="login-link">
          <span>已有账号？</span>
          <el-link type="primary" @click="goToLogin">
            立即登录
          </el-link>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Message, Lock, UserFilled } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'

const router = useRouter()

// 响应式数据
const registerFormRef = ref<FormInstance>()
const loading = ref(false)

const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  nickname: ''
})

// 表单验证规则
const registerRules: FormRules = {
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
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 方法
const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  try {
    await registerFormRef.value.validate()
    loading.value = true
    
    // TODO: 调用注册API
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (error: any) {
    if (error.message) {
      ElMessage.error(error.message)
    }
  } finally {
    loading.value = false
  }
}

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped lang="scss">
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: $spacing-lg;
}

.register-box {
  width: 100%;
  max-width: 400px;
  background: $background-white;
  border-radius: $border-radius-large;
  box-shadow: $box-shadow-dark;
  padding: $spacing-xxl;
}

.register-header {
  text-align: center;
  margin-bottom: $spacing-xl;
  
  .register-title {
    font-size: $font-size-extra-large;
    font-weight: bold;
    color: $text-primary;
    margin-bottom: $spacing-sm;
  }
  
  .register-subtitle {
    font-size: $font-size-base;
    color: $text-secondary;
    margin: 0;
  }
}

.register-form {
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
  
  .register-button {
    width: 100%;
    border-radius: $border-radius-base;
    font-size: $font-size-medium;
    font-weight: $font-weight-primary;
    height: 44px;
  }
  
  .login-link {
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
  .register-box {
    padding: $spacing-xl;
    margin: $spacing-md;
  }
  
  .register-container {
    padding: $spacing-md;
  }
}
</style>
