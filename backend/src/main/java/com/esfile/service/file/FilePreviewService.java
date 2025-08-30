package com.esfile.service.file;

import java.util.Map;

/**
 * 文件预览服务接口
 */
public interface FilePreviewService {

    /**
     * 获取文件预览URL
     */
    String getPreviewUrl(Long fileId);

    /**
     * 获取文件预览信息
     */
    Map<String, Object> getFilePreviewInfo(Long fileId);

    /**
     * 生成文件预览
     */
    String generatePreview(Long fileId);

    /**
     * 获取文件缩略图
     */
    byte[] getThumbnail(Long fileId, Integer width, Integer height);

    /**
     * 生成文件缩略图
     */
    byte[] generateThumbnail(Long fileId, Integer width, Integer height);

    /**
     * 获取文件内容（文本文件）
     */
    String getFileContent(Long fileId, String encoding);

    /**
     * 获取文件内容（HTML格式）
     */
    String getFileContentAsHtml(Long fileId);

    /**
     * 获取文件内容（Markdown格式）
     */
    String getFileContentAsMarkdown(Long fileId);

    /**
     * 检查文件是否可预览
     */
    boolean canPreview(Long fileId);

    /**
     * 获取预览类型
     */
    String getPreviewType(Long fileId);

    /**
     * 更新预览次数
     */
    void updatePreviewCount(Long fileId);

    /**
     * 记录预览日志
     */
    void logPreview(Long fileId, Long userId, String ipAddress, String userAgent);

    /**
     * 获取预览配置
     */
    Map<String, Object> getPreviewConfig(Long fileId);

    /**
     * 设置预览配置
     */
    boolean setPreviewConfig(Long fileId, Map<String, Object> config);

    /**
     * 清理预览缓存
     */
    boolean cleanupPreviewCache(Long fileId);

    /**
     * 获取支持的预览格式
     */
    Map<String, Object> getSupportedPreviewFormats();
}
