<template>
  <div class="file-preview-component">
    <el-dialog
      v-model="visible"
      :title="`预览文件 - ${file?.name}`"
      width="80%"
      :before-close="handleClose"
    >
      <div class="preview-content">
        <!-- 图片预览 -->
        <div v-if="isImage" class="image-preview">
          <el-image
            :src="file?.preview || file?.url"
            :preview-src-list="[file?.preview || file?.url]"
            fit="contain"
            style="max-width: 100%; max-height: 500px;"
          />
        </div>
        
        <!-- PDF预览 -->
        <div v-else-if="isPdf" class="pdf-preview">
          <iframe
            :src="file?.preview || file?.url"
            width="100%"
            height="500"
            frameborder="0"
          />
        </div>
        
        <!-- 视频预览 -->
        <div v-else-if="isVideo" class="video-preview">
          <video
            :src="file?.url"
            controls
            style="max-width: 100%; max-height: 500px;"
          >
            您的浏览器不支持视频播放
          </video>
        </div>
        
        <!-- 音频预览 -->
        <div v-else-if="isAudio" class="audio-preview">
          <audio
            :src="file?.url"
            controls
            style="width: 100%;"
          >
            您的浏览器不支持音频播放
          </audio>
        </div>
        
        <!-- 文本预览 -->
        <div v-else-if="isText" class="text-preview">
          <el-input
            v-model="textContent"
            type="textarea"
            :rows="20"
            readonly
            placeholder="文件内容加载中..."
          />
        </div>
        
        <!-- 其他文件类型 -->
        <div v-else class="other-preview">
          <div class="preview-placeholder">
            <el-icon :size="64" color="#909399">
              <Document />
            </el-icon>
            <p>此文件类型暂不支持预览</p>
            <el-button type="primary" @click="downloadFile">
              下载文件
            </el-button>
          </div>
        </div>
      </div>
      
      <!-- 文件信息 -->
      <div class="file-info">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="文件名">{{ file?.name }}</el-descriptions-item>
          <el-descriptions-item label="文件大小">{{ formatFileSize(file?.size || 0) }}</el-descriptions-item>
          <el-descriptions-item label="文件类型">{{ file?.type }}</el-descriptions-item>
          <el-descriptions-item label="上传时间">{{ formatTime(file?.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="上传者">{{ file?.uploader }}</el-descriptions-item>
          <el-descriptions-item label="下载次数">{{ file?.downloadCount || 0 }}</el-descriptions-item>
        </el-descriptions>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleClose">关闭</el-button>
          <el-button type="primary" @click="downloadFile">下载</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { Document } from '@element-plus/icons-vue'
import { formatFileSize, formatTime } from '@/utils/format'
import { getFileContent } from '@/api/file/file'
import type { FileInfo } from '@/api/types/file'

interface Props {
  visible: boolean
  file: FileInfo | null
}

interface Emits {
  'update:visible': [visible: boolean]
  close: []
  download: [file: FileInfo]
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const textContent = ref('')

// 计算属性
const isImage = computed(() => {
  if (!props.file) return false
  const imageTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp', 'image/bmp']
  return imageTypes.includes(props.file.mimeType) || props.file.type === 'image'
})

const isPdf = computed(() => {
  if (!props.file) return false
  return props.file.mimeType === 'application/pdf' || props.file.extension === 'pdf'
})

const isVideo = computed(() => {
  if (!props.file) return false
  const videoTypes = ['video/mp4', 'video/avi', 'video/mov', 'video/wmv', 'video/flv']
  return videoTypes.includes(props.file.mimeType) || props.file.type === 'video'
})

const isAudio = computed(() => {
  if (!props.file) return false
  const audioTypes = ['audio/mp3', 'audio/wav', 'audio/flac', 'audio/aac', 'audio/ogg']
  return audioTypes.includes(props.file.mimeType) || props.file.type === 'audio'
})

const isText = computed(() => {
  if (!props.file) return false
  const textTypes = ['text/plain', 'text/html', 'text/css', 'text/javascript', 'application/json']
  const textExtensions = ['txt', 'md', 'html', 'css', 'js', 'json', 'xml', 'log']
  return textTypes.includes(props.file.mimeType) || textExtensions.includes(props.file.extension)
})

// 方法
const handleClose = () => {
  emit('update:visible', false)
  emit('close')
}

const downloadFile = () => {
  if (props.file) {
    emit('download', props.file)
  }
}

// 监听文件变化，加载文本内容
watch(() => props.file, async (newFile) => {
  if (newFile && isText.value) {
    try {
      const response = await getFileContent(newFile.id)
      textContent.value = response.data.content
    } catch (error) {
      textContent.value = '文件内容加载失败'
      console.error('获取文件内容失败:', error)
    }
  }
}, { immediate: true })
</script>

<style lang="scss" scoped>
.file-preview-component {
  .preview-content {
    min-height: 400px;
    display: flex;
    justify-content: center;
    align-items: center;
    
    .image-preview,
    .pdf-preview,
    .video-preview,
    .audio-preview {
      width: 100%;
      text-align: center;
    }
    
    .text-preview {
      width: 100%;
    }
    
    .other-preview {
      .preview-placeholder {
        text-align: center;
        color: $text-secondary;
        
        p {
          margin: $spacing-md 0;
          font-size: $font-size-base;
        }
      }
    }
  }
  
  .file-info {
    margin-top: $spacing-lg;
  }
  
  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: $spacing-sm;
  }
}
</style>
