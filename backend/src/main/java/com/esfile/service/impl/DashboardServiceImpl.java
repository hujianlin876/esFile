package com.esfile.service.impl;

import com.esfile.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 仪表板服务实现类
 * 作为协调器，调用各个具体的统计服务
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private FileStatsService fileStatsService;
    
    @Autowired
    private UserStatsService userStatsService;
    
    @Autowired
    private SystemStatsService systemStatsService;
    
    @Autowired
    private StorageStatsService storageStatsService;

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // 获取文件统计
            Map<String, Object> fileStats = fileStatsService.getFileStats();
            stats.putAll(fileStats);
            
            // 获取用户统计
            Map<String, Object> userStats = userStatsService.getUserStats();
            stats.putAll(userStats);
            
            // 获取系统统计
            Map<String, Object> sysStats = systemStatsService.getSystemStats();
            stats.putAll(sysStats);
            
            // 获取存储统计
            Map<String, Object> storageStats = storageStatsService.getStorageStats();
            stats.putAll(storageStats);
            
        } catch (Exception e) {
            stats.put("error", "获取仪表板统计信息失败: " + e.getMessage());
        }
        
        return stats;
    }

    @Override
    public Map<String, Object> getChartData(int days) {
        Map<String, Object> chartData = new HashMap<>();
        
        try {
            // 获取文件上传下载趋势
            Map<String, Object> uploadTrends = fileStatsService.getUploadDownloadTrends(days);
            chartData.put("uploadTrends", uploadTrends);
            
            // 获取文件类型分布
            Map<String, Object> fileTypeDistribution = fileStatsService.getFileTypeDistribution();
            chartData.put("fileTypeDistribution", fileTypeDistribution);
            
            // 获取存储使用趋势
            Map<String, Object> storageTrends = storageStatsService.getStorageTrends(days);
            chartData.put("storageTrends", storageTrends);
            
            // 获取用户活跃度
            Map<String, Object> userActivity = userStatsService.getUserActivity();
            chartData.put("userActivity", userActivity);
            
        } catch (Exception e) {
            chartData.put("error", "获取图表数据失败: " + e.getMessage());
        }
        
        return chartData;
    }

    @Override
    public Map<String, Object> getRecentActivity(int limit) {
        Map<String, Object> activity = new HashMap<>();
        
        try {
            // 获取最近的文件活动
            Map<String, Object> fileActivity = fileStatsService.getRecentFileActivity(limit);
            activity.put("fileActivity", fileActivity);
            
            // 获取最近的用户活动
            Map<String, Object> userActivity = userStatsService.getRecentUserActivity(limit);
            activity.put("userActivity", userActivity);
            
            // 获取系统日志
            Map<String, Object> systemLogs = systemStatsService.getRecentSystemLogs(limit);
            activity.put("systemLogs", systemLogs);
            
        } catch (Exception e) {
            activity.put("error", "获取最近活动失败: " + e.getMessage());
        }
        
        return activity;
    }

    @Override
    public Map<String, Object> getSystemHealth() {
        Map<String, Object> health = new HashMap<>();
        
        try {
            // 获取系统健康状态
            Map<String, Object> systemHealth = systemStatsService.getSystemHealth();
            health.putAll(systemHealth);
            
            // 获取存储健康状态
            Map<String, Object> storageHealth = storageStatsService.getStorageHealth();
            health.putAll(storageHealth);
            
            // 获取数据库健康状态
            Map<String, Object> databaseHealth = systemStatsService.getDatabaseHealth();
            health.putAll(databaseHealth);
            
        } catch (Exception e) {
            health.put("error", "获取系统健康状态失败: " + e.getMessage());
        }
        
        return health;
    }

    @Override
    public Map<String, Object> getPerformanceMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        try {
            // 获取性能指标
            Map<String, Object> performance = systemStatsService.getPerformanceMetrics();
            metrics.putAll(performance);
            
            // 获取响应时间统计
            Map<String, Object> responseTime = systemStatsService.getResponseTimeStats();
            metrics.putAll(responseTime);
            
            // 获取并发用户统计
            Map<String, Object> concurrentUsers = userStatsService.getConcurrentUserStats();
            metrics.putAll(concurrentUsers);
            
        } catch (Exception e) {
            metrics.put("error", "获取性能指标失败: " + e.getMessage());
        }
        
        return metrics;
    }
}
