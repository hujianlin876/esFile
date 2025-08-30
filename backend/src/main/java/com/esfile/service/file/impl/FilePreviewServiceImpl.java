package com.esfile.service.file.impl;

import com.esfile.entity.mybatis.FileInfo;
import com.esfile.mapper.FileInfoMapper;
import com.esfile.service.file.FilePreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.imageio.ImageIO;

/**
 * 文件预览服务实现类
 * 专门处理文件预览相关功能
 */
@Service
public class FilePreviewServiceImpl implements FilePreviewService {

    @Autowired
    private FileInfoMapper fileInfoMapper;

    /**
     * 获取文件预览URL
     */
    @Override
    public String getPreviewUrl(Long fileId) {
        FileInfo fileInfo = fileInfoMapper.selectById(fileId);
        if (fileInfo == null) {
            throw new RuntimeException("文件不存在");
        }
        
        if (!canPreview(fileId)) {
            throw new RuntimeException("该文件类型不支持预览");
        }
        
        return generatePreviewUrl(fileInfo);
    }

    /**
     * 获取文件预览信息
     */
    @Override
    public Map<String, Object> getFilePreviewInfo(Long fileId) {
        FileInfo fileInfo = fileInfoMapper.selectById(fileId);
        if (fileInfo == null) {
            throw new RuntimeException("文件不存在");
        }
        
        Map<String, Object> previewInfo = new HashMap<>();
        previewInfo.put("fileId", fileId);
        previewInfo.put("fileName", fileInfo.getFileName());
        previewInfo.put("fileType", fileInfo.getFileType());
        previewInfo.put("fileSize", fileInfo.getFileSize());
        previewInfo.put("canPreview", canPreview(fileId));
        previewInfo.put("previewType", getPreviewType(fileId));
        previewInfo.put("previewUrl", generatePreviewUrl(fileInfo));
        previewInfo.put("thumbnailUrl", "/api/files/" + fileId + "/thumbnail");
        
        return previewInfo;
    }

    /**
     * 生成文件预览
     */
    @Override
    public String generatePreview(Long fileId) {
        FileInfo fileInfo = fileInfoMapper.selectById(fileId);
        if (fileInfo == null) {
            throw new RuntimeException("文件不存在");
        }
        
        // TODO: 实现文件预览生成逻辑
        // 这里暂时返回预览URL
        return generatePreviewUrl(fileInfo);
    }

    /**
     * 获取文件缩略图
     */
    @Override
    public byte[] getThumbnail(Long fileId, Integer width, Integer height) {
        if (width == null) width = 200;
        if (height == null) height = 200;
        
        return generateThumbnail(fileId, width, height);
    }

    /**
     * 生成文件缩略图
     */
    @Override
    public byte[] generateThumbnail(Long fileId, Integer width, Integer height) {
        FileInfo fileInfo = fileInfoMapper.selectById(fileId);
        if (fileInfo == null) {
            throw new RuntimeException("文件不存在");
        }
        
        // 根据文件类型生成不同的缩略图
        String fileType = fileInfo.getFileType();
        if (fileType != null && fileType.startsWith("image/")) {
            return generateImageThumbnail(fileId, width, height);
        } else if (fileType != null && fileType.startsWith("text/")) {
            return generateTextThumbnail(fileId, width, height);
        } else {
            return generateDefaultThumbnail(fileInfo, width, height);
        }
    }

    /**
     * 获取文件内容（文本文件）
     */
    @Override
    public String getFileContent(Long fileId, String encoding) {
        if (encoding == null) {
            encoding = StandardCharsets.UTF_8.name();
        }
        
        // TODO: 从MinIO获取文件内容
        // 这里暂时返回模拟内容
        return "文件内容预览功能暂未实现，需要集成MinIO";
    }

    /**
     * 获取文件内容（HTML格式）
     */
    @Override
    public String getFileContentAsHtml(Long fileId) {
        String content = getFileContent(fileId, StandardCharsets.UTF_8.name());
        return "<html><body><pre>" + content + "</pre></body></html>";
    }

    /**
     * 获取文件内容（Markdown格式）
     */
    @Override
    public String getFileContentAsMarkdown(Long fileId) {
        String content = getFileContent(fileId, StandardCharsets.UTF_8.name());
        return "```\n" + content + "\n```";
    }

    /**
     * 检查文件是否可预览
     */
    @Override
    public boolean canPreview(Long fileId) {
        FileInfo fileInfo = fileInfoMapper.selectById(fileId);
        if (fileInfo == null) {
            return false;
        }
        
        String fileType = fileInfo.getFileType();
        if (fileType == null) {
            return false;
        }
        
        // 支持预览的文件类型
        return fileType.startsWith("image/") || 
               fileType.startsWith("text/") || 
               fileType.equals("application/pdf") ||
               fileType.contains("word") ||
               fileType.contains("excel") ||
               fileType.contains("powerpoint");
    }

