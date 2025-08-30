package com.esfile.entity.mybatis;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件信息实体类
 * 
 * @author esfile
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileInfo extends BaseEntity {
    
    /**
     * 文件名
     */
    private String fileName;
    
    /**
     * 原始文件名
     */
    private String originalFileName;
    
    /**
     * 文件路径
     */
    private String filePath;
    
    /**
     * 文件URL
     */
    private String fileUrl;
    
    /**
     * 文件大小（字节）
     */
    private Long fileSize;
    
    /**
     * 文件类型
     */
    private String fileType;
    
    /**
     * 文件扩展名
     */
    private String fileExtension;
    
    /**
     * 文件MD5值
     */
    private String fileMd5;
    
    /**
     * 上传用户ID
     */
    private Long uploadUserId;
    
    /**
     * 上传用户名
     */
    private String uploadUserName;
    
    /**
     * 文件状态：0-删除，1-正常
     */
    private Integer status;
    
    /**
     * 是否公开：0-私有，1-公开
     */
    private Integer isPublic;
    
    /**
     * 文件描述
     */
    private String description;
    
    /**
     * 标签
     */
    private String tags;
    
    /**
     * 下载次数
     */
    private Integer downloadCount;
    
    /**
     * 预览次数
     */
    private Integer previewCount;
    
    /**
     * MinIO存储桶名称
     */
    private String bucketName;
    
    /**
     * MinIO对象名称
     */
    private String objectName;
}
