<template>
  <el-card class="chart-card">
    <template #header>
      <div class="card-header">
        <span>{{ title }}</span>
        <slot name="header-actions" />
      </div>
    </template>
    <div class="chart-container">
      <div ref="chartRef" class="chart"></div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue'
import * as echarts from 'echarts'
import type { EChartsOption } from 'echarts'

interface Props {
  title: string
  option: EChartsOption
  height?: string | number
}

const props = withDefaults(defineProps<Props>(), {
  height: 300
})

const chartRef = ref<HTMLElement>()
let chart: echarts.ECharts | null = null

// 监听配置变化
watch(() => props.option, (newOption) => {
  if (chart) {
    chart.setOption(newOption)
  }
}, { deep: true })

// 初始化图表
const initChart = () => {
  if (!chartRef.value) return
  
  chart = echarts.init(chartRef.value)
  chart.setOption(props.option)
}

// 调整图表大小
const resizeChart = () => {
  chart?.resize()
}

onMounted(() => {
  initChart()
  window.addEventListener('resize', resizeChart)
})

onUnmounted(() => {
  window.removeEventListener('resize', resizeChart)
  chart?.dispose()
})
</script>

<style scoped lang="scss">
.chart-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .chart-container {
    height: v-bind(height + 'px');
    
    .chart {
      width: 100%;
      height: 100%;
    }
  }
}
</style>
