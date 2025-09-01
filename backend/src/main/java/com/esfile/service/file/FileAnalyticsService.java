package com.esfile.service.file;

import java.util.List;
import java.util.Map;

/**
 * 文件分析统计服务接口
 * 负责文件的统计分析功能
 * 
 * @author esfile
 * @since 1.0.0
 */
public interface FileAnalyticsService {

    /**
     * 获取文件统计信息
     */
    Map<String, Object> getFileStats();

    /**
     * 获取文件类型统计
     */
    List<Map<String, Object>> getFileTypeStats();

    /**
     * 获取存储使用情况统计
     */
    Map<String, Object> getStorageUsageStats();

    /**
     * 查找重复文件
     */
    List<Map<String, Object>> findDuplicateFiles();

    /**
     * 获取文件访问日志
     */
    List<Map<String, Object>> getFileAccessLogs(Long fileId, int page, int size);
}

