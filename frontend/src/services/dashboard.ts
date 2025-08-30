/**
 * 仪表板数据服务
 */

export interface DashboardStats {
  totalFiles: number
  totalUsers: number
  totalSize: number
  todayUploads: number
}

export interface ChartData {
  pieData: Array<{ name: string; value: number }>
  lineData: { dates: string[]; values: number[] }
}

export interface Activity {
  id: number
  type: string
  title: string
  description: string
  createTime: string
}

/**
 * 获取仪表板统计数据
 */
export const getDashboardStats = async (): Promise<DashboardStats> => {
  // TODO: 调用真实API
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        totalFiles: 1250,
        totalUsers: 45,
        totalSize: 1024 * 1024 * 1024 * 2.5, // 2.5GB
        todayUploads: 23
      })
    }, 500)
  })
}

/**
 * 获取图表数据
 */
export const getChartData = async (): Promise<ChartData> => {
  // TODO: 调用真实API
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        pieData: [
          { value: 335, name: '文档' },
          { value: 310, name: '图片' },
          { value: 234, name: '视频' },
          { value: 135, name: '音频' },
          { value: 148, name: '其他' }
        ],
        lineData: {
          dates: ['2024-01-01', '2024-01-02', '2024-01-03', '2024-01-04', '2024-01-05', '2024-01-06', '2024-01-07'],
          values: [12, 19, 15, 25, 22, 30, 28]
        }
      })
    }, 500)
  })
}

/**
 * 获取最近活动
 */
export const getRecentActivities = async (): Promise<Activity[]> => {
  // TODO: 调用真实API
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve([
        {
          id: 1,
          type: 'upload',
          title: '文件上传',
          description: '用户张三上传了文件 "项目报告.docx"',
          createTime: new Date().toISOString()
        },
        {
          id: 2,
          type: 'download',
          title: '文件下载',
          description: '用户李四下载了文件 "产品手册.pdf"',
          createTime: new Date(Date.now() - 3600000).toISOString()
        },
        {
          id: 3,
          type: 'edit',
          title: '文件编辑',
          description: '用户王五编辑了文件 "会议记录.xlsx"',
          createTime: new Date(Date.now() - 7200000).toISOString()
        }
      ])
    }, 500)
  })
}
