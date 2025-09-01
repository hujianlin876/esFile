package com.esfile.service.file;

import java.util.List;
import java.util.Map;

/**
 * 文件权限管理服务接口
 * 负责文件权限控制和分享功能
 * 
 * @author esfile
 * @since 1.0.0
 */
public interface FilePermissionService {

    /**
     * 设置文件权限
     */
    boolean setFilePermission(Long fileId, Long userId, String permission);

    /**
     * 获取文件权限
     */
    Map<String, Object> getFilePermission(Long fileId);

    /**
     * 分享文件
     */
    Map<String, Object> shareFile(Long fileId, Long userId, String shareType, Integer expireDays);

    /**
     * 取消文件分享
     */
    boolean cancelFileShare(Long shareId, Long userId);

    /**
     * 获取分享的文件列表
     */
    List<Map<String, Object>> getSharedFiles(Long userId);

    /**
     * 添加文件标签
     */
    boolean addFileTag(Long fileId, String tag);

    /**
     * 移除文件标签
     */
    boolean removeFileTag(Long fileId, String tag);

    /**
     * 获取文件标签
     */
    List<String> getFileTags(Long fileId);

    /**
     * 根据标签获取文件
     */
    List<com.esfile.entity.mybatis.FileInfo> getFilesByTag(String tag);
}

