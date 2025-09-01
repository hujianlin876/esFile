package com.esfile.service.file.impl;

import com.esfile.entity.mybatis.FileInfo;
import com.esfile.mapper.FileInfoMapper;
import com.esfile.service.file.FileAnalyticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文件分析统计服务实现类
 * 负责文件的统计分析功能
 * 
 * @author esfile
 * @since 1.0.0
 */
@Service
public class FileAnalyticsServiceImpl implements FileAnalyticsService {

    private static final Logger logger = LoggerFactory.getLogger(FileAnalyticsServiceImpl.class);

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Override
    public Map<String, Object> getFileStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // 获取文件总数
            long totalFiles = fileInfoMapper.selectCount();
            stats.put("totalFiles", totalFiles);
            
            // 获取总存储大小
            long totalSize = fileInfoMapper.selectTotalSize();
            stats.put("totalSize", totalSize);
            
            // 获取今日上传数量
            long todayUploads = fileInfoMapper.selectTodayUploads();
            stats.put("todayUploads", todayUploads);
            
            // 获取文件夹总数
            long totalFolders = fileInfoMapper.selectFolderCount();
            stats.put("totalFolders", totalFolders);
            
            return stats;
        } catch (Exception e) {
            logger.error("获取文件统计失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public List<Map<String, Object>> getFileTypeStats() {
        try {
            List<Map<String, Object>> stats = new ArrayList<>();
            
            // 获取所有文件
            List<FileInfo> files = fileInfoMapper.selectAll();
            
            // 按文件类型分组统计
            Map<String, Long> typeCount = files.stream()
                .filter(file -> !"folder".equals(file.getFileType()))
                .collect(Collectors.groupingBy(
                    FileInfo::getFileType,
                    Collectors.counting()
                ));
            
            // 计算大小统计
            Map<String, Long> typeSize = files.stream()
                .filter(file -> !"folder".equals(file.getFileType()))
                .collect(Collectors.groupingBy(
                    FileInfo::getFileType,
                    Collectors.summingLong(FileInfo::getFileSize)
                ));
            
            // 构建结果
            for (Map.Entry<String, Long> entry : typeCount.entrySet()) {
                Map<String, Object> stat = new HashMap<>();
                stat.put("fileType", entry.getKey());
                stat.put("count", entry.getValue());
                stat.put("size", typeSize.getOrDefault(entry.getKey(), 0L));
                stats.add(stat);
            }
            
            return stats;
        } catch (Exception e) {
            logger.error("获取文件类型统计失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, Object> getStorageUsageStats() {
        try {
            Map<String, Object> usage = new HashMap<>();
            
            // 获取总使用量
            long totalUsed = fileInfoMapper.selectTotalSize();
            usage.put("totalUsed", totalUsed);
            
            // 模拟存储限制（可从配置文件读取）
            long totalLimit = 100L * 1024 * 1024 * 1024; // 100GB
            usage.put("totalLimit", totalLimit);
            
            // 计算使用率
            double usageRate = totalLimit > 0 ? (double) totalUsed / totalLimit : 0;
            usage.put("usageRate", usageRate);
            
            // 获取按月份的使用统计
            List<Map<String, Object>> monthlyStats = fileInfoMapper.selectMonthlyUploadStats();
            usage.put("monthlyStats", monthlyStats);
            
            return usage;
        } catch (Exception e) {
            logger.error("获取存储使用统计失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public List<Map<String, Object>> findDuplicateFiles() {
        try {
            List<Map<String, Object>> duplicates = new ArrayList<>();
            
            // 获取所有文件按MD5分组
            List<FileInfo> files = fileInfoMapper.selectAll();
            Map<String, List<FileInfo>> md5Groups = files.stream()
                .filter(file -> file.getFileMd5() != null && !"folder".equals(file.getFileType()))
                .collect(Collectors.groupingBy(FileInfo::getFileMd5));
            
            // 找出重复文件
            for (Map.Entry<String, List<FileInfo>> entry : md5Groups.entrySet()) {
                if (entry.getValue().size() > 1) {
                    Map<String, Object> duplicate = new HashMap<>();
                    duplicate.put("md5", entry.getKey());
                    duplicate.put("files", entry.getValue());
                    duplicate.put("count", entry.getValue().size());
                    duplicate.put("totalSize", entry.getValue().get(0).getFileSize() * entry.getValue().size());
                    duplicates.add(duplicate);
                }
            }
            
            return duplicates;
        } catch (Exception e) {
            logger.error("查找重复文件失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Map<String, Object>> getFileAccessLogs(Long fileId, int page, int size) {
        try {
            // TODO: 实现文件访问日志获取逻辑
            // 这里需要先实现访问日志表和相关功能
            logger.info("获取文件访问日志: fileId={}, page={}, size={}", fileId, page, size);
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("获取文件访问日志失败", e);
            return new ArrayList<>();
        }
    }
}

