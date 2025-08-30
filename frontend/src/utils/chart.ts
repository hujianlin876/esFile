import type { EChartsOption } from 'echarts'

/**
 * 饼图配置
 * @param data 数据
 * @returns ECharts配置选项
 */
export const getPieChartOption = (data: Array<{ name: string; value: number }>): EChartsOption => {
  return {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      data: data.map(item => item.name)
    },
    series: [
      {
        name: '文件类型',
        type: 'pie',
        radius: '50%',
        data: data,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
}

/**
 * 折线图配置
 * @param data 数据
 * @returns ECharts配置选项
 */
export const getLineChartOption = (data: { dates: string[]; values: number[] }): EChartsOption => {
  return {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: data.dates
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '上传数量',
        type: 'line',
        data: data.values,
        smooth: true,
        areaStyle: {
          opacity: 0.3
        }
      }
    ]
  }
}
