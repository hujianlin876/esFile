package com.esfile.service.file.impl;

import com.esfile.entity.mybatis.FileInfo;
import com.esfile.mapper.FileInfoMapper;
import com.esfile.service.file.FileDownloadService;
import com.esfile.service.file.MinioStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件下载服务实现类
 * 专门处理文件下载相关功能，集成MinIO存储
 */
@Service
public class FileDownloadServiceImpl implements FileDownloadService {

    private static final Logger logger = LoggerFactory.getLogger(FileDownloadServiceImpl.class);

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Autowired
    private MinioStorageService minioStorageService;

    /**
     * 下载单个文件
     */
    @Override
    public void downloadFile(Long id, HttpServletResponse response) {
        FileInfo fileInfo = fileInfoMapper.selectById(id);
        if (fileInfo == null) {
            throw new RuntimeException("文件不存在");
        }

        // 设置响应头
        setDownloadHeaders(response, fileInfo.getFileName(), fileInfo.getFileSize());
        
        try (InputStream inputStream = minioStorageService.downloadFile(fileInfo.getObjectName());
             OutputStream outputStream = response.getOutputStream()) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            
            // 更新下载次数
            updateDownloadCount(id);
            
            logger.info("文件下载成功: {}, 大小: {} bytes", fileInfo.getFileName(), fileInfo.getFileSize());
            
        } catch (IOException e) {
            logger.error("文件下载失败: {}", fileInfo.getFileName(), e);
            throw new RuntimeException("文件下载失败: " + e.getMessage());
        }
    }

    /**
     * 批量下载文件
     */
    @Override
    public void batchDownloadFiles(List<Long> ids, HttpServletResponse response) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("文件ID列表不能为空");
        }

        // 设置响应头
        String zipFileName = "files_" + System.currentTimeMillis() + ".zip";
        response.setContentType("application/zip");
        try {
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + 
                             URLEncoder.encode(zipFileName, StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            response.setHeader("Content-Disposition", "attachment; filename=" + zipFileName);
        }

        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            
            for (Long id : ids) {
                FileInfo fileInfo = fileInfoMapper.selectById(id);
                if (fileInfo != null) {
                    try (InputStream inputStream = minioStorageService.downloadFile(fileInfo.getObjectName())) {
                        addFileToZip(zipOut, fileInfo, inputStream);
                        updateDownloadCount(id);
                    } catch (Exception e) {
                        logger.warn("添加文件到压缩包失败: {}", fileInfo.getFileName(), e);
                    }
                }
            }
            
            zipOut.finish();
            logger.info("批量下载完成，共下载 {} 个文件", ids.size());
            
        } catch (IOException e) {
            logger.error("批量下载失败", e);
            throw new RuntimeException("批量下载失败: " + e.getMessage());
        }
    }

    /**
     * 下载文件夹（压缩包）
     */
    @Override
    public void downloadFolder(Long folderId, HttpServletResponse response) {
        // TODO: 实现文件夹下载逻辑
        throw new RuntimeException("文件夹下载功能暂未实现");
    }

    /**
     * 预览文件
     */
    @Override
    public void previewFile(Long id, HttpServletResponse response) {
        FileInfo fileInfo = fileInfoMapper.selectById(id);
        if (fileInfo == null) {
            throw new RuntimeException("文件不存在");
        }

        // 设置预览响应头
        response.setContentType(fileInfo.getFileType());
        try {
            response.setHeader("Content-Disposition", "inline; filename*=UTF-8''" + 
                             URLEncoder.encode(fileInfo.getFileName(), StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            response.setHeader("Content-Disposition", "inline; filename=" + fileInfo.getFileName());
        }

        // 获取文件流并写入响应
        try (InputStream inputStream = getFileInputStream(id);
             OutputStream outputStream = response.getOutputStream()) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            
        } catch (IOException e) {
            throw new RuntimeException("文件预览失败", e);
        }
    }

    /**
     * 获取文件流
     */
    @Override
    public byte[] getFileBytes(Long id) {
        try (InputStream inputStream = getFileInputStream(id)) {
            return org.apache.commons.io.IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("获取文件内容失败", e);
        }
    }

    /**
     * 获取文件输入流
     */
    @Override
    public InputStream getFileInputStream(Long id) {
        FileInfo fileInfo = fileInfoMapper.selectById(id);
        if (fileInfo == null) {
            throw new RuntimeException("文件不存在");
        }
        
        return minioStorageService.downloadFile(fileInfo.getObjectName());
    }

    /**
     * 检查文件是否存在
     */
    @Override
    public boolean fileExists(Long id) {
        return fileInfoMapper.selectById(id) != null;
    }

    /**
     * 检查下载权限
     */
    @Override
    public boolean checkDownloadPermission(Long fileId, Long userId) {
        FileInfo fileInfo = fileInfoMapper.selectById(fileId);
        if (fileInfo == null) {
            return false;
        }
        
        // 检查文件是否公开
        if (fileInfo.getIsPublic() != null && fileInfo.getIsPublic() == 1) {
            return true;
        }
        
        // 检查是否为文件上传者
        if (fileInfo.getUploadUserId().equals(userId)) {
            return true;
        }
        
        // TODO: 检查用户角色权限
        return false;
    }

    /**
     * 更新下载次数
     */
    @Override
    public void updateDownloadCount(Long fileId) {
        try {
            // 这里可以添加下载次数统计逻辑
            // 例如：fileInfoMapper.incrementDownloadCount(fileId);
            logger.debug("文件下载次数更新: {}", fileId);
        } catch (Exception e) {
            logger.warn("更新下载次数失败: {}", fileId, e);
        }
    }

    /**
     * 记录下载日志
     */
    @Override
    public void logDownload(Long fileId, Long userId, String ipAddress, String userAgent) {
        logger.info("下载日志: 文件ID={}, 用户ID={}, IP={}, UserAgent={}", fileId, userId, ipAddress, userAgent);
    }

    /**
     * 获取文件下载链接
     */
    @Override
    public String getDownloadUrl(Long fileId, Long userId, Integer expireMinutes) {
        // TODO: 实现临时下载链接生成
        String token = java.util.UUID.randomUUID().toString();
        return "/api/files/" + fileId + "/download?token=" + token;
    }

    /**
     * 验证下载链接
     */
    @Override
    public boolean validateDownloadUrl(String downloadUrl) {
        // TODO: 实现下载链接验证
        return downloadUrl != null && downloadUrl.contains("/api/files/");
    }

    /**
     * 限速下载
     */
    @Override
    public void downloadWithSpeedLimit(Long id, HttpServletResponse response, Long speedLimit) {
        // TODO: 实现限速下载
        downloadFile(id, response);
    }

    /**
     * 断点续传下载
     */
    @Override
    public void downloadWithRange(Long id, HttpServletResponse response, String range) {
        // TODO: 实现断点续传下载
        downloadFile(id, response);
    }

    /**
     * 设置下载响应头
     */
    private void setDownloadHeaders(HttpServletResponse response, String fileName, Long fileSize) {
        try {
            // 设置文件名编码，支持中文
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
            
            // 设置文件大小
            if (fileSize != null) {
                response.setContentLengthLong(fileSize);
            }
            
            // 设置缓存控制
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            
        } catch (Exception e) {
            logger.warn("设置下载响应头失败", e);
        }
    }

    /**
     * 添加文件到ZIP压缩包
     */
    private void addFileToZip(ZipOutputStream zipOut, FileInfo fileInfo, InputStream inputStream) throws IOException {
        ZipEntry zipEntry = new ZipEntry(fileInfo.getFileName());
        zipOut.putNextEntry(zipEntry);
        
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            zipOut.write(buffer, 0, bytesRead);
        }
        
        zipOut.closeEntry();
    }
}
