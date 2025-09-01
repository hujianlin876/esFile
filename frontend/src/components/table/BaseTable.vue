<template>
  <div class="base-table">
    <!-- 表格工具栏 -->
    <div v-if="showToolbar" class="table-toolbar">
      <div class="toolbar-left">
        <slot name="toolbar-left" />
      </div>
      <div class="toolbar-right">
        <slot name="toolbar-right" />
        <el-button
          v-if="showRefresh"
          :icon="Refresh"
          @click="handleRefresh"
        >
          刷新
        </el-button>
        <el-button
          v-if="showColumnSetting"
          :icon="Setting"
          @click="showColumnSettingDialog = true"
        >
          列设置
        </el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <el-table
      ref="tableRef"
      v-loading="loading"
      :data="data"
      :height="height"
      :max-height="maxHeight"
      :stripe="stripe"
      :border="border"
      :size="size"
      :fit="fit"
      :show-header="showHeader"
      :highlight-current-row="highlightCurrentRow"
      :current-row-key="currentRowKey"
      :row-class-name="rowClassName"
      :row-style="rowStyle"
      :cell-class-name="cellClassName"
      :cell-style="cellStyle"
      :header-row-class-name="headerRowClassName"
      :header-row-style="headerRowStyle"
      :header-cell-class-name="headerCellClassName"
      :header-cell-style="headerCellStyle"
      :row-key="rowKey"
      :empty-text="emptyText"
      :default-expand-all="defaultExpandAll"
      :expand-row-keys="expandRowKeys"
      :default-sort="defaultSort"
      :tooltip-effect="tooltipEffect"
      :show-summary="showSummary"
      :sum-text="sumText"
      :summary-method="summaryMethod"
      :span-method="spanMethod"
      :select-on-indeterminate="selectOnIndeterminate"
      :indent="indent"
      :lazy="lazy"
      :load="load"
      :tree-props="treeProps"
      :table-layout="tableLayout"
      :scrollbar-always-on="scrollbarAlwaysOn"
      :flexible="flexible"
      @select="handleSelect"
      @select-all="handleSelectAll"
      @selection-change="handleSelectionChange"
      @cell-mouse-enter="handleCellMouseEnter"
      @cell-mouse-leave="handleCellMouseLeave"
      @cell-click="handleCellClick"
      @cell-dblclick="handleCellDblclick"
      @row-click="handleRowClick"
      @row-contextmenu="handleRowContextmenu"
      @row-dblclick="handleRowDblclick"
      @header-click="handleHeaderClick"
      @header-contextmenu="handleHeaderContextmenu"
      @sort-change="handleSortChange"
      @filter-change="handleFilterChange"
      @expand-change="handleExpandChange"
    >
      <!-- 选择列 -->
      <el-table-column
        v-if="showSelection"
        type="selection"
        width="55"
        align="center"
        fixed="left"
      />
      
      <!-- 序号列 -->
      <el-table-column
        v-if="showIndex"
        type="index"
        label="序号"
        width="60"
        align="center"
        fixed="left"
      />

      <!-- 动态列 -->
      <template v-for="column in visibleColumns" :key="column.prop">
        <el-table-column
          v-bind="column"
          :show-overflow-tooltip="column.showOverflowTooltip !== false"
        >
          <template #default="scope" v-if="column.slot">
            <slot :name="column.slot" :row="scope.row" :column="scope.column" :$index="scope.$index" />
          </template>
        </el-table-column>
      </template>

      <!-- 操作列 -->
      <el-table-column
        v-if="showActions"
        label="操作"
        :width="actionsWidth"
        :fixed="actionsFixed"
        align="center"
      >
        <template #default="scope">
          <slot name="actions" :row="scope.row" :column="scope.column" :$index="scope.$index" />
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页器 -->
    <div v-if="showPagination" class="table-pagination">
      <el-pagination
        :current-page="currentPage"
        :page-size="pageSize"
        :page-sizes="pageSizes"
        :total="total"
        :layout="paginationLayout"
        :background="paginationBackground"
        :small="paginationSmall"
        :disabled="paginationDisabled"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 列设置对话框 -->
    <el-dialog
      :model-value="showColumnSettingDialog"
      title="列设置"
      width="500px"
      @update:model-value="showColumnSettingDialog = $event"
    >
      <el-checkbox-group v-model="visibleColumnKeys">
        <el-checkbox
          v-for="column in allColumns"
          :key="column.prop"
          :label="column.prop"
        >
          {{ column.label }}
        </el-checkbox>
      </el-checkbox-group>
      <template #footer>
        <el-button @click="showColumnSettingDialog = false">取消</el-button>
        <el-button type="primary" @click="handleColumnSettingConfirm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, nextTick } from 'vue'
