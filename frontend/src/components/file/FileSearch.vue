<template>
  <div class="file-search">
    <el-form :model="searchForm" inline class="search-form">
      <el-form-item>
        <el-input
          v-model="searchForm.keyword"
          placeholder="搜索文件名、内容..."
          clearable
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      
      <el-form-item>
        <el-select
          v-model="searchForm.fileType"
          placeholder="文件类型"
          clearable
          style="width: 120px"
        >
          <el-option label="全部" value="" />
          <el-option label="文档" value="document" />
          <el-option label="图片" value="image" />
          <el-option label="视频" value="video" />
          <el-option label="音频" value="audio" />
          <el-option label="压缩包" value="archive" />
          <el-option label="其他" value="other" />
        </el-select>
      </el-form-item>
      
      <el-form-item>
        <el-select
          v-model="searchForm.dateRange"
          placeholder="时间范围"
          clearable
          style="width: 140px"
        >
          <el-option label="全部时间" value="" />
          <el-option label="今天" value="today" />
          <el-option label="昨天" value="yesterday" />
          <el-option label="最近7天" value="7days" />
          <el-option label="最近30天" value="30days" />
          <el-option label="最近90天" value="90days" />
        </el-select>
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleSearch">
          搜索
        </el-button>
        <el-button @click="handleReset">重置</el-button>
        <el-button
          type="text"
          @click="showAdvanced = !showAdvanced"
        >
          {{ showAdvanced ? '收起' : '高级搜索' }}
          <el-icon>
            <component :is="showAdvanced ? ArrowUp : ArrowDown" />
          </el-icon>
        </el-button>
      </el-form-item>
    </el-form>
    
    <!-- 高级搜索 -->
    <el-collapse-transition>
      <div v-show="showAdvanced" class="advanced-search">
        <el-form :model="searchForm" inline class="advanced-form">
          <el-form-item label="文件大小">
            <el-select
              v-model="searchForm.sizeRange"
              placeholder="大小范围"
              clearable
              style="width: 120px"
            >
              <el-option label="全部" value="" />
              <el-option label="小于1MB" value="0-1" />
              <el-option label="1MB-10MB" value="1-10" />
              <el-option label="10MB-100MB" value="10-100" />
              <el-option label="大于100MB" value="100+" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="上传者">
            <el-input
              v-model="searchForm.uploader"
              placeholder="上传者用户名"
              clearable
              style="width: 150px"
            />
          </el-form-item>
          
          <el-form-item label="标签">
            <el-select
              v-model="searchForm.tags"
              placeholder="选择标签"
              multiple
              clearable
              style="width: 200px"
            >
              <el-option label="重要" value="important" />
              <el-option label="工作" value="work" />
              <el-option label="个人" value="personal" />
              <el-option label="临时" value="temporary" />
            </el-select>
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-collapse-transition>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import { Search, ArrowUp, ArrowDown } from '@element-plus/icons-vue'

interface SearchForm {
  keyword: string
  fileType: string
  dateRange: string
  sizeRange: string
  uploader: string
  tags: string[]
}

interface Emits {
  search: [form: SearchForm]
  reset: []
}

const emit = defineEmits<Emits>()

// 响应式数据
const showAdvanced = ref(false)
const searchForm = reactive<SearchForm>({
  keyword: '',
  fileType: '',
  dateRange: '',
  sizeRange: '',
  uploader: '',
  tags: []
})

// 监听搜索条件变化，自动搜索
watch(
  () => [searchForm.fileType, searchForm.dateRange, searchForm.sizeRange, searchForm.uploader, searchForm.tags],
  () => {
    if (showAdvanced.value) {
      handleSearch()
    }
  },
  { deep: true }
)

// 方法
const handleSearch = () => {
  emit('search', { ...searchForm })
}

const handleReset = () => {
  Object.assign(searchForm, {
    keyword: '',
    fileType: '',
    dateRange: '',
    sizeRange: '',
    uploader: '',
    tags: []
  })
  emit('reset')
}
</script>

<style scoped lang="scss">
.file-search {
  margin-bottom: $spacing-lg;
  
  .search-form {
    margin-bottom: 0;
    
    .el-form-item {
      margin-bottom: $spacing-md;
      margin-right: $spacing-md;
    }
  }
  
  .advanced-search {
    background-color: $background-light;
    border-radius: $border-radius-base;
    padding: $spacing-lg;
    margin-top: $spacing-md;
    
    .advanced-form {
      .el-form-item {
        margin-bottom: $spacing-md;
        margin-right: $spacing-lg;
        
        .el-form-item__label {
          color: $text-regular;
          font-size: $font-size-small;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: $breakpoint-md) {
  .file-search {
    .search-form,
    .advanced-form {
      .el-form-item {
        margin-right: 0;
        width: 100%;
        
        .el-input,
        .el-select {
          width: 100%;
        }
      }
    }
  }
}
</style>
