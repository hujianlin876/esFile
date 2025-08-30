<template>
  <div class="error-layout">
    <div class="error-content">
      <h1 class="error-code">404</h1>
      <h2 class="error-title">页面不存在</h2>
      <p class="error-desc">抱歉，您访问的页面不存在或已被删除</p>
      <div class="error-actions">
        <el-button type="primary" @click="goHome">返回首页</el-button>
        <el-button @click="goBack">返回上页</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/')
}

const goBack = () => {
  router.go(-1)
}
</script>

<style scoped lang="scss">
.error-layout {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: $background-color;
  
  .error-content {
    text-align: center;
    padding: $spacing-xxl;
    
    .error-code {
      font-size: 120px;
      font-weight: bold;
      color: $primary-color;
      margin: 0;
      line-height: 1;
    }
    
    .error-title {
      font-size: $font-size-extra-large;
      color: $text-primary;
      margin: $spacing-lg 0;
    }
    
    .error-desc {
      font-size: $font-size-base;
      color: $text-secondary;
      margin-bottom: $spacing-xl;
    }
    
    .error-actions {
      display: flex;
      justify-content: center;
      gap: $spacing-md;
    }
  }
}
</style>
