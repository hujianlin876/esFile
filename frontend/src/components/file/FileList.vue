<template>
  <div class="file-list-component">
    <el-table
      :data="files"
      :loading="loading"
      @selection-change="handleSelectionChange"
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="文件名" min-width="200">
        <template #default="{ row }">
          <div class="file-name-cell">
            <el-icon :color="getFileIconColor(row.type)" class="file-icon-small">
              <component :is="getFileIcon(row.type)" />
            </el-icon>
            <span class="file-name" :title="row.name">{{ row.name }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="size" label="大小" width="120">
        <template #default="{ row }">
          {{ formatFileSize(row.size) }}
        </template>
      </el-table-column>
      <el-table-column prop="type" label="类型" width="100" />
      <el-table-column prop="createTime" label="上传时间" width="180">
        <template #default="{ row }">
          {{ formatTime(row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="uploader" label="上传者" width="120" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button type="text" size="small" @click="previewFile(row)">
            预览
          </el-button>
          <el-button type="text" size="small" @click="downloadFile(row)">
            下载
          </el-button>
          <el-button type="text" size="small" @click="editFile(row)">
            编辑
          </el-button>
          <el-button type="text" size="small" @click="deleteFile(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Document,
  Picture,
  VideoPlay,
  Headset,
  Files,
  Folder
} from '@element-plus/icons-vue'
import { formatFileSize, formatTime } from '@/utils/format'
import type { FileInfo } from '@/api/types/file'

interface Props {
  files: FileInfo[]
  loading?: boolean
}

interface Emits {
  selectionChange: [files: FileInfo[]]
  preview: [file: FileInfo]
  download: [file: FileInfo]
  edit: [file: FileInfo]
  delete: [file: FileInfo]
}

const props = withDefaults(defineProps<Props>(), {
  loading: false
})

const emit = defineEmits<Emits>()

// 方法
const handleSelectionChange = (selection: FileInfo[]) => {
  emit('selectionChange', selection)
}

const previewFile = (file: FileInfo) => {
  emit('preview', file)
}

const downloadFile = (file: FileInfo) => {
  emit('download', file)
}

const editFile = (file: FileInfo) => {
  emit('edit', file)
}

const deleteFile = (file: FileInfo) => {
  emit('delete', file)
}

const getFileIcon = (type: string) => {
  const iconMap: Record<string, any> = {
    document: Document,
    image: Picture,
    video: VideoPlay,
    audio: Headset,
    archive: Files,
    folder: Folder
  }
  return iconMap[type] || Document
}

const getFileIconColor = (type: string) => {
  const colorMap: Record<string, string> = {
    document: '#409eff',
    image: '#67c23a',
    video: '#e6a23c',
    audio: '#f56c6c',
    archive: '#909399',
    folder: '#409eff'
  }
  return colorMap[type] || '#909399'
}
</script>

<style lang="scss" scoped>
.file-list-component {
  .file-name-cell {
    display: flex;
    align-items: center;
    gap: $spacing-sm;
    
    .file-icon-small {
      font-size: 16px;
    }
    
    .file-name {
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
}
</style>
