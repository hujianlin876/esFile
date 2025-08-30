<template>
  <div class="file-toolbar">
    <div class="toolbar-left">
      <el-button-group>
        <el-button
          :type="viewMode === 'grid' ? 'primary' : ''"
          :icon="Grid"
          @click="changeViewMode('grid')"
        >
          网格视图
        </el-button>
        <el-button
          :type="viewMode === 'list' ? 'primary' : ''"
          :icon="List"
          @click="changeViewMode('list')"
        >
          列表视图
        </el-button>
      </el-button-group>
      
      <el-divider direction="vertical" />
      
      <el-button
        :icon="Refresh"
        @click="$emit('refresh')"
      >
        刷新
      </el-button>
      
      <el-button
        :icon="Download"
        :disabled="!hasSelection"
        @click="handleBatchDownload"
      >
        批量下载
      </el-button>
      
      <el-button
        :icon="Delete"
        type="danger"
        :disabled="!hasSelection"
        @click="handleBatchDelete"
      >
        批量删除
      </el-button>
    </div>
    
    <div class="toolbar-right">
      <el-button-group>
        <el-button
          :icon="Sort"
          @click="showSortMenu = !showSortMenu"
        >
          排序
          <el-icon class="el-icon--right">
            <ArrowDown />
          </el-icon>
        </el-button>
        
        <el-dropdown
          v-model="showSortMenu"
          trigger="click"
          @command="handleSort"
        >
          <span class="el-dropdown-link">
            <el-icon class="el-icon--right">
              <ArrowDown />
            </el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="name-asc">文件名 A-Z</el-dropdown-item>
              <el-dropdown-item command="name-desc">文件名 Z-A</el-dropdown-item>
              <el-dropdown-item command="size-asc">大小 小到大</el-dropdown-item>
              <el-dropdown-item command="size-desc">大小 大到小</el-dropdown-item>
              <el-dropdown-item command="date-asc">时间 早到晚</el-dropdown-item>
              <el-dropdown-item command="date-desc">时间 晚到早</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-button-group>
      
      <el-divider direction="vertical" />
      
      <el-button
        :icon="Setting"
        @click="$emit('settings')"
      >
        设置
      </el-button>
    </div>
    
    <!-- 批量删除确认对话框 -->
    <el-dialog
      v-model="deleteDialogVisible"
      title="确认删除"
      width="400px"
    >
      <p>确定要删除选中的 {{ selectedCount }} 个文件吗？此操作不可恢复。</p>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deleteDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="confirmBatchDelete">确定删除</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Grid,
  List,
  Refresh,
  Download,
  Delete,
  Sort,
  ArrowDown,
  Setting
} from '@element-plus/icons-vue'

interface Props {
  viewMode: 'grid' | 'list'
  selectedCount: number
}

interface Emits {
  'update:viewMode': [mode: 'grid' | 'list']
  refresh: []
  'batch-download': []
  'batch-delete': []
  settings: []
  sort: [sortBy: string, sortOrder: 'asc' | 'desc']
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// 响应式数据
const showSortMenu = ref(false)
const deleteDialogVisible = ref(false)

// 计算属性
const hasSelection = computed(() => props.selectedCount > 0)

// 方法
const changeViewMode = (mode: 'grid' | 'list') => {
  emit('update:viewMode', mode)
}

const handleBatchDownload = () => {
  if (props.selectedCount === 0) {
    ElMessage.warning('请先选择要下载的文件')
    return
  }
  emit('batch-download')
}

const handleBatchDelete = () => {
  if (props.selectedCount === 0) {
    ElMessage.warning('请先选择要删除的文件')
    return
  }
  deleteDialogVisible.value = true
}

const confirmBatchDelete = () => {
  emit('batch-delete')
  deleteDialogVisible.value = false
}

const handleSort = (command: string) => {
  const [sortBy, sortOrder] = command.split('-')
  emit('sort', sortBy, sortOrder as 'asc' | 'desc')
  showSortMenu.value = false
}
</script>

<style scoped lang="scss">
.file-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: $spacing-md;
  background-color: $background-white;
  border: 1px solid $border-color;
  border-radius: $border-radius-base;
  margin-bottom: $spacing-lg;
  
  .toolbar-left,
  .toolbar-right {
    display: flex;
    align-items: center;
    gap: $spacing-sm;
  }
  
  .el-divider--vertical {
    height: 20px;
    margin: 0 $spacing-sm;
  }
  
  .el-button-group {
    .el-button {
      border-radius: 0;
      
      &:first-child {
        border-top-left-radius: $border-radius-base;
        border-bottom-left-radius: $border-radius-base;
      }
      
      &:last-child {
        border-top-right-radius: $border-radius-base;
        border-bottom-right-radius: $border-radius-base;
      }
    }
  }
}

// 响应式设计
@media (max-width: $breakpoint-md) {
  .file-toolbar {
    flex-direction: column;
    gap: $spacing-md;
    
    .toolbar-left,
    .toolbar-right {
      width: 100%;
      justify-content: center;
    }
  }
}
</style>
