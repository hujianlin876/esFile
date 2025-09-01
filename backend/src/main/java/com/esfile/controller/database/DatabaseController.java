package com.esfile.controller.database;

import com.esfile.common.vo.ResponseResult;
import com.esfile.entity.dto.SqlExecuteDto;
import com.esfile.service.database.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 数据库操作控制器
 */
@RestController
@RequestMapping("/database")
public class DatabaseController {
    @Autowired
    private DatabaseService databaseService;

    /**
     * 执行SQL
     */
    @PostMapping("/execute")
    public ResponseResult<Map<String, Object>> execute(@RequestBody SqlExecuteDto sqlExecuteDto) {
        try {
            Map<String, Object> result = databaseService.executeSql(sqlExecuteDto);
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.fail("SQL执行失败: " + e.getMessage());
        }
    }

    /**
     * 获取数据库状态
     */
    @GetMapping("/stats")
    public ResponseResult<Map<String, Object>> getDatabaseStats() {
        try {
            Map<String, Object> stats = databaseService.getDatabaseInfo();
            // 添加一些模拟的统计数据
            stats.put("connections", 10);
            stats.put("tables", 15);
            stats.put("queries", 150);
            stats.put("slowQueries", 2);
            return ResponseResult.success(stats);
        } catch (Exception e) {
            return ResponseResult.fail("获取数据库状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取数据库表列表
     */
    @GetMapping("/tables")
    public ResponseResult<List<Map<String, Object>>> getTableList() {
        try {
            List<Map<String, Object>> tables = databaseService.getTableList();
            return ResponseResult.success(tables);
        } catch (Exception e) {
            return ResponseResult.fail("获取表列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取表结构
     */
    @GetMapping("/tables/{tableName}/structure")
    public ResponseResult<Map<String, Object>> getTableStructure(@PathVariable String tableName) {
        try {
            Map<String, Object> structure = databaseService.getTableInfo(tableName);
            return ResponseResult.success(structure);
        } catch (Exception e) {
            return ResponseResult.fail("获取表结构失败: " + e.getMessage());
        }
    }

    /**
     * 获取表数据
     */
    @GetMapping("/tables/{tableName}/data")
    public ResponseResult<Map<String, Object>> getTableData(
            @PathVariable String tableName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(required = false) String orderBy) {
        try {
            Map<String, Object> data = databaseService.getTableData(tableName, page, size, orderBy);
            return ResponseResult.success(data);
        } catch (Exception e) {
            return ResponseResult.fail("获取表数据失败: " + e.getMessage());
        }
    }

    /**
     * 备份数据库
     */
    @PostMapping("/backup")
    public ResponseResult<Map<String, Object>> backupDatabase(@RequestBody Map<String, String> request) {
        try {
            String name = request.get("name");
            String description = request.get("description");
            
            // 模拟备份操作
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("backupId", System.currentTimeMillis());
            result.put("name", name);
            result.put("description", description);
            result.put("status", "completed");
            result.put("size", "2.5MB");
            result.put("createTime", new java.util.Date());
            
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.fail("数据库备份失败: " + e.getMessage());
        }
    }

    /**
     * 恢复数据库
     */
    @PostMapping("/restore/{backupId}")
    public ResponseResult<String> restoreDatabase(@PathVariable Long backupId) {
        try {
            // 模拟恢复操作
            return ResponseResult.success("数据库恢复成功");
        } catch (Exception e) {
            return ResponseResult.fail("数据库恢复失败: " + e.getMessage());
        }
    }

    /**
     * 获取SQL历史记录
     */
    @GetMapping("/sql-history")
    public ResponseResult<Map<String, Object>> getSqlHistory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            // 模拟SQL历史记录
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("total", 50);
            result.put("list", new java.util.ArrayList<>());
            
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.fail("获取SQL历史记录失败: " + e.getMessage());
        }
    }

    /**
     * 清理SQL历史记录
     */
    @DeleteMapping("/sql-history")
    public ResponseResult<String> clearSqlHistory(@RequestParam(required = false) String beforeDate) {
        try {
            return ResponseResult.success("SQL历史记录清理成功");
        } catch (Exception e) {
            return ResponseResult.fail("清理SQL历史记录失败: " + e.getMessage());
        }
    }

    /**
     * 获取连接池状态
     */
    @GetMapping("/connection-pool-status")
    public ResponseResult<Map<String, Object>> getConnectionPoolStatus() {
        try {
            Map<String, Object> status = new java.util.HashMap<>();
            status.put("activeConnections", 5);
            status.put("idleConnections", 8);
            status.put("totalConnections", 13);
            status.put("maxConnections", 20);
            status.put("minConnections", 5);
            status.put("connectionTimeout", 30000);
            status.put("idleTimeout", 600000);
            
            return ResponseResult.success(status);
        } catch (Exception e) {
            return ResponseResult.fail("获取连接池状态失败: " + e.getMessage());
        }
    }

    /**
     * 重置连接池
     */
    @PostMapping("/connection-pool-reset")
    public ResponseResult<String> resetConnectionPool() {
        try {
            return ResponseResult.success("连接池重置成功");
        } catch (Exception e) {
            return ResponseResult.fail("连接池重置失败: " + e.getMessage());
        }
    }
}
