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
      @create-folder="showCreateFolder = true"
      @sort="handleSort"
    />
    
    <!-- 文件内容区域 -->
    <div class="file-content">
      <!-- 网格视图 -->
      <FileGrid
        v-if="viewMode === 'grid'"
        :file-list="fileList"
        :selected-files="selectedFiles"
        :loading="loading"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        @update:current-page="currentPage = $event"
        @update:page-size="pageSize = $event"
        @selection-change="toggleFileSelection"
        @item-click="handleFileClick"
        @item-double-click="handleFileDoubleClick"
        @action="handleFileAction"
      />
      
      <!-- 列表视图 -->
      <FileListView
        v-else
        :file-list="fileList"
        :loading="loading"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        @update:current-page="currentPage = $event"
        @update:page-size="pageSize = $event"
        @selection-change="handleSelectionChange"
        @item-click="handleFileClick"
        @item-double-click="handleFileDoubleClick"
        @action="handleFileAction"
        @sort-change="handleSortChange"
      />
    </div>

    <!-- 文件预览对话框 -->
    <FilePreview
      v-model:visible="showPreview"
      :file="currentFile"
    />

    <!-- 创建文件夹对话框 -->
    <el-dialog
      v-model="showCreateFolder"
      title="创建文件夹"
      width="400px"
    >
      <el-form @submit.prevent="handleCreateFolder">
        <el-form-item label="文件夹名称">
          <el-input
            v-model="newFolderName"
            placeholder="请输入文件夹名称"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showCreateFolder = false">取消</el-button>
          <el-button type="primary" @click="handleCreateFolder">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import FileUpload from '@/components/file/FileUpload.vue'
import FileSearch from '@/components/file/FileSearch.vue'
import FileToolbar from '@/components/file/FileToolbar.vue'
import FileGrid from '@/components/file/FileGrid.vue'
import FileListView from '@/components/file/FileListView.vue'
import FilePreview from '@/components/file/FilePreview.vue'
import { 
  getFileList, 
  batchDownloadFiles, 
  batchDeleteFiles,
  createFolder,
  downloadFile
} from '@/api/file/file'
import type { FileInfo } from '@/api/types/file'

// 响应式数据
const showUpload = ref(true)
const viewMode = ref<'grid' | 'list'>('grid')
const showPreview = ref(false)
const showCreateFolder = ref(false)
const newFolderName = ref('')

const fileList = ref<FileInfo[]>([])
const selectedFiles = ref<number[]>([])
const currentFile = ref<FileInfo | null>(null)
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
  tags: [] as string[],
  sortBy: 'updateTime',
  sortOrder: 'desc' as 'asc' | 'desc'
})

const fileUploadRef = ref()

// 加载文件列表
const loadFiles = async () => {
  try {
    loading.value = true
    const response = await getFileList({
      page: currentPage.value,
      size: pageSize.value,
      ...searchParams
    })
    
    fileList.value = response.data.list || []
    total.value = response.data.total || 0
  } catch (error) {
    console.error('加载文件列表失败:', error)
    ElMessage.error('加载文件列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索处理
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
    sortBy: 'updatedTime',
    sortOrder: 'desc'
  })
  currentPage.value = 1
  loadFiles()
}

// 文件上传处理
const handleUploadSuccess = () => {
  ElMessage.success('文件上传成功')
  loadFiles()
}

const handleUploadError = (error: any) => {
  console.error('文件上传失败:', error)
  ElMessage.error('文件上传失败')
}

// 文件选择处理
const toggleFileSelection = (fileId: number) => {
  const index = selectedFiles.value.indexOf(fileId)
  if (index > -1) {
    selectedFiles.value.splice(index, 1)
  } else {
    selectedFiles.value.push(fileId)
  }
}

const handleSelectionChange = (selection: FileInfo[]) => {
  selectedFiles.value = selection.map(file => file.id)
}

// 文件操作处理
const handleFileClick = (file: FileInfo) => {
  currentFile.value = file
}

const handleFileDoubleClick = (file: FileInfo) => {
  if (file.fileType === 'folder') {
    // 进入文件夹
    // TODO: 实现文件夹导航
  } else {
    // 预览文件
    currentFile.value = file
    showPreview.value = true
  }
}

const handleFileAction = async (action: string, fileId: number) => {
  const file = fileList.value.find(f => f.id === fileId)
  if (!file) return

  try {
    switch (action) {
      case 'preview':
        currentFile.value = file
        showPreview.value = true
        break
        
      case 'download':
        await downloadFile(fileId)
        ElMessage.success('文件下载开始')
        break
        
      case 'delete':
        await ElMessageBox.confirm('确定要删除这个文件吗？', '确认删除', {
          type: 'warning'
        })
        await batchDeleteFiles([fileId])
        ElMessage.success('文件删除成功')
        loadFiles()
        break
        
      default:
        ElMessage.info(`功能开发中: ${action}`)
    }
  } catch (error) {
    console.error(`文件操作失败 (${action}):`, error)
    ElMessage.error(`操作失败: ${action}`)
  }
}

// 批量操作
const handleBatchDownload = async () => {
  if (selectedFiles.value.length === 0) {
    ElMessage.warning('请先选择要下载的文件')
    return
  }

  try {
    await batchDownloadFiles(selectedFiles.value)
    ElMessage.success('批量下载开始')
  } catch (error) {
    console.error('批量下载失败:', error)
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
      '确认批量删除',
      { type: 'warning' }
    )
    
    await batchDeleteFiles(selectedFiles.value)
    ElMessage.success('批量删除成功')
    selectedFiles.value = []
    loadFiles()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

// 创建文件夹
const handleCreateFolder = async () => {
  if (!newFolderName.value.trim()) {
    ElMessage.warning('请输入文件夹名称')
    return
  }

  try {
    await createFolder({ name: newFolderName.value })
    ElMessage.success('文件夹创建成功')
    showCreateFolder.value = false
    newFolderName.value = ''
    loadFiles()
  } catch (error) {
    console.error('创建文件夹失败:', error)
    ElMessage.error('创建文件夹失败')
  }
}

// 排序处理
const handleSort = (sort: any) => {
  searchParams.sortBy = sort.prop
  searchParams.sortOrder = sort.order
  loadFiles()
}

const handleSortChange = (sort: { prop: string; order: string }) => {
  handleSort(sort)
}

// 监听页码变化
watch([currentPage, pageSize], () => {
  loadFiles()
})

// 初始化
onMounted(() => {
  loadFiles()
})
</script>

<style scoped lang="scss">
@import './FileManager.scss';
</style>