<template>
  <el-card class="activity-card">
    <template #header>
      <div class="card-header">
        <span>{{ title }}</span>
        <el-button type="text" @click="$emit('more')">查看更多</el-button>
      </div>
    </template>
    <div class="activity-list">
      <div
        v-for="activity in activities"
        :key="activity.id"
        class="activity-item"
      >
        <div class="activity-icon">
          <el-icon :color="getActivityColor(activity.type)">
            <component :is="getActivityIcon(activity.type)" />
          </el-icon>
        </div>
        <div class="activity-content">
          <div class="activity-title">{{ activity.title }}</div>
          <div class="activity-desc">{{ activity.description }}</div>
          <div class="activity-time">{{ formatTime(activity.createTime) }}</div>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { getActivityIcon, getActivityColor } from '@/utils/activity'
import { formatTime } from '@/utils/format'

interface Activity {
  id: number
  type: string
  title: string
  description: string
  createTime: string
}

interface Props {
  title: string
  activities: Activity[]
}

defineProps<Props>()

defineEmits<{
  more: []
}>()
</script>

<style scoped lang="scss">
.activity-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .activity-list {
    .activity-item {
      display: flex;
      align-items: flex-start;
      gap: $spacing-md;
      padding: $spacing-md 0;
      border-bottom: 1px solid $border-lighter;
      
      &:last-child {
        border-bottom: none;
      }
      
      .activity-icon {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background-color: $border-lighter;
        display: flex;
        align-items: center;
        justify-content: center;
        flex-shrink: 0;
        
        .el-icon {
          font-size: 20px;
        }
      }
      
      .activity-content {
        flex: 1;
        
        .activity-title {
          font-size: $font-size-base;
          font-weight: $font-weight-primary;
          color: $text-primary;
          margin-bottom: $spacing-xs;
        }
        
        .activity-desc {
          font-size: $font-size-small;
          color: $text-regular;
          margin-bottom: $spacing-xs;
        }
        
        .activity-time {
          font-size: $font-size-extra-small;
          color: $text-secondary;
        }
      }
    }
  }
}
</style>
