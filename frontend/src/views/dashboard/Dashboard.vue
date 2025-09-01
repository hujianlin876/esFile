<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <div class="stats-grid">
      <StatCard
        :icon="Document"
        :number="stats.totalFiles"
        label="总文件数"
        icon-background="linear-gradient(135deg, #409eff, #67c23a)"
      />
      
      <StatCard
        :icon="User"
        :number="stats.totalUsers"
        label="总用户数"
        icon-background="linear-gradient(135deg, #67c23a, #e6a23c)"
      />
      
      <StatCard
        :icon="DataAnalysis"
        :number="formatFileSize(stats.totalSize)"
        label="总存储量"
        icon-background="linear-gradient(135deg, #e6a23c, #f56c6c)"
      />
      
      <StatCard
        :icon="TrendCharts"
        :number="stats.todayUploads"
        label="今日上传"
        icon-background="linear-gradient(135deg, #f56c6c, #909399)"
      />
    </div>

    <!-- 图表区域 -->
    <div class="charts-grid">
      <ChartCard
        title="文件类型分布"
        :option="pieChartOption"
        :height="300"
      >
        <template #header-actions>
          <el-button type="text" @click="refreshChartData">刷新</el-button>
        </template>
      </ChartCard>
      
      <ChartCard
        title="上传趋势"
        :option="lineChartOption"
        :height="300"
      >
        <template #header-actions>
          <el-select v-model="trendPeriod" size="small" @change="updateTrendChart">
            <el-option label="最近7天" value="7" />
            <el-option label="最近30天" value="30" />
            <el-option label="最近90天" value="90" />
          </el-select>
        </template>
      </ChartCard>
    </div>

    <!-- 最近活动 -->
    <ActivityList
      title="最近活动"
      :activities="recentActivities"
      @more="loadRecentActivities"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Document,
  User,
  DataAnalysis,
  TrendCharts
} from '@element-plus/icons-vue'
import StatCard from '@/components/dashboard/StatCard.vue'
import ChartCard from '@/components/dashboard/ChartCard.vue'
import ActivityList from '@/components/dashboard/ActivityList.vue'
import { getPieChartOption, getLineChartOption } from '@/utils/chart'
import { formatFileSize } from '@/utils/format'
import { getDashboardStats, getChartData, getRecentActivities } from '@/api/dashboard/dashboard'
import type { DashboardStats, ChartData, Activity } from '@/services/dashboard'

// 响应式数据
const stats = ref<DashboardStats>({
  totalFiles: 0,
  totalUsers: 0,
  totalSize: 0,
  todayUploads: 0
})

const chartData = ref<ChartData>({
  pieData: [],
  lineData: { dates: [], values: [] }
})

const activities = ref<Activity[]>([])
const loading = ref(false)
const selectedPeriod = ref<'day' | 'week' | 'month'>('week')
const trendPeriod = ref('7')
const recentActivities = ref<Activity[]>([])

// 计算属性
const pieChartOption = computed(() => getPieChartOption(chartData.value?.pieData || []))
const lineChartOption = computed(() => getLineChartOption(chartData.value?.lineData || { dates: [], values: [] }))

// 方法
const loadData = async () => {
  loading.value = true
  try {
    const [statsRes, chartRes, activitiesRes] = await Promise.all([
      getDashboardStats(),
      getChartData(selectedPeriod.value),
      getRecentActivities(10)
    ])
    
    stats.value = statsRes.data as unknown as DashboardStats
    chartData.value = chartRes.data as unknown as ChartData
    activities.value = activitiesRes.data as unknown as Activity[]
    recentActivities.value = activitiesRes.data as unknown as Activity[]
  } catch (error) {
    ElMessage.error('加载数据失败')
    // 确保即使API调用失败，数据也不会变成undefined
    if (!chartData.value) {
      chartData.value = {
        pieData: [],
        lineData: { dates: [], values: [] }
      }
    }
  } finally {
    loading.value = false
  }
}

// 周期变化处理
const handlePeriodChange = async (period: 'day' | 'week' | 'month') => {
  selectedPeriod.value = period
  try {
    const response = await getChartData(period)
    chartData.value = response.data as unknown as ChartData
  } catch (error) {
    ElMessage.error('更新趋势图失败')
    // 确保即使API调用失败，数据也不会变成undefined
    if (!chartData.value) {
      chartData.value = {
        pieData: [],
        lineData: { dates: [], values: [] }
      }
    }
  }
}

const loadRecentActivities = async () => {
  try {
    activities.value = await getRecentActivities() as unknown as Activity[]
  } catch (error) {
    ElMessage.error('加载活动记录失败')
  }
}

const updateTrendChart = () => {
  // TODO: 根据选择的周期更新趋势图
  loadData()
}

const refreshChartData = () => {
  loadData()
  ElMessage.success('图表数据已刷新')
}

// 生命周期
onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.dashboard {
  .stats-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: $spacing-lg;
    margin-bottom: $spacing-xl;
  }
  
  .charts-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
    gap: $spacing-lg;
    margin-bottom: $spacing-xl;
  }
}

// 响应式设计
@media (max-width: $breakpoint-md) {
  .dashboard {
    .stats-grid {
      grid-template-columns: 1fr;
    }
    
    .charts-grid {
      grid-template-columns: 1fr;
    }
  }
}
</style>
