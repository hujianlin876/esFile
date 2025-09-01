package com.esfile.service.file;

import com.esfile.entity.mybatis.FileInfo;
import java.util.List;
import java.util.Map;

/**
 * 文件操作服务接口
 * 负责文件的基础操作：移动、复制、删除等
 * 
 * @author esfile
 * @since 1.0.0
 */
public interface FileOperationService {

    /**
     * 删除文件
     */
    boolean deleteFile(Long id, Long userId);

    /**
     * 批量删除文件
     */
    boolean batchDeleteFiles(List<Long> ids, Long userId);

    /**
     * 移动文件
     */
    boolean moveFile(Long id, Long targetFolderId, Long userId);

    /**
     * 复制文件
     */
    FileInfo copyFile(Long id, Long targetFolderId, Long userId);

    /**
     * 更新文件信息
     */
    FileInfo updateFile(FileInfo fileInfo);

    /**
     * 创建文件夹
     */
    FileInfo createFolder(String folderName, Long parentId, Long userId);

    /**
     * 删除文件夹
     */
    boolean deleteFolder(Long folderId, Long userId);

    /**
     * 获取文件夹结构
     */
    List<Map<String, Object>> getFolderStructure(Long parentId);

    /**
     * 清理临时文件
     */
    boolean cleanupTempFiles();

    /**
     * 备份文件
     */
    boolean backupFile(Long id);

    /**
     * 恢复文件
     */
    boolean restoreFile(Long id);
}

