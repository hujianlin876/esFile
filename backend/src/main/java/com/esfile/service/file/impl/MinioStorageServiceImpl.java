package com.esfile.service.file.impl;

import com.esfile.service.file.MinioStorageService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * MinIO存储服务实现类
 * 提供完整的文件存储操作功能
 * 
 * @author esfile
 * @since 1.0.0
 */
@Service
public class MinioStorageServiceImpl implements MinioStorageService {

    private static final Logger logger = LoggerFactory.getLogger(MinioStorageServiceImpl.class);

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Override
    public String uploadFile(MultipartFile file, String objectName) {
        try {
            // 检查桶是否存在，不存在则创建
            ensureBucketExists();
            
            // 上传文件
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
            
            logger.info("文件上传成功: {}", objectName);
            return getFileUrl(objectName);
            
        } catch (Exception e) {
            logger.error("文件上传失败: {}", objectName, e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public String uploadBytes(byte[] fileData, String objectName, String contentType) {
        try {
            // 检查桶是否存在，不存在则创建
            ensureBucketExists();
            
            // 上传字节数组
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(new ByteArrayInputStream(fileData), fileData.length, -1)
                    .contentType(contentType)
                    .build()
            );
            
            logger.info("字节数组上传成功: {}", objectName);
            return getFileUrl(objectName);
            
        } catch (Exception e) {
            logger.error("字节数组上传失败: {}", objectName, e);
            throw new RuntimeException("字节数组上传失败: " + e.getMessage());
        }
    }

    @Override
    public InputStream downloadFile(String objectName) {
        try {
            // 检查文件是否存在
            if (!fileExists(objectName)) {
                throw new RuntimeException("文件不存在: " + objectName);
            }
            
            // 获取文件流
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
            
        } catch (Exception e) {
            logger.error("文件下载失败: {}", objectName, e);
            throw new RuntimeException("文件下载失败: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteFile(String objectName) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
            
            logger.info("文件删除成功: {}", objectName);
            return true;
            
        } catch (Exception e) {
            logger.error("文件删除失败: {}", objectName, e);
            return false;
        }
    }

    @Override
    public int batchDeleteFiles(List<String> objectNames) {
        if (objectNames == null || objectNames.isEmpty()) {
            return 0;
        }
        
        int successCount = 0;
        for (String objectName : objectNames) {
            if (deleteFile(objectName)) {
                successCount++;
            }
        }
        
        logger.info("批量删除文件完成，成功删除 {} 个文件", successCount);
        return successCount;
    }

    @Override
    public String getFileUrl(String objectName) {
        return endpoint + "/" + bucketName + "/" + objectName;
    }

    @Override
    public String getPresignedUploadUrl(String objectName, int expirationMinutes) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.PUT)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry((int) TimeUnit.MINUTES.toSeconds(expirationMinutes))
                    .build()
            );
        } catch (Exception e) {
            logger.error("获取预签名上传URL失败: {}", objectName, e);
            throw new RuntimeException("获取预签名上传URL失败: " + e.getMessage());
        }
    }

    @Override
    public String getPresignedDownloadUrl(String objectName, int expirationMinutes) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry((int) TimeUnit.MINUTES.toSeconds(expirationMinutes))
                    .build()
            );
        } catch (Exception e) {
            logger.error("获取预签名下载URL失败: {}", objectName, e);
            throw new RuntimeException("获取预签名下载URL失败: " + e.getMessage());
        }
    }

    @Override
    public boolean fileExists(String objectName) {
        try {
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
            return true;
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                return false;
            }
            throw new RuntimeException("检查文件存在性失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("检查文件存在性失败: {}", objectName, e);
            return false;
        }
    }

    @Override
    public Map<String, Object> getFileInfo(String objectName) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
            
            Map<String, Object> fileInfo = new HashMap<>();
            fileInfo.put("size", stat.size());
            fileInfo.put("contentType", stat.contentType());
            fileInfo.put("lastModified", stat.lastModified());
            fileInfo.put("etag", stat.etag());
            fileInfo.put("versionId", stat.versionId());
            
            return fileInfo;
            
        } catch (Exception e) {
            logger.error("获取文件信息失败: {}", objectName, e);
            throw new RuntimeException("获取文件信息失败: " + e.getMessage());
        }
    }

    @Override
    public boolean copyFile(String sourceObjectName, String targetObjectName) {
        try {
            minioClient.copyObject(
                CopyObjectArgs.builder()
                    .bucket(bucketName)
                    .object(targetObjectName)
                    .source(CopySource.builder()
                        .bucket(bucketName)
                        .object(sourceObjectName)
                        .build())
                    .build()
            );
            
            logger.info("文件复制成功: {} -> {}", sourceObjectName, targetObjectName);
            return true;
            
        } catch (Exception e) {
            logger.error("文件复制失败: {} -> {}", sourceObjectName, targetObjectName, e);
            return false;
        }
    }

    @Override
    public boolean moveFile(String sourceObjectName, String targetObjectName) {
        try {
            // 先复制文件
            if (copyFile(sourceObjectName, targetObjectName)) {
                // 复制成功后删除源文件
                return deleteFile(sourceObjectName);
            }
            return false;
        } catch (Exception e) {
            logger.error("文件移动失败: {} -> {}", sourceObjectName, targetObjectName, e);
            return false;
        }
    }

    @Override
    public long getFileSize(String objectName) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
            return stat.size();
        } catch (Exception e) {
            logger.error("获取文件大小失败: {}", objectName, e);
            return -1;
        }
    }

    @Override
    public String getFileType(String objectName) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
            return stat.contentType();
        } catch (Exception e) {
            logger.error("获取文件类型失败: {}", objectName, e);
            return null;
        }
    }

    /**
     * 确保桶存在，不存在则创建
     */
    private void ensureBucketExists() {
        try {
            boolean bucketExists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build()
            );
            
            if (!bucketExists) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build()
                );
                logger.info("创建桶成功: {}", bucketName);
            }
        } catch (Exception e) {
            logger.error("检查或创建桶失败: {}", bucketName, e);
            throw new RuntimeException("检查或创建桶失败: " + e.getMessage());
        }
    }
}
