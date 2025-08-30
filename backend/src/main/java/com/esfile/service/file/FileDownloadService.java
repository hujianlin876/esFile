package com.esfile.service.file;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文件下载服务接口
 */
public interface FileDownloadService {

    /**
     * 下载单个文件
     */
    void downloadFile(Long id, HttpServletResponse response);

    /**
     * 批量下载文件
     */
    void batchDownloadFiles(List<Long> ids, HttpServletResponse response);

    /**
     * 下载文件夹（压缩包）
     */
    void downloadFolder(Long folderId, HttpServletResponse response);

    /**
     * 预览文件
     */
    void previewFile(Long id, HttpServletResponse response);

    /**
     * 获取文件流
     */
    byte[] getFileBytes(Long id);

    /**
     * 获取文件输入流
     */
    java.io.InputStream getFileInputStream(Long id);

    /**
     * 检查文件是否存在
     */
    boolean fileExists(Long id);

    /**
     * 检查下载权限
     */
    boolean checkDownloadPermission(Long fileId, Long userId);

    /**
     * 更新下载次数
     */
    void updateDownloadCount(Long fileId);

    /**
     * 记录下载日志
     */
    void logDownload(Long fileId, Long userId, String ipAddress, String userAgent);

    /**
     * 获取文件下载链接
     */
    String getDownloadUrl(Long fileId, Long userId, Integer expireMinutes);

    /**
     * 验证下载链接
     */
    boolean validateDownloadUrl(String downloadUrl);

    /**
     * 限速下载
     */
    void downloadWithSpeedLimit(Long id, HttpServletResponse response, Long speedLimit);

    /**
     * 断点续传下载
     */
    void downloadWithRange(Long id, HttpServletResponse response, String range);
}