import { Refresh, Setting } from '@element-plus/icons-vue'
import type { TableInstance, TableColumnCtx } from 'element-plus'

interface Column extends Partial<TableColumnCtx<any>> {
  prop: string
  label: string
  slot?: string
  showOverflowTooltip?: boolean
}

interface Props {
  data: any[]
  columns: Column[]
  loading?: boolean
  height?: string | number
  maxHeight?: string | number
  stripe?: boolean
  border?: boolean
  size?: 'large' | 'default' | 'small'
  fit?: boolean
  showHeader?: boolean
  highlightCurrentRow?: boolean
  currentRowKey?: string | number
  rowClassName?: string | ((params: any) => string)
  rowStyle?: any
  cellClassName?: string | ((params: any) => string)
  cellStyle?: any
  headerRowClassName?: string | ((params: any) => string)
  headerRowStyle?: any
  headerCellClassName?: string | ((params: any) => string)
  headerCellStyle?: any
  rowKey?: string | ((row: any) => string)
  emptyText?: string
  defaultExpandAll?: boolean
  expandRowKeys?: string[]
  defaultSort?: any
  tooltipEffect?: 'dark' | 'light'
  showSummary?: boolean
  sumText?: string
  summaryMethod?: (params: any) => string[]
  spanMethod?: (params: any) => number[] | { rowspan: number; colspan: number }
  selectOnIndeterminate?: boolean
  indent?: number
  lazy?: boolean
  load?: (row: any, treeNode: any, resolve: (data: any[]) => void) => void
  treeProps?: object
  tableLayout?: 'fixed' | 'auto'
  scrollbarAlwaysOn?: boolean
  flexible?: boolean
  
  // 自定义属性
  showToolbar?: boolean
  showRefresh?: boolean
  showColumnSetting?: boolean
  showSelection?: boolean
  showIndex?: boolean
  showActions?: boolean
  actionsWidth?: string | number
  actionsFixed?: boolean | 'left' | 'right'
  showPagination?: boolean
  total?: number
  currentPage?: number
  pageSize?: number
  pageSizes?: number[]
  paginationLayout?: string
  paginationBackground?: boolean
  paginationSmall?: boolean
  paginationDisabled?: boolean
}

interface Emits {
  (e: 'select', selection: any[], row: any): void
  (e: 'select-all', selection: any[]): void
  (e: 'selection-change', selection: any[]): void
  (e: 'cell-mouse-enter', row: any, column: any, cell: any, event: Event): void
  (e: 'cell-mouse-leave', row: any, column: any, cell: any, event: Event): void
  (e: 'cell-click', row: any, column: any, cell: any, event: Event): void
  (e: 'cell-dblclick', row: any, column: any, cell: any, event: Event): void
  (e: 'row-click', row: any, column: any, event: Event): void
  (e: 'row-contextmenu', row: any, column: any, event: Event): void
  (e: 'row-dblclick', row: any, column: any, event: Event): void
  (e: 'header-click', column: any, event: Event): void
  (e: 'header-contextmenu', column: any, event: Event): void
  (e: 'sort-change', sort: any): void
  (e: 'filter-change', filters: any): void
  (e: 'expand-change', row: any, expandedRows: any[]): void
  (e: 'refresh'): void
  (e: 'size-change', size: number): void
  (e: 'current-change', page: number): void
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  stripe: true,
  border: true,
  size: 'default',
  fit: true,
  showHeader: true,
  highlightCurrentRow: true,
  showToolbar: true,
  showRefresh: true,
  showColumnSetting: true,
  showSelection: false,
  showIndex: false,
  showActions: false,
  actionsWidth: 150,
  actionsFixed: 'right',
  showPagination: true,
  total: 0,
  currentPage: 1,
  pageSize: 10,
  pageSizes: () => [10, 20, 50, 100],
  paginationLayout: 'total, sizes, prev, pager, next, jumper',
  paginationBackground: true,
  paginationSmall: false,
  paginationDisabled: false
})

