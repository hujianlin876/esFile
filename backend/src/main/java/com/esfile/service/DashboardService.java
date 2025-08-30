package com.esfile.service;

import java.util.Map;

/**
 * 仪表板服务接口
 * 提供核心的仪表板功能
 */
public interface DashboardService {

    /**
     * 获取仪表板统计数据
     */
    Map<String, Object> getDashboardStats();

    /**
     * 获取图表数据
     */
    Map<String, Object> getChartData(int days);

    /**
     * 获取最近活动
     */
    Map<String, Object> getRecentActivity(int limit);

    /**
     * 获取系统健康状态
     */
    Map<String, Object> getSystemHealth();

    /**
     * 获取性能指标
     */
    Map<String, Object> getPerformanceMetrics();
}
