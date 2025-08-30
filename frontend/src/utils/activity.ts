import {
  Upload,
  Download,
  Delete,
  Edit,
  Document
} from '@element-plus/icons-vue'

/**
 * 活动类型图标映射
 */
export const getActivityIcon = (type: string) => {
  const iconMap: Record<string, any> = {
    upload: Upload,
    download: Download,
    delete: Delete,
    edit: Edit
  }
  return iconMap[type] || Document
}

/**
 * 活动类型颜色映射
 */
export const getActivityColor = (type: string): string => {
  const colorMap: Record<string, string> = {
    upload: '#67c23a',
    download: '#409eff',
    delete: '#f56c6c',
    edit: '#e6a23c'
  }
  return colorMap[type] || '#909399'
}
