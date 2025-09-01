<template>
  <div class="file-list-view">
    <BaseTable
      :data="fileList"
      :loading="loading"
      :total="total"
      :current-page="currentPage"
      :page-size="pageSize"
      :show-selection="true"
      :columns="[]"
      @selection-change="handleSelectionChange"
      @current-change="handlePageChange"
      @size-change="handleSizeChange"
    >
      <el-table-column type="selection" width="55" />
      
      <el-table-column label="文件名" min-width="200">
        <template #default="{ row }">
          <div class="file-name-cell">
            <el-icon class="file-icon">
              <Folder v-if="row.fileType === 'folder'" />
              <Picture v-else-if="isImageFile(row.fileType)" />
              <VideoPlay v-else-if="isVideoFile(row.fileType)" />
              <Headset v-else-if="isAudioFile(row.fileType)" />
              <Document v-else-if="isDocumentFile(row.fileType)" />
              <Files v-else />
            </el-icon>
            <span 
              class="file-name"
              @click="handleItemClick(row)"
              @dblclick="handleItemDoubleClick(row)"
            >
              {{ row.fileName }}
            </span>
          </div>
        </template>
      </el-table-column>
      
      <el-table-column label="大小" width="100" sortable="custom">
        <template #default="{ row }">
          <span v-if="row.fileType !== 'folder'">
            {{ formatFileSize(row.fileSize) }}
          </span>
          <span v-else class="folder-indicator">-</span>
        </template>
      </el-table-column>
      
      <el-table-column label="类型" width="100">
        <template #default="{ row }">
          <el-tag 
            :type="getFileTypeTagType(row.fileType)"
            size="small"
          >
            {{ getFileTypeDisplay(row.fileType) }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="修改时间" width="160" sortable="custom">
        <template #default="{ row }">
          {{ formatDate(new Date(row.updateTime)) }}
        </template>
      </el-table-column>
      
      <el-table-column label="上传者" width="120">
        <template #default="{ row }">
          {{ row.uploadUserName || '未知' }}
        </template>
      </el-table-column>
      
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <div class="action-buttons">
            <el-button
              type="text"
              size="small"
              @click="handleAction('preview', row.id)"
            >
              预览
            </el-button>
            
            <el-button
              type="text"
              size="small"
              @click="handleAction('download', row.id)"
            >
              下载
            </el-button>
            
            <el-dropdown @command="handleDropdownAction">
              <el-button type="text" size="small">
                更多<el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :command="`rename_${row.id}`">
                    重命名
                  </el-dropdown-item>
                  <el-dropdown-item :command="`move_${row.id}`">
                    移动
                  </el-dropdown-item>
                  <el-dropdown-item :command="`copy_${row.id}`">
                    复制
                  </el-dropdown-item>
                  <el-dropdown-item :command="`share_${row.id}`">
                    分享
                  </el-dropdown-item>
                  <el-dropdown-item 
                    :command="`delete_${row.id}`" 
                    divided
                    class="danger-item"
                  >
                    删除
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </template>
      </el-table-column>
    </BaseTable>
  </div>
</template>

<script setup lang="ts">
import { 
  Folder, Picture, VideoPlay, Headset, 
  Document, Files, ArrowDown 
} from '@element-plus/icons-vue'
import BaseTable from '@/components/table/BaseTable.vue'
import { formatFileSize, formatDate } from '@/utils/format'
import type { FileInfo } from '@/api/types/file'

interface Props {
  fileList: FileInfo[]
  loading: boolean
  currentPage: number
  pageSize: number
  total: number
}

interface Emits {
  (e: 'update:currentPage', page: number): void
  (e: 'update:pageSize', size: number): void
  (e: 'selection-change', selection: FileInfo[]): void
  (e: 'item-click', file: FileInfo): void
  (e: 'item-double-click', file: FileInfo): void
  (e: 'action', action: string, fileId: number): void
  (e: 'sort-change', sort: { prop: string; order: string }): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const handleSelectionChange = (selection: FileInfo[]) => {
  emit('selection-change', selection)
}

const handlePageChange = (page: number) => {
  emit('update:currentPage', page)
}

const handleSizeChange = (size: number) => {
  emit('update:pageSize', size)
}

const handleItemClick = (file: FileInfo) => {
  emit('item-click', file)
}

const handleItemDoubleClick = (file: FileInfo) => {
  emit('item-double-click', file)
}

const handleAction = (action: string, fileId: number) => {
  emit('action', action, fileId)
}

const handleDropdownAction = (command: string) => {
  const [action, fileId] = command.split('_')
  emit('action', action, parseInt(fileId))
}

// 文件类型判断和显示
const isImageFile = (fileType: string) => {
  return ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp'].includes(fileType.toLowerCase())
}

const isVideoFile = (fileType: string) => {
  return ['mp4', 'avi', 'mov', 'wmv', 'flv', 'mkv'].includes(fileType.toLowerCase())
}

const isAudioFile = (fileType: string) => {
  return ['mp3', 'wav', 'flac', 'aac', 'ogg'].includes(fileType.toLowerCase())
}

const isDocumentFile = (fileType: string) => {
  return ['pdf', 'doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'txt'].includes(fileType.toLowerCase())
}

const getFileTypeDisplay = (fileType: string) => {
  if (fileType === 'folder') return '文件夹'
  if (isImageFile(fileType)) return '图片'
  if (isVideoFile(fileType)) return '视频'
  if (isAudioFile(fileType)) return '音频'
  if (isDocumentFile(fileType)) return '文档'
  return fileType.toUpperCase()
}

const getFileTypeTagType = (fileType: string): 'success' | 'warning' | 'info' | 'primary' | 'danger' | undefined => {
  if (fileType === 'folder') return undefined
  if (isImageFile(fileType)) return 'success'
  if (isVideoFile(fileType)) return 'warning'
  if (isAudioFile(fileType)) return 'info'
  if (isDocumentFile(fileType)) return 'primary'
  return 'danger'
}
</script>

<style scoped lang="scss">
@import '@/assets/styles/variables.scss';

.file-list-view {
  .file-name-cell {
    display: flex;
    align-items: center;
    gap: $spacing-sm;

    .file-icon {
      font-size: 16px;
      color: $text-secondary;
    }

    .file-name {
      cursor: pointer;
      color: $primary-color;
      
      &:hover {
        text-decoration: underline;
      }
    }
  }

  .folder-indicator {
    color: $text-secondary;
  }

  .action-buttons {
    display: flex;
    gap: $spacing-xs;
    align-items: center;
  }

  :deep(.danger-item) {
    color: $danger-color !important;
  }
}
</style>

