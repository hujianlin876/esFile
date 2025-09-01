<template>
  <div class="file-grid">
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="4" animated />
    </div>
    
    <div v-else-if="fileList.length === 0" class="empty-container">
      <el-empty description="暂无文件" />
    </div>
    
    <div v-else class="grid-container">
      <div 
        v-for="file in fileList" 
        :key="file.id" 
        class="file-item"
        :class="{ 'selected': isSelected(file.id) }"
        @click="handleItemClick(file)"
        @dblclick="handleItemDoubleClick(file)"
      >
        <div class="file-checkbox">
          <el-checkbox
            :model-value="isSelected(file.id)"
            @change="handleSelectionChange(file.id)"
            @click.stop
          />
        </div>
        
        <div class="file-icon">
          <el-image
            v-if="isImageFile(file.fileType)"
            :src="getThumbnailUrl(file.id)"
            fit="cover"
            class="thumbnail"
            :preview-src-list="[getFileUrl(file.id)]"
          >
            <template #error>
              <div class="file-type-icon">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
          
          <div v-else class="file-type-icon">
            <el-icon v-if="file.fileType === 'folder'"><Folder /></el-icon>
            <el-icon v-else-if="isVideoFile(file.fileType)"><VideoPlay /></el-icon>
            <el-icon v-else-if="isAudioFile(file.fileType)"><Headset /></el-icon>
            <el-icon v-else-if="isDocumentFile(file.fileType)"><Document /></el-icon>
            <el-icon v-else><Files /></el-icon>
          </div>
        </div>
        
        <div class="file-info">
          <div class="file-name" :title="file.fileName">
            {{ file.fileName }}
          </div>
          <div class="file-details">
            <span class="file-size">{{ formatFileSize(file.fileSize) }}</span>
            <span class="file-time">{{ formatDate(new Date(file.updateTime)) }}</span>
          </div>
        </div>
        
        <div class="file-actions">
          <el-dropdown @command="handleAction">
            <el-button type="text" size="small">
              <el-icon><More /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item :command="`preview_${file.id}`">预览</el-dropdown-item>
                <el-dropdown-item :command="`download_${file.id}`">下载</el-dropdown-item>
                <el-dropdown-item :command="`rename_${file.id}`">重命名</el-dropdown-item>
                <el-dropdown-item :command="`move_${file.id}`">移动</el-dropdown-item>
                <el-dropdown-item :command="`copy_${file.id}`">复制</el-dropdown-item>
                <el-dropdown-item :command="`delete_${file.id}`" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    
    <!-- 分页 -->
    <div v-if="total > 0" class="pagination-container">
      <el-pagination
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { 
  Picture, Folder, VideoPlay, Headset, 
  Document, Files, More 
} from '@element-plus/icons-vue'
import { formatFileSize, formatDate } from '@/utils/format'
import type { FileInfo } from '@/api/types/file'

interface Props {
  fileList: FileInfo[]
  selectedFiles: number[]
  loading: boolean
  currentPage: number
  pageSize: number
  total: number
}

interface Emits {
  (e: 'update:currentPage', page: number): void
  (e: 'update:pageSize', size: number): void
  (e: 'selection-change', fileId: number): void
  (e: 'item-click', file: FileInfo): void
  (e: 'item-double-click', file: FileInfo): void
  (e: 'action', action: string, fileId: number): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const isSelected = (fileId: number) => {
  return props.selectedFiles.includes(fileId)
}

const handleSelectionChange = (fileId: number) => {
  emit('selection-change', fileId)
}

const handleItemClick = (file: FileInfo) => {
  emit('item-click', file)
}

const handleItemDoubleClick = (file: FileInfo) => {
  emit('item-double-click', file)
}

const handleSizeChange = (size: number) => {
  emit('update:pageSize', size)
}

const handlePageChange = (page: number) => {
  emit('update:currentPage', page)
}

const handleAction = (command: string) => {
  const [action, fileId] = command.split('_')
  emit('action', action, parseInt(fileId))
}

// 文件类型判断
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

// URL生成
const getThumbnailUrl = (fileId: number) => {
  return `/api/files/${fileId}/thumbnail`
}

const getFileUrl = (fileId: number) => {
  return `/api/files/${fileId}/download`
}
</script>

<style scoped lang="scss">
@import '@/assets/styles/variables.scss';

.file-grid {
  .grid-container {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: $spacing-md;
    padding: $spacing-md;
  }

  .file-item {
    position: relative;
    background: $background-light;
    border: 1px solid $border-light;
    border-radius: 4px;
    padding: $spacing-md;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      border-color: $primary-color;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    &.selected {
      background: rgba($primary-color, 0.1);
      border-color: $primary-color;
    }
  }

  .file-checkbox {
    position: absolute;
    top: $spacing-sm;
    left: $spacing-sm;
    z-index: 1;
  }

  .file-icon {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 80px;
    margin-bottom: $spacing-sm;

    .thumbnail {
      width: 60px;
      height: 60px;
      border-radius: 4px;
    }

    .file-type-icon {
      font-size: 48px;
      color: $text-secondary;
    }
  }

  .file-info {
    text-align: center;

    .file-name {
      font-weight: 500;
      margin-bottom: $spacing-xs;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .file-details {
      font-size: $font-size-small;
      color: $text-secondary;
      
      .file-size {
        margin-right: $spacing-sm;
      }
    }
  }

  .file-actions {
    position: absolute;
    top: $spacing-sm;
    right: $spacing-sm;
    opacity: 0;
    transition: opacity 0.3s ease;
  }

  .file-item:hover .file-actions {
    opacity: 1;
  }

  .loading-container,
  .empty-container {
    padding: $spacing-xl;
    text-align: center;
  }

  .pagination-container {
    display: flex;
    justify-content: center;
    padding: $spacing-lg;
  }
}
</style>

