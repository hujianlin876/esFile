<template>
  <el-card class="stat-card">
    <div class="stat-content">
      <div class="stat-icon" :style="{ background: iconBackground }">
        <el-icon :color="iconColor">
          <component :is="icon" />
        </el-icon>
      </div>
      <div class="stat-info">
        <div class="stat-number">{{ number }}</div>
        <div class="stat-label">{{ label }}</div>
      </div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  icon: any
  number: string | number
  label: string
  iconColor?: string
  iconBackground?: string
}

const props = withDefaults(defineProps<Props>(), {
  iconColor: 'white',
  iconBackground: 'linear-gradient(135deg, #409eff, #67c23a)'
})
</script>

<style scoped lang="scss">
.stat-card {
  .stat-content {
    display: flex;
    align-items: center;
    gap: $spacing-lg;
    
    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      
      .el-icon {
        font-size: 28px;
      }
    }
    
    .stat-info {
      .stat-number {
        font-size: $font-size-extra-large;
        font-weight: bold;
        color: $text-primary;
        margin-bottom: $spacing-xs;
      }
      
      .stat-label {
        font-size: $font-size-base;
        color: $text-secondary;
      }
    }
  }
}
</style>
