package com.esfile.service.file.impl;

import com.esfile.entity.dto.FileUploadDto;
import com.esfile.entity.mybatis.FileInfo;
import com.esfile.mapper.FileInfoMapper;
import com.esfile.service.file.FileUploadService;
import com.esfile.service.file.MinioStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件上传服务实现类
 * 专门处理文件上传相关功能，集成MinIO存储
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Autowired
    private MinioStorageService minioStorageService;

    // 上传进度缓存
    private final Map<String, Map<String, Object>> uploadProgressCache = new ConcurrentHashMap<>();
    
    // 分片文件缓存
    private final Map<String, List<byte[]>> chunkCache = new ConcurrentHashMap<>();

    /**
     * 上传单个文件
     */
    @Override
    @Transactional
    public FileInfo uploadFile(FileUploadDto uploadDto) {
        MultipartFile file = uploadDto.getFile();
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        // 验证文件
        if (!validateFileType(file.getOriginalFilename(), file.getContentType())) {
            throw new RuntimeException("不支持的文件类型");
        }
        if (!validateFileSize(file.getSize())) {
            throw new RuntimeException("文件大小超出限制");
        }

        // 生成文件信息
        FileInfo fileInfo = createFileInfo(file, uploadDto);
        
        // 检查重复文件
        FileInfo duplicateFile = checkDuplicateFile(fileInfo.getFileMd5(), uploadDto.getUploadUserId());
        if (duplicateFile != null) {
            return duplicateFile;
        }
        
        // 生成MinIO对象名称
        String objectName = generateObjectName(fileInfo.getFileName(), uploadDto.getUploadUserId());
        fileInfo.setObjectName(objectName);
        
        try {
            // 保存文件到MinIO存储
            String fileUrl = minioStorageService.uploadFile(file, objectName);
            fileInfo.setFileUrl(fileUrl);
            
            // 保存文件信息到数据库
            fileInfoMapper.insert(fileInfo);
            
            logger.info("文件上传成功: {}, 大小: {} bytes, 存储路径: {}", 
                fileInfo.getFileName(), fileInfo.getFileSize(), objectName);
            
            return fileInfo;
            
        } catch (Exception e) {
            logger.error("文件上传失败: {}", fileInfo.getFileName(), e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 批量上传文件
     */
    @Override
    @Transactional
    public List<FileInfo> batchUploadFiles(List<FileUploadDto> uploadDtos) {
        List<FileInfo> uploadedFiles = new ArrayList<>();
        
        for (FileUploadDto uploadDto : uploadDtos) {
            try {
                FileInfo fileInfo = uploadFile(uploadDto);
                uploadedFiles.add(fileInfo);
            } catch (Exception e) {
                // 记录错误但继续处理其他文件
                logger.error("批量上传文件失败: {}", uploadDto.getFile().getOriginalFilename(), e);
            }
        }
        
        logger.info("批量上传完成，成功上传 {} 个文件", uploadedFiles.size());
        return uploadedFiles;
    }

    /**
     * 分片上传文件
     */
    @Override
    public FileInfo uploadFileChunk(String chunkId, Integer chunkNumber, Integer totalChunks, 
                                  byte[] chunkData, String fileName, Long fileSize) {
        // 缓存分片数据
        chunkCache.computeIfAbsent(chunkId, k -> new ArrayList<>());
        List<byte[]> chunks = chunkCache.get(chunkId);
        
        // 确保chunks列表大小足够
        while (chunks.size() <= chunkNumber) {
            chunks.add(null);
        }
        chunks.set(chunkNumber, chunkData);
        
        // 更新上传进度
        updateUploadProgress(chunkId, chunkNumber, totalChunks, fileName, fileSize);
        
        // 检查是否所有分片都已上传
        if (isAllChunksUploaded(chunkId, totalChunks)) {
            return mergeChunksAndUpload(chunkId, fileName, fileSize);
        }
        
        return null;
    }

    /**
     * 合并分片文件
     */
    @Override
    @Transactional
    public FileInfo mergeFileChunks(String chunkId, String fileName, Long fileSize, 
                                  Integer totalChunks, Long userId) {
        List<byte[]> chunks = chunkCache.get(chunkId);
        if (chunks == null || chunks.size() != totalChunks) {
            throw new RuntimeException("分片文件不完整");
        }
        
        // 合并分片数据
        byte[] completeFile = new byte[fileSize.intValue()];
        int offset = 0;
        for (byte[] chunk : chunks) {
            if (chunk != null) {
                System.arraycopy(chunk, 0, completeFile, offset, chunk.length);
                offset += chunk.length;
            }
        }
        
        // 创建文件信息
        FileUploadDto uploadDto = new FileUploadDto();
        uploadDto.setUploadUserId(userId);
        uploadDto.setUploadUserName("用户" + userId);
        
        // 保存完整文件
        String objectName = saveFileToStorage(completeFile, fileName, "elasticsearch");
        
        // 创建文件信息
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(fileName);
        fileInfo.setOriginalFileName(fileName);
        fileInfo.setFileSize(fileSize);
        fileInfo.setFileType(getContentType(fileName));
        fileInfo.setFileExtension(getFileExtension(fileName));
        fileInfo.setFileMd5(generateFileMd5(completeFile));
        fileInfo.setUploadUserId(userId);
        fileInfo.setUploadUserName("用户" + userId);
        fileInfo.setStatus(1);
        fileInfo.setDownloadCount(0);
        fileInfo.setPreviewCount(0);
        fileInfo.setBucketName("elasticsearch");
        fileInfo.setObjectName(objectName);
        
        // 保存到数据库
        fileInfoMapper.insert(fileInfo);
        
        // 清理缓存
        cleanupUploadCache(chunkId);
        
        return fileInfo;
    }

    /**
     * 断点续传
     */
    @Override
    public FileInfo resumeUpload(String uploadId, Integer chunkNumber, byte[] chunkData) {
        // 获取上传进度
        Map<String, Object> progress = getUploadProgress(uploadId);
        if (progress == null) {
            throw new RuntimeException("上传会话不存在");
        }
        
        // 继续分片上传
        return uploadFileChunk(uploadId, chunkNumber, 
                             (Integer) progress.get("totalChunks"), 
                             chunkData, 
                             (String) progress.get("fileName"), 
                             (Long) progress.get("fileSize"));
    }

    /**
     * 取消上传
     */
    @Override
    public boolean cancelUpload(String uploadId) {
        return cleanupUploadCache(uploadId);
    }

    /**
     * 获取上传进度
     */
    @Override
    public Map<String, Object> getUploadProgress(String uploadId) {
        return uploadProgressCache.get(uploadId);
    }

    /**
     * 验证文件类型
     */
    @Override
    public boolean validateFileType(String fileName, String contentType) {
        if (fileName == null || contentType == null) {
            return false;
        }
        
        // 检查文件扩展名
        String extension = getFileExtension(fileName).toLowerCase();
        Set<String> allowedExtensions = new HashSet<>(Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", "pdf", "doc", "docx", "xls", "xlsx", 
            "ppt", "pptx", "txt", "zip", "rar", "7z", "mp4", "avi", "mov", "mp3", "wav"
        ));
        
        return allowedExtensions.contains(extension);
    }

    /**
     * 验证文件大小
     */
    @Override
    public boolean validateFileSize(Long fileSize) {
        // 默认最大文件大小：100MB
        long maxFileSize = 100 * 1024 * 1024;
        return fileSize != null && fileSize > 0 && fileSize <= maxFileSize;
    }

    /**
     * 生成文件MD5
     */
    @Override
    public String generateFileMd5(byte[] fileData) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(fileData);
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5算法不可用", e);
        }
    }

    /**
     * 检查文件是否重复
     */
    @Override
    public FileInfo checkDuplicateFile(String fileMd5, Long userId) {
        return fileInfoMapper.selectByFileMd5(fileMd5);
    }

    /**
     * 保存文件到存储
     */
    @Override
    public String saveFileToStorage(byte[] fileData, String fileName, String bucketName) {
        // 生成对象名称
        String objectName = generateObjectName(fileName, null);
        
        // 使用MinIO存储服务上传
        return minioStorageService.uploadBytes(fileData, objectName, "application/octet-stream");
    }

    /**
     * 清理上传缓存
     */
    @Override
    public boolean cleanupUploadCache(String uploadId) {
        chunkCache.remove(uploadId);
        uploadProgressCache.remove(uploadId);
        return true;
    }

    /**
     * 创建文件信息
     */
    private FileInfo createFileInfo(MultipartFile file, FileUploadDto uploadDto) {
        FileInfo fileInfo = new FileInfo();
        
        // 基本信息
        fileInfo.setFileName(generateFileName(file.getOriginalFilename()));
        fileInfo.setOriginalFileName(file.getOriginalFilename());
        fileInfo.setFileSize(file.getSize());
        fileInfo.setFileType(file.getContentType());
        fileInfo.setFileExtension(getFileExtension(file.getOriginalFilename()));
        
        try {
            fileInfo.setFileMd5(generateFileMd5(file.getBytes()));
        } catch (IOException e) {
            logger.warn("无法读取文件内容生成MD5: {}", file.getOriginalFilename(), e);
            fileInfo.setFileMd5(UUID.randomUUID().toString());
        }
        
        // 用户信息
        fileInfo.setUploadUserId(uploadDto.getUploadUserId());
        fileInfo.setUploadUserName(uploadDto.getUploadUserName());
        
        // 其他信息
        fileInfo.setDescription(uploadDto.getDescription());
        fileInfo.setTags(uploadDto.getTags());
        fileInfo.setIsPublic(uploadDto.getIsPublic());
        fileInfo.setStatus(1); // 1: 正常
        
        return fileInfo;
    }

    /**
     * 更新上传进度
     */
    private void updateUploadProgress(String chunkId, Integer chunkNumber, Integer totalChunks, 
                                    String fileName, Long fileSize) {
        Map<String, Object> progress = new HashMap<>();
        progress.put("chunkNumber", chunkNumber);
        progress.put("totalChunks", totalChunks);
        progress.put("fileName", fileName);
        progress.put("fileSize", fileSize);
        progress.put("progress", (chunkNumber + 1) * 100.0 / totalChunks);
        progress.put("lastUpdate", System.currentTimeMillis());
        
        uploadProgressCache.put(chunkId, progress);
    }

    /**
     * 生成文件名
     */
    private String generateFileName(String originalFileName) {
        if (originalFileName == null) {
            return UUID.randomUUID().toString();
        }
        
        String extension = getFileExtension(originalFileName);
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        
        // 添加时间戳避免重名
        return baseName + "_" + System.currentTimeMillis() + "." + extension;
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 获取内容类型
     */
    private String getContentType(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        Map<String, String> contentTypeMap = new HashMap<>();
        contentTypeMap.put("jpg", "image/jpeg");
        contentTypeMap.put("jpeg", "image/jpeg");
        contentTypeMap.put("png", "image/png");
        contentTypeMap.put("gif", "image/gif");
        contentTypeMap.put("pdf", "application/pdf");
        contentTypeMap.put("doc", "application/msword");
        contentTypeMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        contentTypeMap.put("txt", "text/plain");
        contentTypeMap.put("zip", "application/zip");
        
        return contentTypeMap.getOrDefault(extension, "application/octet-stream");
    }

    /**
     * 生成MinIO对象名称
     */
    private String generateObjectName(String fileName, Long userId) {
        String userIdStr = userId != null ? userId.toString() : "anonymous";
        String dateStr = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return String.format("files/%s/%s/%s", userIdStr, dateStr, fileName);
    }

    /**
     * 检查是否所有分片都已上传
     */
    private boolean isAllChunksUploaded(String chunkId, Integer totalChunks) {
        List<byte[]> chunks = chunkCache.get(chunkId);
        if (chunks == null || chunks.size() < totalChunks) {
            return false;
        }
        
        for (int i = 0; i < totalChunks; i++) {
            if (chunks.get(i) == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 合并分片并上传
     */
    private FileInfo mergeChunksAndUpload(String chunkId, String fileName, Long fileSize) {
        List<byte[]> chunks = chunkCache.get(chunkId);
        
        // 计算总大小
        int totalSize = chunks.stream()
            .filter(chunk -> chunk != null)
            .mapToInt(chunk -> chunk.length)
            .sum();
        
        // 合并分片
        byte[] mergedData = new byte[totalSize];
        int offset = 0;
        for (byte[] chunk : chunks) {
            if (chunk != null) {
                System.arraycopy(chunk, 0, mergedData, offset, chunk.length);
                offset += chunk.length;
            }
        }
        
        // 生成对象名称
        String objectName = generateObjectName(fileName, null);
        
        try {
            // 上传到MinIO
            String fileUrl = minioStorageService.uploadBytes(mergedData, objectName, "application/octet-stream");
            
            // 创建文件信息
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(fileName);
            fileInfo.setFileSize((long) totalSize);
            fileInfo.setObjectName(objectName);
            fileInfo.setFileUrl(fileUrl);
            fileInfo.setStatus(1);
            
            // 保存到数据库
            fileInfoMapper.insert(fileInfo);
            
            // 清理缓存
            cleanupUploadCache(chunkId);
            
            logger.info("分片文件合并上传成功: {}, 大小: {} bytes", fileName, totalSize);
            return fileInfo;
            
        } catch (Exception e) {
            logger.error("分片文件合并上传失败: {}", fileName, e);
            throw new RuntimeException("分片文件合并上传失败: " + e.getMessage());
        }
    }
}
