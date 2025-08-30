<template>
  <div class="file-manager">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">文件管理</h1>
      <p class="page-desc">管理您的所有文件，支持上传、下载、预览、搜索等功能</p>
    </div>
    
    <!-- 文件上传区域 -->
    <el-card class="upload-card">
      <template #header>
        <div class="card-header">
          <span>文件上传</span>
          <el-button type="text" @click="showUpload = !showUpload">
            {{ showUpload ? '收起' : '展开' }}
          </el-button>
        </div>
      </template>
      
      <el-collapse-transition>
        <div v-show="showUpload">
          <FileUpload
            ref="fileUploadRef"
            @success="handleUploadSuccess"
            @error="handleUploadError"
            @change="handleFileChange"
          />
        </div>
      </el-collapse-transition>
    </el-card>
    
    <!-- 搜索和工具栏 -->
    <FileSearch
      @search="handleSearch"
      @reset="handleSearchReset"
    />
    
    <FileToolbar
      v-model:view-mode="viewMode"
      :selected-count="selectedFiles.length"
      @refresh="loadFiles"
      @batch-download="handleBatchDownload"
      @batch-delete="handleBatchDelete"
      @settings="showSettings = true"
      @sort="handleSort"
    />
    
    <!-- 文件列表 -->
    <div class="file-content">
      <!-- 网格视图 -->
      <div v-if="viewMode === 'grid'" class="file-grid">
        <div
          v-for="file in fileList"
          :key="file.id"
          class="file-item"
          :class="{ selected: selectedFiles.includes(file.id) }"
          @click="toggleFileSelection(file.id)"
        >
          <div class="file-icon">
            <el-icon :size="48" :color="getFileIconColor(file.fileType)">
              <component :is="getFileIcon(file.fileType)" />
            </el-icon>
          </div>
          <div class="file-info">
            <div class="file-name" :title="file.fileName">{{ file.fileName }}</div>
            <div class="file-meta">
              <span class="file-size">{{ formatFileSize(file.fileSize) }}</span>
              <span class="file-date">{{ formatTime(file.createTime) }}</span>
            </div>
          </div>
          <div class="file-actions">
            <el-button
              type="text"
              size="small"
              :icon="View"
              @click.stop="previewFile(file)"
            >
              预览
            </el-button>
            <el-button
              type="text"
              size="small"
              :icon="Download"
              @click.stop="downloadFile(file)"
            >
              下载
            </el-button>
            <el-button
              type="text"
              size="small"
              :icon="Delete"
              @click.stop="deleteFile(file)"
            >
              删除
            </el-button>
          </div>
        </div>
      </div>
      
      <!-- 列表视图 -->
      <div v-else class="file-list">
        <el-table
          :data="fileList"
          @selection-change="handleSelectionChange"
          style="width: 100%"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column label="文件名" min-width="200">
            <template #default="{ row }">
              <div class="file-name-cell">
                <el-icon :color="getFileIconColor(row.fileType)" class="file-icon-small">
                  <component :is="getFileIcon(row.fileType)" />
                </el-icon>
                <span>{{ row.fileName }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="fileSize" label="大小" width="120">
            <template #default="{ row }">
              {{ formatFileSize(row.fileSize) }}
            </template>
          </el-table-column>
          <el-table-column prop="fileType" label="类型" width="100" />
          <el-table-column prop="createTime" label="上传时间" width="180">
            <template #default="{ row }">
              {{ formatTime(row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="uploadUserName" label="上传者" width="120" />
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button type="text" size="small" @click="previewFile(row)">
                预览
              </el-button>
              <el-button type="text" size="small" @click="downloadFile(row)">
                下载
              </el-button>
              <el-button type="text" size="small" @click="deleteFile(row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    
    <!-- 分页 -->
    <div class="pagination-wrapper">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    
    <!-- 设置对话框 -->
    <el-dialog
      v-model="showSettings"
      title="文件管理设置"
      width="500px"
    >
      <el-form label-width="120px">
        <el-form-item label="默认视图">
          <el-radio-group v-model="defaultViewMode">
            <el-radio label="grid">网格视图</el-radio>
            <el-radio label="list">列表视图</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="每页显示">
          <el-select v-model="defaultPageSize">
            <el-option label="20" :value="20" />
            <el-option label="50" :value="50" />
            <el-option label="100" :value="100" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showSettings = false">取消</el-button>
        <el-button type="primary" @click="saveSettings">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  View,
  Download,
  Delete,
  Document,
  Picture,
  VideoPlay,
  Headset,
  Files,
  Folder
} from '@element-plus/icons-vue'
import FileUpload from '@/components/file/FileUpload.vue'
import FileSearch from '@/components/file/FileSearch.vue'
import FileToolbar from '@/components/file/FileToolbar.vue'
import { formatFileSize, formatTime } from '@/utils/format'
import { getFileList, deleteFile as deleteFileApi, batchDeleteFiles, batchDownloadFiles } from '@/api/file/file'
import type { FileInfo } from '@/api/types/file'

// 响应式数据
const showUpload = ref(true)
const viewMode = ref<'grid' | 'list'>('grid')
const showSettings = ref(false)
const defaultViewMode = ref<'grid' | 'list'>('grid')
const defaultPageSize = ref(20)

const fileList = ref<FileInfo[]>([])
const selectedFiles = ref<number[]>([])
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)
const loading = ref(false)

const searchParams = reactive({
  keyword: '',
  fileType: '',
  dateRange: '',
  sizeRange: '',
  uploader: '',
  tags: [],
  sortBy: '', // 新增排序字段
  sortOrder: '' // 新增排序顺序
})

// 计算属性
const fileUploadRef = ref()

// 方法
const loadFiles = async () => {
  loading.value = true
  try {
    const response = await getFileList({
      page: currentPage.value,
      size: pageSize.value,
      ...searchParams
    })
    fileList.value = response.data.list
    total.value = response.data.total
  } catch (error) {
    ElMessage.error('加载文件列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = (params: any) => {
  Object.assign(searchParams, params)
  currentPage.value = 1
  loadFiles()
}

const handleSearchReset = () => {
  Object.assign(searchParams, {
    keyword: '',
    fileType: '',
    dateRange: '',
    sizeRange: '',
    uploader: '',
    tags: [],
    sortBy: '', // 重置排序字段
    sortOrder: '' // 重置排序顺序
  })
  currentPage.value = 1
  loadFiles()
}

const handleFileChange = (file: any, fileList: any[]) => {
  // 文件选择变化处理
}

const handleUploadSuccess = (response: any, file: any, fileList: any[]) => {
  ElMessage.success('文件上传成功')
  loadFiles()
}

const handleUploadError = (error: any, file: any, fileList: any[]) => {
  ElMessage.error('文件上传失败')
}

const toggleFileSelection = (fileId: number) => {
  const index = selectedFiles.value.indexOf(fileId)
  if (index > -1) {
    selectedFiles.value.splice(index, 1)
  } else {
    selectedFiles.value.push(fileId)
  }
}

const handleSelectionChange = (selection: FileInfo[]) => {
  selectedFiles.value = selection.map(item => item.id)
}

const handleBatchDownload = async () => {
  if (selectedFiles.value.length === 0) {
    ElMessage.warning('请先选择要下载的文件')
    return
  }
  
  try {
    const response = await batchDownloadFiles(selectedFiles.value)
    // 创建下载链接
    const blob = new Blob([response.data])
    const downloadUrl = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = downloadUrl
    link.download = `批量下载_${new Date().toISOString().slice(0, 10)}.zip`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(downloadUrl)
    
    ElMessage.success('批量下载成功')
  } catch (error) {
    ElMessage.error('批量下载失败')
  }
}

const handleBatchDelete = async () => {
  if (selectedFiles.value.length === 0) {
    ElMessage.warning('请先选择要删除的文件')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedFiles.value.length} 个文件吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await batchDeleteFiles(selectedFiles.value)
    ElMessage.success('批量删除成功')
    selectedFiles.value = []
    loadFiles()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error('批量删除失败')
    }
  }
}

const handleSort = async (sortBy: string, sortOrder: 'asc' | 'desc') => {
  try {
    // 更新搜索参数并重新加载
    searchParams.sortBy = sortBy
    searchParams.sortOrder = sortOrder
    currentPage.value = 1
    await loadFiles()
    ElMessage.success(`已按${sortBy}${sortOrder === 'asc' ? '升序' : '降序'}排序`)
  } catch (error) {
    ElMessage.error('排序失败')
  }
}

const previewFile = (file: FileInfo) => {
  ElMessage.info(`预览文件: ${file.name}`)
}

const downloadFile = (file: FileInfo) => {
  ElMessage.info(`下载文件: ${file.name}`)
}

const deleteFile = async (file: FileInfo) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除文件 "${file.name}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await deleteFileApi(file.id)
    ElMessage.success('删除成功')
    loadFiles()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  loadFiles()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  loadFiles()
}

const saveSettings = async () => {
  try {
    // 保存设置到localStorage
    localStorage.setItem('fileManagerSettings', JSON.stringify({
      defaultViewMode: defaultViewMode.value,
      defaultPageSize: defaultPageSize.value
    }))
    
    // 应用设置
    viewMode.value = defaultViewMode.value
    pageSize.value = defaultPageSize.value
    showSettings.value = false
    
    ElMessage.success('设置保存成功')
  } catch (error) {
    ElMessage.error('设置保存失败')
  }
}

// 加载设置
const loadSettings = () => {
  try {
    const settings = localStorage.getItem('fileManagerSettings')
    if (settings) {
      const { defaultViewMode: savedViewMode, defaultPageSize: savedPageSize } = JSON.parse(settings)
      defaultViewMode.value = savedViewMode || 'grid'
      defaultPageSize.value = savedPageSize || 20
      viewMode.value = defaultViewMode.value
      pageSize.value = defaultPageSize.value
    }
  } catch (error) {
    console.error('加载设置失败:', error)
  }
}

// 文件图标和颜色
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

// 生命周期
onMounted(() => {
  loadSettings()
  loadFiles()
})
</script>

<style scoped lang="scss">
.file-manager {
  .page-header {
    margin-bottom: $spacing-xl;
    
    .page-title {
      font-size: $font-size-extra-large;
      font-weight: bold;
      color: $text-primary;
      margin-bottom: $spacing-sm;
    }
    
    .page-desc {
      color: $text-secondary;
      margin: 0;
    }
  }
  
  .upload-card {
    margin-bottom: $spacing-lg;
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
  
  .file-content {
    margin-bottom: $spacing-lg;
    
    .file-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
      gap: $spacing-md;
      
      .file-item {
        background: $background-white;
        border: 1px solid $border-color;
        border-radius: $border-radius-base;
        padding: $spacing-md;
        cursor: pointer;
        transition: all 0.3s ease;
        
        &:hover {
          border-color: $primary-color;
          box-shadow: $box-shadow-light;
        }
        
        &.selected {
          border-color: $primary-color;
          background-color: rgba($primary-color, 0.1);
        }
        
        .file-icon {
          text-align: center;
          margin-bottom: $spacing-sm;
        }
        
        .file-info {
          text-align: center;
          margin-bottom: $spacing-sm;
          
          .file-name {
            font-size: $font-size-small;
            color: $text-primary;
            margin-bottom: $spacing-xs;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
          
          .file-meta {
            font-size: $font-size-extra-small;
            color: $text-secondary;
            
            .file-size {
              margin-right: $spacing-sm;
            }
          }
        }
        
        .file-actions {
          display: flex;
          justify-content: center;
          gap: $spacing-xs;
        }
      }
    }
    
    .file-list {
      .file-name-cell {
        display: flex;
        align-items: center;
        gap: $spacing-sm;
        
        .file-icon-small {
          font-size: 16px;
        }
      }
    }
  }
  
  .pagination-wrapper {
    display: flex;
    justify-content: center;
    margin-top: $spacing-lg;
  }
}

// 响应式设计
@media (max-width: $breakpoint-md) {
.file-manager {
    .file-grid {
      grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
      gap: $spacing-sm;
      
      .file-item {
        padding: $spacing-sm;
        
        .file-info .file-name {
          font-size: $font-size-extra-small;
        }
        
        .file-actions {
          flex-direction: column;
          gap: $spacing-xs;
        }
      }
    }
  }
}
</style>
