<template>
  <div class="main-layout">
    <!-- 侧边栏 -->
    <el-aside
      :width="sidebarWidth"
      class="main-sidebar"
      :class="{ 'collapsed': sidebarCollapsed }"
    >
      <div class="sidebar-header">
        <h1 class="sidebar-title" v-if="!sidebarCollapsed">
          ES文件系统
        </h1>
        <el-icon v-else class="sidebar-icon">
          <Document />
        </el-icon>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        :collapse="sidebarCollapsed"
        :unique-opened="true"
        router
        class="sidebar-menu"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataBoard /></el-icon>
          <template #title>仪表板</template>
        </el-menu-item>
        
        <el-menu-item index="/files">
          <el-icon><Folder /></el-icon>
          <template #title>文件管理</template>
        </el-menu-item>
        
        <el-sub-menu index="/system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/users">用户管理</el-menu-item>
          <el-menu-item index="/permissions">权限管理</el-menu-item>
        </el-sub-menu>
        
        <el-menu-item index="/database">
          <el-icon><DataAnalysis /></el-icon>
          <template #title>数据库管理</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主内容区域 -->
    <el-container class="main-container">
      <!-- 头部 -->
      <el-header class="main-header">
        <div class="header-left">
          <el-button
            type="text"
            :icon="sidebarCollapsed ? Expand : Fold"
            @click="toggleSidebar"
            class="sidebar-toggle"
          />
          <el-breadcrumb separator="/">
            <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path">
              {{ item.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <div class="user-info">
              <el-avatar :size="32" :src="userAvatar">
                {{ userInitials }}
              </el-avatar>
              <span class="username">{{ authStore.user?.nickname || authStore.user?.username }}</span>
              <el-icon><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人资料</el-dropdown-item>
                <el-dropdown-item command="settings">系统设置</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区域 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import {
  Document,
  DataBoard,
  Folder,
  Setting,
  DataAnalysis,
  Expand,
  Fold,
  ArrowDown
} from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/modules/auth'
import { useAppStore } from '@/stores/modules/app'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const appStore = useAppStore()

// 响应式数据
const sidebarCollapsed = computed(() => appStore.sidebarCollapsed)
const sidebarWidth = computed(() => sidebarCollapsed.value ? '64px' : '200px')

// 计算属性
const activeMenu = computed(() => route.path)
const userAvatar = computed(() => authStore.user?.avatar || '')
const userInitials = computed(() => {
  const user = authStore.user
  if (user?.nickname) {
    return user.nickname.charAt(0).toUpperCase()
  }
  if (user?.username) {
    return user.username.charAt(0).toUpperCase()
  }
  return 'U'
})

const breadcrumbs = computed(() => {
  const matched = route.matched.filter(item => item.meta && item.meta.title)
  return matched.map(item => ({
    title: item.meta.title,
    path: item.path
  }))
})

// 方法
const toggleSidebar = () => {
  appStore.toggleSidebar()
}

const handleCommand = async (command: string) => {
  switch (command) {
    case 'profile':
      // TODO: 跳转到个人资料页面
      break
    case 'settings':
      // TODO: 跳转到系统设置页面
      break
    case 'logout':
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        authStore.logout()
        router.push('/login')
      } catch {
        // 用户取消
      }
      break
  }
}

// 监听器
watch(() => route.path, () => {
  // 路由变化时更新面包屑
}, { immediate: true })
</script>

<style scoped lang="scss">
.main-layout {
  height: 100vh;
  display: flex;
}

.main-sidebar {
  background-color: $background-white;
  border-right: 1px solid $border-color;
  transition: width 0.3s;
  
  &.collapsed {
    .sidebar-header {
      padding: $spacing-sm;
      
      .sidebar-title {
        display: none;
      }
      
      .sidebar-icon {
        font-size: 24px;
        color: $primary-color;
      }
    }
  }
  
  .sidebar-header {
    height: $header-height;
    display: flex;
    align-items: center;
    justify-content: center;
    border-bottom: 1px solid $border-lighter;
    padding: $spacing-md;
    
    .sidebar-title {
      font-size: $font-size-large;
      font-weight: bold;
      color: $primary-color;
      margin: 0;
    }
  }
  
  .sidebar-menu {
    border-right: none;
    
    .el-menu-item,
    .el-sub-menu__title {
      height: 50px;
      line-height: 50px;
      
      .el-icon {
        margin-right: $spacing-sm;
      }
    }
  }
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.main-header {
  background-color: $background-white;
  border-bottom: 1px solid $border-color;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 $spacing-lg;
  
  .header-left {
    display: flex;
    align-items: center;
    gap: $spacing-lg;
    
    .sidebar-toggle {
      padding: $spacing-xs;
      margin-right: $spacing-sm;
    }
  }
  
  .header-right {
    .user-info {
      display: flex;
      align-items: center;
      gap: $spacing-sm;
      cursor: pointer;
      padding: $spacing-xs $spacing-sm;
      border-radius: $border-radius-base;
      transition: background-color 0.3s;
      
      &:hover {
        background-color: $border-lighter;
      }
      
      .username {
        font-size: $font-size-base;
        color: $text-primary;
      }
    }
  }
}

.main-content {
  background-color: $background-color;
  padding: $spacing-lg;
  overflow-y: auto;
}

// 响应式设计
@media (max-width: $breakpoint-md) {
  .main-sidebar {
    position: fixed;
    left: 0;
    top: 0;
    height: 100vh;
    z-index: 1000;
    transform: translateX(-100%);
    transition: transform 0.3s;
    
    &.collapsed {
      transform: translateX(0);
    }
  }
  
  .main-container {
    margin-left: 0;
  }
}
</style>
