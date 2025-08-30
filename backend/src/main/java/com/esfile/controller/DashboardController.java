package com.esfile.controller;

import com.esfile.common.vo.ResponseResult;
import com.esfile.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 仪表板控制器
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * 获取仪表板统计数据
     */
    @GetMapping("/stats")
    public ResponseResult<Map<String, Object>> getDashboardStats() {
        try {
            Map<String, Object> stats = dashboardService.getDashboardStats();
            return ResponseResult.success(stats);
        } catch (Exception e) {
            return ResponseResult.fail("获取统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取图表数据
     */
    @GetMapping("/charts")
    public ResponseResult<Map<String, Object>> getChartData(
            @RequestParam(defaultValue = "7") int days) {
        try {
            Map<String, Object> chartData = dashboardService.getChartData(days);
            return ResponseResult.success(chartData);
        } catch (Exception e) {
            return ResponseResult.fail("获取图表数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取最近活动
     */
    @GetMapping("/activities")
    public ResponseResult<Map<String, Object>> getRecentActivities(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            Map<String, Object> activities = dashboardService.getRecentActivities(limit);
            return ResponseResult.success(activities);
        } catch (Exception e) {
            return ResponseResult.fail("获取最近活动失败: " + e.getMessage());
        }
    }

    /**
     * 获取系统状态
     */
    @GetMapping("/system-status")
    public ResponseResult<Map<String, Object>> getSystemStatus() {
        try {
            Map<String, Object> status = dashboardService.getSystemStatus();
            return ResponseResult.success(status);
        } catch (Exception e) {
            return ResponseResult.fail("获取系统状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户活动统计
     */
    @GetMapping("/user-activity")
    public ResponseResult<Map<String, Object>> getUserActivityStats() {
        try {
            Map<String, Object> stats = dashboardService.getUserActivityStats();
            return ResponseResult.success(stats);
        } catch (Exception e) {
            return ResponseResult.fail("获取用户活动统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取上传趋势
     */
    @GetMapping("/upload-trends")
    public ResponseResult<Map<String, Object>> getUploadTrends(
            @RequestParam(defaultValue = "30") int days) {
        try {
            Map<String, Object> trends = dashboardService.getUploadTrends(days);
            return ResponseResult.success(trends);
        } catch (Exception e) {
            return ResponseResult.fail("获取上传趋势失败: " + e.getMessage());
        }
    }

    /**
     * 获取存储使用情况
     */
    @GetMapping("/storage-usage")
    public ResponseResult<Map<String, Object>> getStorageUsage() {
        try {
            Map<String, Object> usage = dashboardService.getStorageUsage();
            return ResponseResult.success(usage);
        } catch (Exception e) {
            return ResponseResult.fail("获取存储使用情况失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件类型分布
     */
    @GetMapping("/file-type-distribution")
    public ResponseResult<Map<String, Object>> getFileTypeDistribution() {
        try {
            Map<String, Object> distribution = dashboardService.getFileTypeDistribution();
            return ResponseResult.success(distribution);
        } catch (Exception e) {
            return ResponseResult.fail("获取文件类型分布失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户活跃度统计
     */
    @GetMapping("/user-activity-by-time")
    public ResponseResult<Map<String, Object>> getUserActivityByTime() {
        try {
            Map<String, Object> activity = dashboardService.getUserActivityByTime();
            return ResponseResult.success(activity);
        } catch (Exception e) {
            return ResponseResult.fail("获取用户活跃度统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取系统性能指标
     */
    @GetMapping("/system-performance")
    public ResponseResult<Map<String, Object>> getSystemPerformanceMetrics() {
        try {
            Map<String, Object> metrics = dashboardService.getSystemPerformanceMetrics();
            return ResponseResult.success(metrics);
        } catch (Exception e) {
            return ResponseResult.fail("获取系统性能指标失败: " + e.getMessage());
        }
    }

    /**
     * 获取安全状态信息
     */
    @GetMapping("/security-status")
    public ResponseResult<Map<String, Object>> getSecurityStatus() {
        try {
            Map<String, Object> security = dashboardService.getSecurityStatus();
            return ResponseResult.success(security);
        } catch (Exception e) {
            return ResponseResult.fail("获取安全状态信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取备份状态
     */
    @GetMapping("/backup-status")
    public ResponseResult<Map<String, Object>> getBackupStatus() {
        try {
            Map<String, Object> backup = dashboardService.getBackupStatus();
            return ResponseResult.success(backup);
        } catch (Exception e) {
            return ResponseResult.fail("获取备份状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取网络状态
     */
    @GetMapping("/network-status")
    public ResponseResult<Map<String, Object>> getNetworkStatus() {
        try {
            Map<String, Object> network = dashboardService.getNetworkStatus();
            return ResponseResult.success(network);
        } catch (Exception e) {
            return ResponseResult.fail("获取网络状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取磁盘使用情况
     */
    @GetMapping("/disk-usage")
    public ResponseResult<Map<String, Object>> getDiskUsage() {
        try {
            Map<String, Object> disk = dashboardService.getDiskUsage();
            return ResponseResult.success(disk);
        } catch (Exception e) {
            return ResponseResult.fail("获取磁盘使用情况失败: " + e.getMessage());
        }
    }

    /**
     * 获取内存使用情况
     */
    @GetMapping("/memory-usage")
    public ResponseResult<Map<String, Object>> getMemoryUsage() {
        try {
            Map<String, Object> memory = dashboardService.getMemoryUsage();
            return ResponseResult.success(memory);
        } catch (Exception e) {
            return ResponseResult.fail("获取内存使用情况失败: " + e.getMessage());
        }
    }

    /**
     * 获取CPU使用情况
     */
    @GetMapping("/cpu-usage")
    public ResponseResult<Map<String, Object>> getCpuUsage() {
        try {
            Map<String, Object> cpu = dashboardService.getCpuUsage();
            return ResponseResult.success(cpu);
        } catch (Exception e) {
            return ResponseResult.fail("获取CPU使用情况失败: " + e.getMessage());
        }
    }

    /**
     * 获取数据库状态
     */
    @GetMapping("/database-status")
    public ResponseResult<Map<String, Object>> getDatabaseStatus() {
        try {
            Map<String, Object> db = dashboardService.getDatabaseStatus();
            return ResponseResult.success(db);
        } catch (Exception e) {
            return ResponseResult.fail("获取数据库状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取缓存状态
     */
    @GetMapping("/cache-status")
    public ResponseResult<Map<String, Object>> getCacheStatus() {
        try {
            Map<String, Object> cache = dashboardService.getCacheStatus();
            return ResponseResult.success(cache);
        } catch (Exception e) {
            return ResponseResult.fail("获取缓存状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取队列状态
     */
    @GetMapping("/queue-status")
    public ResponseResult<Map<String, Object>> getQueueStatus() {
        try {
            Map<String, Object> queue = dashboardService.getQueueStatus();
            return ResponseResult.success(queue);
        } catch (Exception e) {
            return ResponseResult.fail("获取队列状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取定时任务状态
     */
    @GetMapping("/scheduled-task-status")
    public ResponseResult<Map<String, Object>> getScheduledTaskStatus() {
        try {
            Map<String, Object> tasks = dashboardService.getScheduledTaskStatus();
            return ResponseResult.success(tasks);
        } catch (Exception e) {
            return ResponseResult.fail("获取定时任务状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取API调用统计
     */
    @GetMapping("/api-call-stats")
    public ResponseResult<Map<String, Object>> getApiCallStats() {
        try {
            Map<String, Object> api = dashboardService.getApiCallStats();
            return ResponseResult.success(api);
        } catch (Exception e) {
            return ResponseResult.fail("获取API调用统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取错误日志统计
     */
    @GetMapping("/error-log-stats")
    public ResponseResult<Map<String, Object>> getErrorLogStats() {
        try {
            Map<String, Object> errors = dashboardService.getErrorLogStats();
            return ResponseResult.success(errors);
        } catch (Exception e) {
            return ResponseResult.fail("获取错误日志统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户登录统计
     */
    @GetMapping("/user-login-stats")
    public ResponseResult<Map<String, Object>> getUserLoginStats() {
        try {
            Map<String, Object> login = dashboardService.getUserLoginStats();
            return ResponseResult.success(login);
        } catch (Exception e) {
            return ResponseResult.fail("获取用户登录统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件操作统计
     */
    @GetMapping("/file-operation-stats")
    public ResponseResult<Map<String, Object>> getFileOperationStats() {
        try {
            Map<String, Object> operations = dashboardService.getFileOperationStats();
            return ResponseResult.success(operations);
        } catch (Exception e) {
            return ResponseResult.fail("获取文件操作统计失败: " + e.getMessage());
        }
    }
}
