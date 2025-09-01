package com.esfile.service.file.impl;

import com.esfile.entity.dto.FileSearchDto;
import com.esfile.entity.dto.FileUploadDto;
import com.esfile.entity.mybatis.FileInfo;
import com.esfile.mapper.FileInfoMapper;
import com.esfile.service.file.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件管理服务实现类 - 重构版
 * 作为协调器，委托给各个专门的服务处理具体业务
 * 符合单一职责原则，文件大小控制在400行以内
 * 
 * @author esfile
 * @since 1.0.0
 */
@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private FileDownloadService fileDownloadService;

    @Autowired
    private FileOperationService fileOperationService;

    @Autowired
    private FileAnalyticsService fileAnalyticsService;

    @Autowired
    private FilePermissionService filePermissionService;

    @Autowired
    private FilePreviewService filePreviewService;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    // =============== 文件查询相关 ===============
    
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

    // =============== 文件上传下载 ===============
    
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

    // =============== 文件操作相关 ===============
    
    @Override
    public boolean deleteFile(Long id, Long userId) {
        return fileOperationService.deleteFile(id, userId);
    }

    @Override
    public boolean batchDeleteFiles(List<Long> ids, Long userId) {
        return fileOperationService.batchDeleteFiles(ids, userId);
    }

    @Override
    public FileInfo updateFile(FileInfo fileInfo) {
        return fileOperationService.updateFile(fileInfo);
    }

    @Override
    public boolean moveFile(Long id, Long targetFolderId, Long userId) {
        return fileOperationService.moveFile(id, targetFolderId, userId);
    }

    @Override
    public FileInfo copyFile(Long id, Long targetFolderId, Long userId) {
        return fileOperationService.copyFile(id, targetFolderId, userId);
    }

    @Override
    public FileInfo createFolder(String folderName, Long parentId, Long userId) {
        return fileOperationService.createFolder(folderName, parentId, userId);
    }

    @Override
    public boolean deleteFolder(Long folderId, Long userId) {
        return fileOperationService.deleteFolder(folderId, userId);
    }

    @Override
    public List<Map<String, Object>> getFolderStructure(Long parentId) {
        return fileOperationService.getFolderStructure(parentId);
    }

    @Override
    public boolean cleanupTempFiles() {
        return fileOperationService.cleanupTempFiles();
    }

    @Override
    public boolean backupFile(Long id) {
        return fileOperationService.backupFile(id);
    }

    @Override
    public boolean restoreFile(Long id) {
        return fileOperationService.restoreFile(id);
    }

    // =============== 文件预览相关 ===============
    
    @Override
    public Map<String, Object> getFilePreview(Long id) {
        return filePreviewService.getFilePreviewInfo(id);
    }

    @Override
    public String getFileContent(Long id) {
        return filePreviewService.getFileContent(id, "UTF-8");
    }

    @Override
    public byte[] getFileThumbnail(Long id) {
        return filePreviewService.getThumbnail(id, 150, 150);
    }

    // =============== 文件搜索相关 ===============
    
    @Override
    public Map<String, Object> searchFiles(FileSearchDto searchDto) {
        // 这里可以集成ES搜索或数据库搜索
        return getFileList(searchDto); // 简化实现
    }

    // =============== 文件统计分析 ===============
    
    @Override
    public Map<String, Object> getFileStats() {
        return fileAnalyticsService.getFileStats();
    }

    @Override
    public List<Map<String, Object>> getFileTypeStats() {
        return fileAnalyticsService.getFileTypeStats();
    }

    @Override
    public Map<String, Object> getStorageUsageStats() {
        return fileAnalyticsService.getStorageUsageStats();
    }

    @Override
    public List<Map<String, Object>> findDuplicateFiles() {
        return fileAnalyticsService.findDuplicateFiles();
    }

    @Override
    public List<Map<String, Object>> getFileAccessLogs(Long fileId, int page, int size) {
        return fileAnalyticsService.getFileAccessLogs(fileId, page, size);
    }

    // =============== 文件权限和分享 ===============
    
    @Override
    public boolean setFilePermission(Long fileId, Long userId, String permission) {
        return filePermissionService.setFilePermission(fileId, userId, permission);
    }

    @Override
    public Map<String, Object> getFilePermission(Long fileId) {
        return filePermissionService.getFilePermission(fileId);
    }

    @Override
    public Map<String, Object> shareFile(Long fileId, Long userId, String shareType, Integer expireDays) {
        return filePermissionService.shareFile(fileId, userId, shareType, expireDays);
    }

    @Override
    public boolean cancelFileShare(Long shareId, Long userId) {
        return filePermissionService.cancelFileShare(shareId, userId);
    }

    @Override
    public List<Map<String, Object>> getSharedFiles(Long userId) {
        return filePermissionService.getSharedFiles(userId);
    }

    @Override
    public boolean addFileTag(Long fileId, String tag) {
        return filePermissionService.addFileTag(fileId, tag);
    }

    @Override
    public boolean removeFileTag(Long fileId, String tag) {
        return filePermissionService.removeFileTag(fileId, tag);
    }

    @Override
    public List<String> getFileTags(Long fileId) {
        return filePermissionService.getFileTags(fileId);
    }

    @Override
    public List<FileInfo> getFilesByTag(String tag) {
        return filePermissionService.getFilesByTag(tag);
    }
}