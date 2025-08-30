package com.esfile.service.system;

import java.util.Map;

/**
 * 系统服务接口
 * 定义系统配置管理的所有方法
 */
public interface SystemService {
    
    /**
     * 获取系统信息
     */
    Map<String, Object> getSystemInfo();
    
    /**
     * 获取系统配置
     */
    Map<String, Object> getSystemConfig();
    
    /**
     * 更新系统配置
     */
    boolean updateSystemConfig(Map<String, Object> config);
    
    /**
     * 获取系统状态
     */
    Map<String, Object> getSystemStatus();
    
    /**
     * 获取系统日志
     */
    Map<String, Object> getSystemLogs(String level, String startTime, String endTime, int page, int size);
    
    /**
     * 清理系统日志
     */
    boolean clearSystemLogs(String level, String beforeTime);
    
    /**
     * 系统备份
     */
    Map<String, Object> backupSystem();
    
    /**
     * 系统恢复
     */
    boolean restoreSystem(String backupPath);
}
