package com.esfile.service.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * MinIO存储服务接口
 * 定义文件存储相关的操作
 * 
 * @author esfile
 * @since 1.0.0
 */
public interface MinioStorageService {

    /**
     * 上传文件
     * 
     * @param file 文件对象
     * @param objectName 对象名称（文件路径）
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file, String objectName);

    /**
     * 上传字节数组
     * 
     * @param fileData 文件字节数组
     * @param objectName 对象名称
     * @param contentType 内容类型
     * @return 文件访问URL
     */
    String uploadBytes(byte[] fileData, String objectName, String contentType);

    /**
     * 下载文件
     * 
     * @param objectName 对象名称
     * @return 文件输入流
     */
    InputStream downloadFile(String objectName);

    /**
     * 删除文件
     * 
     * @param objectName 对象名称
     * @return 是否删除成功
     */
    boolean deleteFile(String objectName);

    /**
     * 批量删除文件
     * 
     * @param objectNames 对象名称列表
     * @return 删除成功的文件数量
     */
    int batchDeleteFiles(List<String> objectNames);

    /**
     * 获取文件访问URL
     * 
     * @param objectName 对象名称
     * @return 文件访问URL
     */
    String getFileUrl(String objectName);

    /**
     * 获取预签名上传URL
     * 
     * @param objectName 对象名称
     * @param expirationMinutes 过期时间（分钟）
     * @return 预签名URL
     */
    String getPresignedUploadUrl(String objectName, int expirationMinutes);

    /**
     * 获取预签名下载URL
     * 
     * @param objectName 对象名称
     * @param expirationMinutes 过期时间（分钟）
     * @return 预签名URL
     */
    String getPresignedDownloadUrl(String objectName, int expirationMinutes);

    /**
     * 检查文件是否存在
     * 
     * @param objectName 对象名称
     * @return 文件是否存在
     */
    boolean fileExists(String objectName);

    /**
     * 获取文件信息
     * 
     * @param objectName 对象名称
     * @return 文件信息Map
     */
    java.util.Map<String, Object> getFileInfo(String objectName);

    /**
     * 复制文件
     * 
     * @param sourceObjectName 源对象名称
     * @param targetObjectName 目标对象名称
     * @return 是否复制成功
     */
    boolean copyFile(String sourceObjectName, String targetObjectName);

    /**
     * 移动文件
     * 
     * @param sourceObjectName 源对象名称
     * @param targetObjectName 目标对象名称
     * @return 是否移动成功
     */
    boolean moveFile(String sourceObjectName, String targetObjectName);

    /**
     * 获取文件大小
     * 
     * @param objectName 对象名称
     * @return 文件大小（字节）
     */
    long getFileSize(String objectName);

    /**
     * 获取文件类型
     * 
     * @param objectName 对象名称
     * @return 文件类型
     */
    String getFileType(String objectName);
}
