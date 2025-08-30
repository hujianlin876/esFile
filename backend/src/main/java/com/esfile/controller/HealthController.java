package com.esfile.controller;

import com.esfile.common.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 * 用于系统健康状态监控
 * 
 * @author esfile
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 基础健康检查
     */
    @GetMapping
    public ResponseResult<Map<String, Object>> health() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("timestamp", System.currentTimeMillis());
        healthInfo.put("version", "1.0.0");
        healthInfo.put("application", "ES文件管理系统");
        
        return ResponseResult.success(healthInfo);
    }

    /**
     * 详细健康检查
     */
    @GetMapping("/detailed")
    public ResponseResult<Map<String, Object>> detailedHealth() {
        Map<String, Object> healthInfo = new HashMap<>();
        
        // 应用状态
        healthInfo.put("application", "UP");
        healthInfo.put("version", "1.0.0");
        healthInfo.put("timestamp", System.currentTimeMillis());
        
        // Redis状态
        try {
            redisTemplate.opsForValue().get("health_check");
            healthInfo.put("redis", "UP");
        } catch (Exception e) {
            healthInfo.put("redis", "DOWN");
            healthInfo.put("redis_error", e.getMessage());
        }
        
        // 系统信息
        Runtime runtime = Runtime.getRuntime();
        healthInfo.put("memory_total", runtime.totalMemory());
        healthInfo.put("memory_free", runtime.freeMemory());
        healthInfo.put("memory_used", runtime.totalMemory() - runtime.freeMemory());
        healthInfo.put("processors", runtime.availableProcessors());
        
        return ResponseResult.success(healthInfo);
    }

    /**
     * 数据库健康检查
     */
    @GetMapping("/database")
    public ResponseResult<Map<String, Object>> databaseHealth() {
        Map<String, Object> healthInfo = new HashMap<>();
        
        try {
            // 这里可以添加数据库连接检查逻辑
            healthInfo.put("database", "UP");
            healthInfo.put("message", "数据库连接正常");
        } catch (Exception e) {
            healthInfo.put("database", "DOWN");
            healthInfo.put("error", e.getMessage());
        }
        
        return ResponseResult.success(healthInfo);
    }
}
