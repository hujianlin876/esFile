package com.esfile.controller.database;

import com.esfile.common.vo.ResponseResult;
import com.esfile.service.database.DatabaseToolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 数据库工具控制器
 */
@RestController
@RequestMapping("/api/database/tools")
public class DatabaseToolsController {

    @Autowired
    private DatabaseToolsService databaseToolsService;

    /**
     * SQL格式化
     */
    @PostMapping("/format-sql")
    public ResponseResult<Map<String, Object>> formatSql(@RequestBody Map<String, String> request) {
        try {
            String sql = request.get("sql");
            Map<String, Object> result = databaseToolsService.formatSql(sql);
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.fail("SQL格式化失败: " + e.getMessage());
        }
    }

    /**
     * 导出查询结果
     */
    @PostMapping("/export-result")
    public ResponseResult<Map<String, Object>> exportQueryResult(@RequestBody Map<String, Object> request) {
        try {
            String format = (String) request.get("format");
            List<Map<String, Object>> data = (List<Map<String, Object>>) request.get("data");
            
            Map<String, Object> result = databaseToolsService.exportQueryResult(format, data);
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.fail("导出查询结果失败: " + e.getMessage());
        }
    }

    /**
     * SQL分析
     */
    @PostMapping("/analyze-sql")
    public ResponseResult<Map<String, Object>> analyzeSql(@RequestBody Map<String, String> request) {
        try {
            String sql = request.get("sql");
            Map<String, Object> result = databaseToolsService.analyzeSql(sql);
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.fail("SQL分析失败: " + e.getMessage());
        }
    }

    /**
     * 获取SQL模板
     */
    @GetMapping("/templates")
    public ResponseResult<List<Map<String, Object>>> getSqlTemplates() {
        try {
            List<Map<String, Object>> templates = databaseToolsService.getSqlTemplates();
            return ResponseResult.success(templates);
        } catch (Exception e) {
            return ResponseResult.fail("获取SQL模板失败: " + e.getMessage());
        }
    }

    /**
     * 创建SQL模板
     */
    @PostMapping("/templates")
    public ResponseResult<String> createSqlTemplate(@RequestBody Map<String, Object> template) {
        try {
            boolean success = databaseToolsService.createSqlTemplate(template);
            if (success) {
                return ResponseResult.success("SQL模板创建成功");
            } else {
                return ResponseResult.fail("SQL模板创建失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("创建SQL模板失败: " + e.getMessage());
        }
    }

    /**
     * 更新SQL模板
     */
    @PutMapping("/templates/{id}")
    public ResponseResult<String> updateSqlTemplate(
            @PathVariable Long id,
            @RequestBody Map<String, Object> template) {
        try {
            boolean success = databaseToolsService.updateSqlTemplate(id, template);
            if (success) {
                return ResponseResult.success("SQL模板更新成功");
            } else {
                return ResponseResult.fail("SQL模板更新失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("更新SQL模板失败: " + e.getMessage());
        }
    }

    /**
     * 删除SQL模板
     */
    @DeleteMapping("/templates/{id}")
    public ResponseResult<String> deleteSqlTemplate(@PathVariable Long id) {
        try {
            boolean success = databaseToolsService.deleteSqlTemplate(id);
            if (success) {
                return ResponseResult.success("SQL模板删除成功");
            } else {
                return ResponseResult.fail("SQL模板删除失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("删除SQL模板失败: " + e.getMessage());
        }
    }

    /**
     * 获取SQL执行历史
     */
    @GetMapping("/history")
    public ResponseResult<List<Map<String, Object>>> getSqlHistory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            List<Map<String, Object>> history = databaseToolsService.getSqlHistory(page, size);
            return ResponseResult.success(history);
        } catch (Exception e) {
            return ResponseResult.fail("获取SQL执行历史失败: " + e.getMessage());
        }
    }

    /**
     * 清空SQL执行历史
     */
    @DeleteMapping("/history")
    public ResponseResult<String> clearSqlHistory() {
        try {
            boolean success = databaseToolsService.clearSqlHistory();
            if (success) {
                return ResponseResult.success("SQL执行历史清空成功");
            } else {
                return ResponseResult.fail("SQL执行历史清空失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("清空SQL执行历史失败: " + e.getMessage());
        }
    }

    /**
     * 获取连接池状态
     */
    @GetMapping("/connection-pool/status")
    public ResponseResult<Map<String, Object>> getConnectionPoolStatus() {
        try {
            Map<String, Object> status = databaseToolsService.getConnectionPoolStatus();
            return ResponseResult.success(status);
        } catch (Exception e) {
            return ResponseResult.fail("获取连接池状态失败: " + e.getMessage());
        }
    }

    /**
     * 重置连接池
     */
    @PostMapping("/connection-pool/reset")
    public ResponseResult<String> resetConnectionPool() {
        try {
            boolean success = databaseToolsService.resetConnectionPool();
            if (success) {
                return ResponseResult.success("连接池重置成功");
            } else {
                return ResponseResult.fail("连接池重置失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("重置连接池失败: " + e.getMessage());
        }
    }
}