    /**
     * 获取预览类型
     */
    @Override
    public String getPreviewType(Long fileId) {
        FileInfo fileInfo = fileInfoMapper.selectById(fileId);
        if (fileInfo == null) {
            return "unknown";
        }
        
        String fileType = fileInfo.getFileType();
        if (fileType == null) {
            return "unknown";
        }
        
        if (fileType.startsWith("image/")) {
            return "image";
        } else if (fileType.startsWith("text/")) {
            return "text";
        } else if (fileType.equals("application/pdf")) {
            return "pdf";
        } else if (fileType.contains("word")) {
            return "document";
        } else if (fileType.contains("excel")) {
            return "spreadsheet";
        } else if (fileType.contains("powerpoint")) {
            return "presentation";
        } else {
            return "unknown";
        }
    }

    /**
     * 更新预览次数
     */
    @Override
    public void updatePreviewCount(Long fileId) {
        fileInfoMapper.updatePreviewCount(fileId);
    }

    /**
     * 记录预览日志
     */
    @Override
    public void logPreview(Long fileId, Long userId, String ipAddress, String userAgent) {
        // TODO: 实现预览日志记录
        System.out.println("预览日志: 文件ID=" + fileId + ", 用户ID=" + userId + 
                          ", IP=" + ipAddress + ", UserAgent=" + userAgent);
    }

    /**
     * 获取预览配置
     */
    @Override
    public Map<String, Object> getPreviewConfig(Long fileId) {
        Map<String, Object> config = new HashMap<>();
        config.put("enablePreview", true);
        config.put("maxPreviewSize", 10 * 1024 * 1024); // 10MB
        config.put("thumbnailWidth", 200);
        config.put("thumbnailHeight", 200);
        config.put("previewQuality", "medium");
        return config;
    }

    /**
     * 设置预览配置
     */
    @Override
    public boolean setPreviewConfig(Long fileId, Map<String, Object> config) {
        // TODO: 实现预览配置保存
        return true;
    }

    /**
     * 清理预览缓存
     */
    @Override
    public boolean cleanupPreviewCache(Long fileId) {
        // TODO: 实现预览缓存清理
        return true;
    }

    /**
     * 获取支持的预览格式
     */
    @Override
    public Map<String, Object> getSupportedPreviewFormats() {
        Map<String, Object> formats = new HashMap<>();
        
        // 图片格式
        formats.put("image", new String[]{"jpg", "jpeg", "png", "gif", "bmp", "webp"});
        
        // 文档格式
        formats.put("document", new String[]{"pdf", "doc", "docx", "txt", "rtf"});
        
        // 表格格式
        formats.put("spreadsheet", new String[]{"xls", "xlsx", "csv"});
        
        // 演示格式
        formats.put("presentation", new String[]{"ppt", "pptx"});
        
        // 文本格式
        formats.put("text", new String[]{"txt", "md", "html", "css", "js", "java", "py", "xml", "json"});
        
        return formats;
    }

    /**
     * 生成预览URL
     */
    private String generatePreviewUrl(FileInfo fileInfo) {
        return "/api/files/" + fileInfo.getId() + "/preview";
    }

    /**
     * 生成图片缩略图
     */
    private byte[] generateImageThumbnail(Long fileId, Integer width, Integer height) {
        // TODO: 实现图片缩略图生成
        // 这里暂时返回默认缩略图
        return generateDefaultThumbnail(null, width, height);
    }

    /**
     * 生成文本缩略图
     */
    private byte[] generateTextThumbnail(Long fileId, Integer width, Integer height) {
        // TODO: 实现文本缩略图生成
        // 这里暂时返回默认缩略图
        return generateDefaultThumbnail(null, width, height);
    }

    /**
     * 生成默认缩略图
     */
    private byte[] generateDefaultThumbnail(FileInfo fileInfo, Integer width, Integer height) {
        try {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            
            // 设置背景色
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, width, height);
            
            // 设置文字
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            
            String text = fileInfo != null ? fileInfo.getFileName() : "文件预览";
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
            
            int x = (width - textWidth) / 2;
            int y = (height + textHeight) / 2;
            
            g2d.drawString(text, x, y);
            g2d.dispose();
            
            // 转换为字节数组
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            return baos.toByteArray();
            
        } catch (IOException e) {
            throw new RuntimeException("生成缩略图失败", e);
        }
    }
}
