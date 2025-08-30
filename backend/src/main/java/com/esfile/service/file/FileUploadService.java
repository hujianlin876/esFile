package com.esfile.service.file;

import com.esfile.entity.dto.FileUploadDto;
import com.esfile.entity.mybatis.FileInfo;

import java.util.List;
import java.util.Map;

/**
 * 文件上传服务接口
 */
public interface FileUploadService {

    /**
     * 上传单个文件
     */
    FileInfo uploadFile(FileUploadDto uploadDto);

    /**
     * 批量上传文件
     */
    List<FileInfo> batchUploadFiles(List<FileUploadDto> uploadDtos);

    /**
     * 分片上传文件
     */
    FileInfo uploadFileChunk(String chunkId, Integer chunkNumber, Integer totalChunks, 
                           byte[] chunkData, String fileName, Long fileSize);

    /**
     * 合并分片文件
     */
    FileInfo mergeFileChunks(String chunkId, String fileName, Long fileSize, 
                           Integer totalChunks, Long userId);

    /**
     * 断点续传
     */
    FileInfo resumeUpload(String uploadId, Integer chunkNumber, byte[] chunkData);

    /**
     * 取消上传
     */
    boolean cancelUpload(String uploadId);

    /**
     * 获取上传进度
     */
    Map<String, Object> getUploadProgress(String uploadId);

    /**
     * 验证文件类型
     */
    boolean validateFileType(String fileName, String contentType);

    /**
     * 验证文件大小
     */
    boolean validateFileSize(Long fileSize);

    /**
     * 生成文件MD5
     */
    String generateFileMd5(byte[] fileData);

    /**
     * 检查文件是否重复
     */
    FileInfo checkDuplicateFile(String fileMd5, Long userId);

    /**
     * 保存文件到存储
     */
    String saveFileToStorage(byte[] fileData, String fileName, String bucketName);

    /**
     * 清理上传缓存
     */
    boolean cleanupUploadCache(String uploadId);
}
