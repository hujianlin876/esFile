package com.esfile.service.file;

import com.esfile.entity.dto.FileSearchDto;
import com.esfile.entity.dto.FileUploadDto;
import com.esfile.entity.mybatis.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 文件管理服务接口
 */
public interface FileService {

    /**
     * 获取文件列表（分页）
     */
    Map<String, Object> getFileList(FileSearchDto searchDto);

    /**
     * 根据ID获取文件详情
     */
    FileInfo getFileDetail(Long id);

    /**
     * 上传单个文件
     */
    FileInfo uploadFile(FileUploadDto uploadDto);

    /**
     * 批量上传文件
     */
    List<FileInfo> batchUploadFiles(List<FileUploadDto> uploadDtos);

    /**
     * 下载文件
     */
    void downloadFile(Long id, HttpServletResponse response);

    /**
     * 批量下载文件
     */
    void batchDownloadFiles(List<Long> ids, HttpServletResponse response);

    /**
     * 删除文件
     */
    boolean deleteFile(Long id, Long userId);

    /**
     * 批量删除文件
     */
    boolean batchDeleteFiles(List<Long> ids, Long userId);

    /**
     * 更新文件信息
     */
    FileInfo updateFile(FileInfo fileInfo);

    /**
     * 移动文件
     */
    boolean moveFile(Long id, Long targetFolderId, Long userId);

    /**
     * 复制文件
     */
    FileInfo copyFile(Long id, Long targetFolderId, Long userId);

    /**
     * 获取文件预览信息
     */
    Map<String, Object> getFilePreview(Long id);

    /**
     * 获取文件内容（文本文件）
     */
    String getFileContent(Long id);

    /**
     * 获取文件缩略图
     */
    byte[] getFileThumbnail(Long id);

    /**
     * 搜索文件
     */
    Map<String, Object> searchFiles(FileSearchDto searchDto);

    /**
     * 获取文件统计信息
     */
    Map<String, Object> getFileStats();

    /**
     * 获取文件夹结构
     */
    List<Map<String, Object>> getFolderStructure(Long parentId);

    /**
     * 创建文件夹
     */
    FileInfo createFolder(String folderName, Long parentId, Long userId);

    /**
     * 删除文件夹
     */
    boolean deleteFolder(Long folderId, Long userId);

    /**
     * 获取文件类型统计
     */
    List<Map<String, Object>> getFileTypeStats();

    /**
     * 获取存储使用统计
     */
    Map<String, Object> getStorageUsageStats();

    /**
     * 文件去重
     */
    List<Map<String, Object>> findDuplicateFiles();

    /**
     * 清理临时文件
     */
    boolean cleanupTempFiles();

    /**
     * 文件备份
     */
    boolean backupFile(Long id);

    /**
     * 文件恢复
     */
    boolean restoreFile(Long id);

    /**
     * 获取文件访问日志
     */
    List<Map<String, Object>> getFileAccessLogs(Long fileId, int page, int size);

    /**
     * 设置文件权限
     */
    boolean setFilePermission(Long fileId, Long userId, String permission);

    /**
     * 获取文件权限
     */
    Map<String, Object> getFilePermission(Long fileId);

    /**
     * 文件分享
     */
    Map<String, Object> shareFile(Long fileId, Long userId, String shareType, Integer expireDays);

    /**
     * 取消文件分享
     */
    boolean cancelFileShare(Long shareId, Long userId);

    /**
     * 获取分享文件列表
     */
    List<Map<String, Object>> getSharedFiles(Long userId);

    /**
     * 文件标签管理
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
     * 根据标签搜索文件
     */
    List<FileInfo> getFilesByTag(String tag);
}
