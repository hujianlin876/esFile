package com.esfile.service.file.impl;

import com.esfile.entity.mybatis.FileInfo;
import com.esfile.mapper.FileInfoMapper;
import com.esfile.service.file.FileOperationService;
import com.esfile.service.file.MinioStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件操作服务实现类
 * 负责文件的基础操作：移动、复制、删除等
 * 
 * @author esfile
 * @since 1.0.0
 */
@Service
public class FileOperationServiceImpl implements FileOperationService {

    private static final Logger logger = LoggerFactory.getLogger(FileOperationServiceImpl.class);

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Autowired
    private MinioStorageService minioStorageService;

    @Override
    @Transactional
    public boolean deleteFile(Long id, Long userId) {
        try {
            FileInfo fileInfo = fileInfoMapper.selectById(id);
            if (fileInfo == null) {
                return false;
            }
            
            // 检查权限
            if (!fileInfo.getUploadUserId().equals(userId)) {
                throw new RuntimeException("没有权限删除此文件");
            }
            
            // 从MinIO删除文件
            minioStorageService.deleteFile(fileInfo.getObjectName());
            
            // 从数据库删除记录
            return fileInfoMapper.deleteById(id) > 0;
        } catch (Exception e) {
            logger.error("删除文件失败", e);
            return false;
        }
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
    @Transactional
    public FileInfo copyFile(Long id, Long targetFolderId, Long userId) {
        try {
            FileInfo originalFile = fileInfoMapper.selectById(id);
            if (originalFile == null) {
                return null;
            }

            // 检查权限
            if (!originalFile.getUploadUserId().equals(userId)) {
                throw new RuntimeException("没有权限复制此文件");
            }

            // 创建副本
            FileInfo copyFile = new FileInfo();
            copyFile.setFileName(originalFile.getFileName() + "_copy");
            copyFile.setFileType(originalFile.getFileType());
            copyFile.setFileSize(originalFile.getFileSize());
            copyFile.setFileMd5(originalFile.getFileMd5());
            copyFile.setObjectName(originalFile.getObjectName());
            copyFile.setUploadUserId(userId);
            copyFile.setParentFolderId(targetFolderId);
            copyFile.setCreateTime(LocalDateTime.now());
            copyFile.setUpdateTime(LocalDateTime.now());

            if (fileInfoMapper.insert(copyFile) > 0) {
                return copyFile;
            }
            return null;
        } catch (Exception e) {
            logger.error("复制文件失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public FileInfo updateFile(FileInfo fileInfo) {
        try {
            fileInfo.setUpdateTime(LocalDateTime.now());
            if (fileInfoMapper.updateById(fileInfo) > 0) {
                return fileInfo;
            }
            return null;
        } catch (Exception e) {
            logger.error("更新文件失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public FileInfo createFolder(String folderName, Long parentId, Long userId) {
        try {
            // 检查文件夹名称是否已存在
            if (isFolderNameExists(folderName, parentId)) {
                throw new RuntimeException("文件夹名称已存在");
            }

            FileInfo folder = new FileInfo();
            folder.setFileName(folderName);
            folder.setFileType("folder");
            folder.setFileSize(0L);
            folder.setUploadUserId(userId);
            folder.setParentFolderId(parentId);
            folder.setCreateTime(LocalDateTime.now());
            folder.setUpdateTime(LocalDateTime.now());

            if (fileInfoMapper.insert(folder) > 0) {
                return folder;
            }
            return null;
        } catch (Exception e) {
            logger.error("创建文件夹失败", e);
            return null;
        }
    }

    @Override
    @Transactional
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

            // 检查文件夹是否为空
            List<FileInfo> children = fileInfoMapper.selectByParentId(folderId);
            if (!children.isEmpty()) {
                throw new RuntimeException("文件夹不为空，请先删除其中的文件");
            }

            return fileInfoMapper.deleteById(folderId) > 0;
        } catch (Exception e) {
            logger.error("删除文件夹失败", e);
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> getFolderStructure(Long parentId) {
        try {
            List<FileInfo> folders = fileInfoMapper.selectFoldersByParentId(parentId);
            List<Map<String, Object>> result = new ArrayList<>();

            for (FileInfo folder : folders) {
                Map<String, Object> folderInfo = new HashMap<>();
                folderInfo.put("id", folder.getId());
                folderInfo.put("name", folder.getFileName());
                folderInfo.put("parentId", folder.getParentFolderId());
                folderInfo.put("children", getFolderStructure(folder.getId()));
                result.add(folderInfo);
            }

            return result;
        } catch (Exception e) {
            logger.error("获取文件夹结构失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public boolean cleanupTempFiles() {
        try {
            // TODO: 实现临时文件清理逻辑
            logger.info("清理临时文件");
            return true;
        } catch (Exception e) {
            logger.error("清理临时文件失败", e);
            return false;
        }
    }

    @Override
    public boolean backupFile(Long id) {
        try {
            // TODO: 实现文件备份逻辑
            logger.info("备份文件: {}", id);
            return true;
        } catch (Exception e) {
            logger.error("备份文件失败", e);
            return false;
        }
    }

    @Override
    public boolean restoreFile(Long id) {
        try {
            // TODO: 实现文件恢复逻辑
            logger.info("恢复文件: {}", id);
            return true;
        } catch (Exception e) {
            logger.error("恢复文件失败", e);
            return false;
        }
    }

    /**
     * 检查文件夹名称是否已存在
     */
    private boolean isFolderNameExists(String folderName, Long parentId) {
        List<FileInfo> files = fileInfoMapper.selectByParentId(parentId);
        return files.stream().anyMatch(file -> 
            "folder".equals(file.getFileType()) && folderName.equals(file.getFileName())
        );
    }
}
