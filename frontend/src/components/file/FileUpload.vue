<template>
  <div class="file-upload">
    <el-upload
      ref="uploadRef"
      :action="uploadUrl"
      :headers="headers"
      :data="uploadData"
      :multiple="multiple"
      :show-file-list="showFileList"
      :drag="drag"
      :accept="accept"
      :limit="limit"
      :file-list="fileList"
      :auto-upload="autoUpload"
      :before-upload="beforeUpload"
      :on-success="onSuccess"
      :on-error="onError"
      :on-exceed="onExceed"
      :on-remove="onRemove"
      :on-change="onChange"
      class="upload-area"
    >
      <template v-if="drag">
        <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
        <div class="el-upload__text">
          将文件拖到此处，或<em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">
            {{ tip || '支持jpg/png/gif/pdf/doc/docx/xls/xlsx等格式文件' }}
          </div>
        </template>
      </template>
      
      <template v-else>
        <el-button type="primary" :icon="Plus">选择文件</el-button>
        <template #tip>
          <div class="el-upload__tip">
            {{ tip || '支持jpg/png/gif/pdf/doc/docx/xls/xlsx等格式文件' }}
          </div>
        </template>
      </template>
    </el-upload>
    
    <!-- 上传进度 -->
    <div v-if="showProgress && uploadProgress.length > 0" class="upload-progress">
      <div
        v-for="item in uploadProgress"
        :key="item.uid"
        class="progress-item"
      >
        <div class="progress-info">
          <span class="filename">{{ item.name }}</span>
          <span class="status">{{ item.status }}</span>
        </div>
        <el-progress
          :percentage="item.percentage"
          :status="item.status === 'success' ? 'success' : item.status === 'error' ? 'exception' : undefined"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { UploadFilled, Plus } from '@element-plus/icons-vue'
import type { UploadProps, UploadUserFile, UploadFiles } from 'element-plus'

interface UploadProgress {
  uid: string
  name: string
  percentage: number
  status: 'uploading' | 'success' | 'error'
}

interface Props {
  uploadUrl?: string
  headers?: Record<string, string>
  uploadData?: Record<string, any>
  multiple?: boolean
  showFileList?: boolean
  drag?: boolean
  accept?: string
  limit?: number
  autoUpload?: boolean
  tip?: string
  description?: string
  tags?: string
  isPublic?: number
  parentFolderId?: number
}

interface Emits {
  success: [response: any, file: UploadUserFile, fileList: UploadFiles]
  error: [error: Error, file: UploadUserFile, fileList: UploadFiles]
  change: [file: UploadUserFile, fileList: UploadFiles]
  remove: [file: UploadUserFile, fileList: UploadFiles]
  exceed: [files: File[], fileList: UploadFiles]
}

const props = withDefaults(defineProps<Props>(), {
  uploadUrl: '/api/files/upload',
  multiple: true,
  showFileList: true,
  drag: true,
  accept: '.jpg,.jpeg,.png,.gif,.pdf,.doc,.docx,.xls,.xlsx,.txt,.zip,.rar',
  limit: 10,
  autoUpload: true,
  description: '',
  tags: '',
  isPublic: 0,
  parentFolderId: undefined
})

const emit = defineEmits<Emits>()

// 响应式数据
const uploadRef = ref()
const fileList = ref<UploadFiles>([])
const uploadProgress = ref<UploadProgress[]>([])

// 计算属性
const showProgress = computed(() => !props.autoUpload || uploadProgress.value.length > 0)

// 构建上传数据
const uploadData = computed(() => {
  const data: Record<string, any> = {}
  
  if (props.description) {
    data.description = props.description
  }
  if (props.tags) {
    data.tags = props.tags
  }
  if (props.isPublic !== undefined) {
    data.isPublic = props.isPublic
  }
  if (props.parentFolderId) {
    data.parentFolderId = props.parentFolderId
  }
  
  return data
})

// 方法
const beforeUpload = (file: File) => {
  // 文件大小限制 (100MB)
  const isLt100M = file.size / 1024 / 1024 < 100
  if (!isLt100M) {
    ElMessage.error('上传文件大小不能超过 100MB!')
    return false
  }
  
  // 添加到进度列表
  if (!props.autoUpload) {
    uploadProgress.value.push({
      uid: file.uid || String(Date.now()),
      name: file.name,
      percentage: 0,
      status: 'uploading'
    })
  }
  
  return true
}

const onSuccess = (response: any, file: UploadUserFile, fileList: UploadFiles) => {
  ElMessage.success(`${file.name} 上传成功`)
  
  // 更新进度
  const progressItem = uploadProgress.value.find(item => item.uid === file.uid)
  if (progressItem) {
    progressItem.percentage = 100
    progressItem.status = 'success'
  }
  
  emit('success', response, file, fileList)
}

const onError = (error: Error, file: UploadUserFile, fileList: UploadFiles) => {
  ElMessage.error(`${file.name} 上传失败`)
  
  // 更新进度
  const progressItem = uploadProgress.value.find(item => item.uid === file.uid)
  if (progressItem) {
    progressItem.status = 'error'
  }
  
  emit('error', error, file, fileList)
}

const onChange = (file: UploadUserFile, fileList: UploadFiles) => {
  emit('change', file, fileList)
}

const onRemove = (file: UploadUserFile, fileList: UploadFiles) => {
  // 从进度列表中移除
  const index = uploadProgress.value.findIndex(item => item.uid === file.uid)
  if (index > -1) {
    uploadProgress.value.splice(index, 1)
  }
  
  emit('remove', file, fileList)
}

const onExceed = (files: File[], fileList: UploadFiles) => {
  ElMessage.warning(`最多只能上传 ${props.limit} 个文件`)
  emit('exceed', files, fileList)
}

// 暴露方法
defineExpose({
  uploadRef,
  fileList,
  uploadProgress,
  submit: () => uploadRef.value?.submit()
})
</script>

<style scoped lang="scss">
.file-upload {
  .upload-area {
    width: 100%;
    
    .el-upload {
      width: 100%;
    }
    
    .el-upload-dragger {
      width: 100%;
      height: 180px;
      
      .el-icon--upload {
        font-size: 67px;
        color: $text-secondary;
        margin-bottom: $spacing-md;
      }
      
      .el-upload__text {
        color: $text-regular;
        font-size: $font-size-base;
        
        em {
          color: $primary-color;
          font-style: normal;
        }
      }
      
      .el-upload__tip {
        color: $text-secondary;
        font-size: $font-size-small;
        margin-top: $spacing-md;
      }
    }
  }
  
  .upload-progress {
    margin-top: $spacing-lg;
    
    .progress-item {
      margin-bottom: $spacing-md;
      
      .progress-info {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: $spacing-xs;
        
        .filename {
          font-size: $font-size-small;
          color: $text-primary;
          flex: 1;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        
        .status {
          font-size: $font-size-extra-small;
          color: $text-secondary;
          margin-left: $spacing-sm;
        }
      }
    }
  }
}
</style>