const emit = defineEmits<Emits>()

// 响应式数据
const tableRef = ref<TableInstance>()
const showColumnSettingDialog = ref(false)
const visibleColumnKeys = ref<string[]>([])

// 计算属性
const allColumns = computed(() => props.columns)
const visibleColumns = computed(() => {
  if (visibleColumnKeys.value.length === 0) {
    return props.columns
  }
  return props.columns.filter(col => visibleColumnKeys.value.includes(col.prop))
})

// 监听器
watch(() => props.columns, (columns) => {
  if (columns.length > 0 && visibleColumnKeys.value.length === 0) {
    visibleColumnKeys.value = columns.map(col => col.prop)
  }
}, { immediate: true })

// 方法
const handleRefresh = () => {
  emit('refresh')
}

const handleSelect = (selection: any[], row: any) => {
  emit('select', selection, row)
}

const handleSelectAll = (selection: any[]) => {
  emit('select-all', selection)
}

const handleSelectionChange = (selection: any[]) => {
  emit('selection-change', selection)
}

const handleCellMouseEnter = (row: any, column: any, cell: any, event: Event) => {
  emit('cell-mouse-enter', row, column, cell, event)
}

const handleCellMouseLeave = (row: any, column: any, cell: any, event: Event) => {
  emit('cell-mouse-leave', row, column, cell, event)
}

const handleCellClick = (row: any, column: any, cell: any, event: Event) => {
  emit('cell-click', row, column, cell, event)
}

const handleCellDblclick = (row: any, column: any, cell: any, event: Event) => {
  emit('cell-dblclick', row, column, cell, event)
}

const handleRowClick = (row: any, column: any, event: Event) => {
  emit('row-click', row, column, event)
}

const handleRowContextmenu = (row: any, column: any, event: Event) => {
  emit('row-contextmenu', row, column, event)
}

const handleRowDblclick = (row: any, column: any, event: Event) => {
  emit('row-dblclick', row, column, event)
}

const handleHeaderClick = (column: any, event: Event) => {
  emit('header-click', column, event)
}

const handleHeaderContextmenu = (column: any, event: Event) => {
  emit('header-contextmenu', column, event)
}

const handleSortChange = (sort: any) => {
  emit('sort-change', sort)
}

const handleFilterChange = (filters: any) => {
  emit('filter-change', filters)
}

const handleExpandChange = (row: any, expandedRows: any[]) => {
  emit('expand-change', row, expandedRows)
}

const handleSizeChange = (size: number) => {
  emit('size-change', size)
}

const handleCurrentChange = (page: number) => {
  emit('current-change', page)
}

const handleColumnSettingConfirm = () => {
  showColumnSettingDialog.value = false
}

// 暴露方法
defineExpose({
  tableRef,
  refresh: handleRefresh
})
</script>

<style scoped lang="scss">
.base-table {
  .table-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: $spacing-md;
    padding: $spacing-md;
    background-color: $background-white;
    border-radius: $border-radius-base;
    box-shadow: $box-shadow-light;

    .toolbar-left {
      display: flex;
      align-items: center;
      gap: $spacing-sm;
    }

    .toolbar-right {
      display: flex;
      align-items: center;
      gap: $spacing-sm;
    }
  }

  .table-pagination {
    display: flex;
    justify-content: center;
    margin-top: $spacing-lg;
    padding: $spacing-md;
    background-color: $background-white;
    border-radius: $border-radius-base;
    box-shadow: $box-shadow-light;
  }
}
</style>
