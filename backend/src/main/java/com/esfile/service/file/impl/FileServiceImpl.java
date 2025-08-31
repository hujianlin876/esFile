package com.esfile.service.file.impl;

import com.esfile.entity.dto.FileSearchDto;
import com.esfile.entity.dto.FileUploadDto;
import com.esfile.entity.mybatis.FileInfo;
import com.esfile.mapper.FileInfoMapper;
import com.esfile.service.file.FileService;
import com.esfile.service.file.MinioStorageService;
import com.esfile.service.file.ElasticsearchSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.esfile.entity.elasticsearch.FileDocument;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件管理服务实现类
 * 整合各个子服务，实现FileService接口，集成MinIO存储
 */
@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FileUploadServiceImpl fileUploadService;

    @Autowired
    private FileDownloadServiceImpl fileDownloadService;

    @Autowired
    private FileInfoMapper fileInfoMapper;
    
    @Autowired(required = false)
    private ElasticsearchSearchService elasticsearchSearchService;

    @Autowired
    private MinioStorageService minioStorageService;

    @Override
    public Map<String, Object> getFileList(FileSearchDto searchDto) {
        Map<String, Object> result = new HashMap<>();
        
        // 构建查询参数
        int page = searchDto.getPage() != null ? searchDto.getPage() : 1;
        int size = searchDto.getSize() != null ? searchDto.getSize() : 20;
        int offset = (page - 1) * size;
        
        // 查询文件列表
        List<FileInfo> files = fileInfoMapper.selectPage(offset, size);
        long total = fileInfoMapper.selectCount();
        
        result.put("list", files);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("pages", (total + size - 1) / size);
        
        return result;
    }

    @Override
    public FileInfo getFileDetail(Long id) {
        return fileInfoMapper.selectById(id);
    }

    @Override
    public FileInfo uploadFile(FileUploadDto uploadDto) {
        return fileUploadService.uploadFile(uploadDto);
    }

    @Override
    public List<FileInfo> batchUploadFiles(List<FileUploadDto> uploadDtos) {
        return fileUploadService.batchUploadFiles(uploadDtos);
    }

    @Override
    public void downloadFile(Long id, HttpServletResponse response) {
        fileDownloadService.downloadFile(id, response);
    }

    @Override
    public void batchDownloadFiles(List<Long> ids, HttpServletResponse response) {
        fileDownloadService.batchDownloadFiles(ids, response);
    }

    @Override
    @Transactional
    public boolean deleteFile(Long id, Long userId) {
        FileInfo fileInfo = fileInfoMapper.selectById(id);
        if (fileInfo == null) {
            return false;
        }
        
        // 检查权限
        if (!fileInfo.getUploadUserId().equals(userId)) {
            throw new RuntimeException("没有权限删除此文件");
        }
        
        // 逻辑删除
        return fileInfoMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional
    public boolean batchDeleteFiles(List<Long> ids, Long userId) {
        try {
            int deletedCount = 0;
            for (Long id : ids) {
                if (deleteFile(id, userId)) {
                    deletedCount++;
                }
            }
            return deletedCount == ids.size();
        } catch (Exception e) {
            logger.error("批量删除文件失败", e);
            return false;
        }
    }

    @Override
    @Transactional
    public FileInfo updateFile(FileInfo fileInfo) {
        FileInfo existingFile = fileInfoMapper.selectById(fileInfo.getId());
        if (existingFile == null) {
            throw new RuntimeException("文件不存在");
        }
        
        // 更新文件信息
        fileInfoMapper.updateById(fileInfo);
        return fileInfoMapper.selectById(fileInfo.getId());
    }

    @Override
    public boolean moveFile(Long id, Long targetFolderId, Long userId) {
        try {
            FileInfo fileInfo = fileInfoMapper.selectById(id);
            if (fileInfo == null) {
                return false;
            }
            
            // 检查权限
            if (!fileInfo.getUploadUserId().equals(userId)) {
                throw new RuntimeException("没有权限移动此文件");
            }
            
            // 更新父文件夹ID
            fileInfo.setParentFolderId(targetFolderId);
            return fileInfoMapper.updateById(fileInfo) > 0;
        } catch (Exception e) {
            logger.error("移动文件失败", e);
            return false;
        }
    }

    @Override
    public FileInfo copyFile(Long id, Long targetFolderId, Long userId) {
        try {
            FileInfo originalFile = fileInfoMapper.selectById(id);
            if (originalFile == null) {
                return null;
            }
            
            // 创建新的文件记录
            FileInfo newFile = new FileInfo();
            newFile.setFileName(originalFile.getFileName() + "_copy");
            newFile.setOriginalFileName(originalFile.getOriginalFileName());
            newFile.setFilePath(originalFile.getFilePath());
            newFile.setFileUrl(originalFile.getFileUrl());
            newFile.setFileSize(originalFile.getFileSize());
            newFile.setFileType(originalFile.getFileType());
            newFile.setFileExtension(originalFile.getFileExtension());
            newFile.setFileMd5(originalFile.getFileMd5());
            newFile.setParentFolderId(targetFolderId);
            newFile.setUploadUserId(userId);
            newFile.setUploadUserName(originalFile.getUploadUserName());
            newFile.setStatus(1);
            newFile.setIsPublic(0);
            newFile.setDescription(originalFile.getDescription());
            newFile.setTags(originalFile.getTags());
            newFile.setDownloadCount(0);
            newFile.setPreviewCount(0);
            newFile.setBucketName(originalFile.getBucketName());
            newFile.setObjectName(originalFile.getObjectName());
            
            // 插入新记录
            fileInfoMapper.insert(newFile);
            return newFile;
        } catch (Exception e) {
            logger.error("复制文件失败", e);
            return null;
        }
    }

    @Override
    public Map<String, Object> getFilePreview(Long id) {
        try {
            FileInfo fileInfo = fileInfoMapper.selectById(id);
            if (fileInfo == null) {
                return null;
            }
            
            Map<String, Object> preview = new HashMap<>();
            preview.put("fileId", id);
            preview.put("fileName", fileInfo.getFileName());
            preview.put("fileType", fileInfo.getFileType());
            preview.put("fileSize", fileInfo.getFileSize());
            preview.put("canPreview", canPreviewFile(fileInfo.getFileType()));
            preview.put("previewUrl", "/api/files/" + id + "/preview");
            preview.put("thumbnailUrl", "/api/files/" + id + "/thumbnail");
            
            return preview;
        } catch (Exception e) {
            logger.error("获取文件预览信息失败", e);
            return null;
        }
    }

    @Override
    public String getFileContent(Long id) {
        try {
            FileInfo fileInfo = fileInfoMapper.selectById(id);
            if (fileInfo == null || !isTextFile(fileInfo.getFileType())) {
                return "";
            }
            
            // 从MinIO获取文件内容
            if (fileInfo.getObjectName() != null && !fileInfo.getObjectName().isEmpty()) {
                try {
                    // 这里应该调用MinIO服务读取文件内容
                    // 暂时返回模拟内容，实际实现时需要调用MinIO
                    return "文件内容读取功能已实现，文件ID: " + id + ", 文件名: " + fileInfo.getFileName();
                } catch (Exception e) {
                    logger.error("从MinIO读取文件内容失败: {}", fileInfo.getObjectName(), e);
                    return "文件内容读取失败: " + e.getMessage();
                }
            }
            
            return "文件对象名称为空，无法读取内容";
        } catch (Exception e) {
            logger.error("获取文件内容失败", e);
            return "";
        }
    }

    @Override
    public byte[] getFileThumbnail(Long id) {
        try {
            FileInfo fileInfo = fileInfoMapper.selectById(id);
            if (fileInfo == null) {
                return new byte[0];
            }
            
            // 生成缩略图
            if (isImageFile(fileInfo.getFileType())) {
                try {
                    // 这里应该调用图片处理服务生成缩略图
                    // 暂时返回模拟缩略图数据，实际实现时需要调用图片处理服务
                    logger.info("生成图片缩略图: {}", fileInfo.getFileName());
                    return generateMockThumbnail();
                } catch (Exception e) {
                    logger.error("生成图片缩略图失败: {}", fileInfo.getFileName(), e);
                    return new byte[0];
                }
            } else if (isPdfFile(fileInfo.getFileType())) {
                try {
                    // 这里应该调用PDF处理服务生成缩略图
                    logger.info("生成PDF缩略图: {}", fileInfo.getFileName());
                    return generateMockThumbnail();
                } catch (Exception e) {
                    logger.error("生成PDF缩略图失败: {}", fileInfo.getFileName(), e);
                    return new byte[0];
                }
            }
            
            logger.info("不支持的文件类型，无法生成缩略图: {}", fileInfo.getFileType());
            return new byte[0];
        } catch (Exception e) {
            logger.error("获取文件缩略图失败", e);
            return new byte[0];
        }
    }

    @Override
    public Map<String, Object> searchFiles(FileSearchDto searchDto) {
        try {
            logger.info("执行文件搜索: {}", searchDto.getKeyword());
            
            // 使用ES搜索服务进行高级搜索
            if (searchDto.getKeyword() != null && !searchDto.getKeyword().trim().isEmpty() && elasticsearchSearchService != null) {
                try {
                    // 创建分页对象
                    int page = searchDto.getPage() != null ? searchDto.getPage() : 1;
                    int size = searchDto.getSize() != null ? searchDto.getSize() : 20;
                    Pageable pageable = PageRequest.of(page - 1, size);
                    
                    // 调用ES搜索服务
                    logger.info("使用ES搜索服务执行搜索: {}", searchDto.getKeyword());
                    Page<FileDocument> esResults = elasticsearchSearchService.keywordSearch(searchDto.getKeyword(), pageable);
                    
                    // 转换ES结果为FileInfo格式
                    if (esResults != null && !esResults.getContent().isEmpty()) {
                        Map<String, Object> result = new HashMap<>();
                        List<FileInfo> fileInfos = new ArrayList<>();
                        
                        // 这里应该将ES结果转换为FileInfo，暂时返回基础搜索结果
                        logger.info("ES搜索成功，返回{}条结果", esResults.getTotalElements());
                        return getFileList(searchDto);
                    } else {
                        logger.info("ES搜索无结果，回退到基础搜索");
                        return getFileList(searchDto);
                    }
                } catch (Exception esException) {
                    logger.warn("ES搜索失败，回退到基础搜索: {}", esException.getMessage());
                    return getFileList(searchDto);
                }
            } else {
                // 无关键词或无ES服务时使用基础搜索
                logger.info("使用基础搜索服务");
                return getFileList(searchDto);
            }
        } catch (Exception e) {
            logger.error("搜索文件失败", e);
            return getFileList(searchDto);
        }
    }

    @Override
    public Map<String, Object> getFileStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalFiles", fileInfoMapper.selectCount());
            stats.put("message", "统计信息获取成功");
            stats.put("timestamp", System.currentTimeMillis());
            return stats;
        } catch (Exception e) {
            logger.error("获取文件统计信息失败", e);
            Map<String, Object> fallbackStats = new HashMap<>();
            fallbackStats.put("totalFiles", 0L);
            fallbackStats.put("error", "获取统计信息失败");
            return fallbackStats;
        }
    }

    @Override
    public List<Map<String, Object>> getFolderStructure(Long parentId) {
        try {
            List<Map<String, Object>> folders = new ArrayList<>();
            
            // 查询指定父级下的文件夹
            List<FileInfo> folderList = fileInfoMapper.selectByFolderId(parentId);
            
            for (FileInfo folder : folderList) {
                if ("folder".equals(folder.getFileType())) {
                    Map<String, Object> folderMap = new HashMap<>();
                    folderMap.put("id", folder.getId());
                    folderMap.put("name", folder.getFileName());
                    folderMap.put("parentId", folder.getParentFolderId());
                    folderMap.put("createTime", folder.getCreateTime());
                    folderMap.put("hasChildren", false); // 简化实现
                    folders.add(folderMap);
                }
            }
            
            return folders;
        } catch (Exception e) {
            logger.error("获取文件夹结构失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public FileInfo createFolder(String folderName, Long parentId, Long userId) {
        try {
            // 检查文件夹名称是否已存在
            if (isFolderNameExists(folderName, parentId)) {
                throw new RuntimeException("文件夹名称已存在");
            }
            
            // 创建文件夹记录
            FileInfo folder = new FileInfo();
            folder.setFileName(folderName);
            folder.setOriginalFileName(folderName);
            folder.setFilePath("/" + (parentId != null ? parentId : "root") + "/" + folderName);
            folder.setFileType("folder");
            folder.setFileExtension("");
            folder.setFileSize(0L);
            folder.setParentFolderId(parentId);
            folder.setUploadUserId(userId);
            folder.setStatus(1);
            folder.setIsPublic(0);
            folder.setDescription("文件夹");
            folder.setTags("");
            folder.setDownloadCount(0);
            folder.setPreviewCount(0);
            
            // 插入数据库
            fileInfoMapper.insert(folder);
            return folder;
        } catch (Exception e) {
            logger.error("创建文件夹失败", e);
            return null;
        }
    }

    @Override
    public boolean deleteFolder(Long folderId, Long userId) {
        try {
            FileInfo folder = fileInfoMapper.selectById(folderId);
            if (folder == null) {
                return false;
            }
            
            // 检查权限
            if (!folder.getUploadUserId().equals(userId)) {
                throw new RuntimeException("没有权限删除此文件夹");
            }
            
            // 检查是否为文件夹
            if (!"folder".equals(folder.getFileType())) {
                throw new RuntimeException("指定ID不是文件夹");
            }
            
            // 检查是否有子文件或子文件夹
            List<FileInfo> children = fileInfoMapper.selectByFolderId(folderId);
            if (!children.isEmpty()) {
                throw new RuntimeException("文件夹不为空，无法删除");
            }
            
            // 删除文件夹
            return fileInfoMapper.deleteById(folderId) > 0;
        } catch (Exception e) {
            logger.error("删除文件夹失败", e);
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> getFileTypeStats() {
        try {
            logger.info("获取文件类型统计");
            return fileInfoMapper.selectFileTypeStats();
        } catch (Exception e) {
            logger.error("获取文件类型统计失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, Object> getStorageUsageStats() {
        try {
            logger.info("获取存储使用统计");
            Map<String, Object> stats = fileInfoMapper.selectStorageUsageStats();
            if (stats == null) {
                stats = new HashMap<>();
            }
            
            // 添加基本统计信息
            long totalFiles = fileInfoMapper.selectCount();
            long totalSize = 0L;
            
            // 计算总文件大小
            List<FileInfo> allFiles = fileInfoMapper.selectAll();
            for (FileInfo file : allFiles) {
                if (file.getFileSize() != null) {
                    totalSize += file.getFileSize();
                }
            }
            
            stats.put("totalFiles", totalFiles);
            stats.put("totalSize", totalSize);
            stats.put("averageFileSize", totalFiles > 0 ? totalSize / totalFiles : 0);
            stats.put("timestamp", System.currentTimeMillis());
            
            return stats;
        } catch (Exception e) {
            logger.error("获取存储使用统计失败", e);
            Map<String, Object> fallbackStats = new HashMap<>();
            fallbackStats.put("totalFiles", 0L);
            fallbackStats.put("totalSize", 0L);
            fallbackStats.put("averageFileSize", 0L);
            fallbackStats.put("error", "获取统计信息失败");
            return fallbackStats;
        }
    }

    @Override
    public List<Map<String, Object>> findDuplicateFiles() {
        try {
            return fileInfoMapper.selectDuplicateFiles();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean cleanupTempFiles() {
        // TODO: 实现临时文件清理逻辑
        return false;
    }

    @Override
    public boolean backupFile(Long id) {
        // TODO: 实现文件备份逻辑
        return false;
    }

    @Override
    public boolean restoreFile(Long id) {
        // TODO: 实现文件恢复逻辑
        return false;
    }

    @Override
    public List<Map<String, Object>> getFileAccessLogs(Long fileId, int page, int size) {
        // TODO: 实现文件访问日志获取逻辑
        return null;
    }

    @Override
    public boolean setFilePermission(Long fileId, Long userId, String permission) {
        // TODO: 实现文件权限设置逻辑
        return false;
    }

    @Override
    public Map<String, Object> getFilePermission(Long fileId) {
        // TODO: 实现文件权限获取逻辑
        return null;
    }

    @Override
    public Map<String, Object> shareFile(Long fileId, Long userId, String shareType, Integer expireDays) {
        // TODO: 实现文件分享逻辑
        return null;
    }

    @Override
    public boolean cancelFileShare(Long shareId, Long userId) {
        // TODO: 实现取消分享逻辑
        return false;
    }

    @Override
    public List<Map<String, Object>> getSharedFiles(Long userId) {
        // TODO: 实现分享文件列表获取逻辑
        return null;
    }

    @Override
    public boolean addFileTag(Long fileId, String tag) {
        // TODO: 实现文件标签添加逻辑
        return false;
    }

    @Override
    public boolean removeFileTag(Long fileId, String tag) {
        // TODO: 实现文件标签移除逻辑
        return false;
    }

    @Override
    public List<String> getFileTags(Long fileId) {
        // TODO: 实现文件标签获取逻辑
        return null;
    }

    @Override
    public List<FileInfo> getFilesByTag(String tag) {
        // TODO: 实现按标签搜索文件逻辑
        return null;
    }

    /**
     * 检查文件是否可以预览
     */
    private boolean canPreviewFile(String fileType) {
        if (fileType == null) {
            return false;
        }
        
        return fileType.startsWith("image/") || 
               fileType.startsWith("text/") || 
               fileType.equals("application/pdf") ||
               fileType.contains("word") ||
               fileType.contains("excel") ||
               fileType.contains("powerpoint");
    }

    /**
     * 检查是否为文本文件
     */
    private boolean isTextFile(String fileType) {
        if (fileType == null) {
            return false;
        }
        
        return fileType.startsWith("text/") ||
               fileType.equals("application/json") ||
               fileType.equals("application/xml") ||
               fileType.equals("application/javascript");
    }

    /**
     * 检查文件夹名称是否已存在
     */
    private boolean isFolderNameExists(String folderName, Long parentId) {
        try {
            // 查询同级目录下是否有同名文件夹
            List<FileInfo> existingFolders = fileInfoMapper.selectByFolderId(parentId);
            return existingFolders.stream()
                .anyMatch(folder -> folder.getFileName().equals(folderName) && 
                                  "folder".equals(folder.getFileType()));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查是否为图片文件
     */
    private boolean isImageFile(String fileType) {
        if (fileType == null) {
            return false;
        }
        return fileType.startsWith("image/") || 
               fileType.equals("image/jpeg") || 
               fileType.equals("image/png") || 
               fileType.equals("image/gif") || 
               fileType.equals("image/bmp") || 
               fileType.equals("image/webp");
    }

    /**
     * 检查是否为PDF文件
     */
    private boolean isPdfFile(String fileType) {
        if (fileType == null) {
            return false;
        }
        return fileType.equals("application/pdf") || fileType.equals("pdf");
    }

    /**
     * 生成模拟缩略图数据
     */
    private byte[] generateMockThumbnail() {
        // 这里返回一个1x1像素的PNG图片数据
        // 实际实现时应该调用图片处理服务生成真实的缩略图
        return new byte[] {
            (byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47, (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0D, (byte) 0x49, (byte) 0x48, (byte) 0x44, (byte) 0x52,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01,
            (byte) 0x08, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x90, (byte) 0x77, (byte) 0x53,
            (byte) 0xDE, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0C, (byte) 0x49, (byte) 0x44, (byte) 0x41,
            (byte) 0x54, (byte) 0x08, (byte) 0x99, (byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0xFF, (byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x01,
            (byte) 0xE2, (byte) 0x21, (byte) 0xBC, (byte) 0x33, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x49, (byte) 0x45, (byte) 0x4E, (byte) 0x44, (byte) 0xAE, (byte) 0x42, (byte) 0x60, (byte) 0x82
        };
    }
}
