package com.esfile.controller.file;

import com.esfile.common.vo.ResponseResult;
import com.esfile.entity.dto.FileUploadDto;
import com.esfile.entity.mybatis.FileInfo;
import com.esfile.service.file.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.ArrayList;

/**
 * 文件上传控制器
 * 负责文件的上传功能
 * 
 * @author esfile
 * @since 1.0.0
 */
@RestController
@RequestMapping("/files/upload")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileService fileService;

    /**
     * 单文件上传
     */
    @PostMapping("/upload")
    public ResponseResult<FileInfo> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) Boolean isPublic,
            @RequestParam(required = false) Long parentFolderId) {
        
        try {
            logger.info("上传文件: {}", file.getOriginalFilename());
            
            // 获取当前用户ID
            Long userId = getCurrentUserId();
            
            // 构建上传DTO
            FileUploadDto uploadDto = new FileUploadDto();
            uploadDto.setFile(file);
            uploadDto.setDescription(description);
            uploadDto.setTags(tags);
            uploadDto.setIsPublic(isPublic != null ? (isPublic ? 1 : 0) : 0);
            uploadDto.setParentFolderId(parentFolderId);
            uploadDto.setUploadUserId(userId);
            
            FileInfo fileInfo = fileService.uploadFile(uploadDto);
            return ResponseResult.success(fileInfo);
        } catch (Exception e) {
            logger.error("上传文件失败", e);
            return ResponseResult.error("上传文件失败: " + e.getMessage());
        }
    }

    /**
     * 批量上传文件
     */
    @PostMapping("/batch-upload")
    public ResponseResult<List<FileInfo>> batchUploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) Boolean isPublic,
            @RequestParam(required = false) Long parentFolderId) {
        
        try {
            logger.info("批量上传文件: {} 个文件", files.length);
            
            // 获取当前用户ID
            Long userId = getCurrentUserId();
            
            // 构建上传DTO列表
            List<FileUploadDto> uploadDtos = new ArrayList<>();
            for (MultipartFile file : files) {
                FileUploadDto uploadDto = new FileUploadDto();
                uploadDto.setFile(file);
                uploadDto.setDescription(description);
                uploadDto.setTags(tags);
                uploadDto.setIsPublic(isPublic != null ? (isPublic ? 1 : 0) : 0);
                uploadDto.setParentFolderId(parentFolderId);
                uploadDto.setUploadUserId(userId);
                uploadDtos.add(uploadDto);
            }
            
            List<FileInfo> fileInfos = fileService.batchUploadFiles(uploadDtos);
            return ResponseResult.success(fileInfos);
        } catch (Exception e) {
            logger.error("批量上传文件失败", e);
            return ResponseResult.error("批量上传文件失败: " + e.getMessage());
        }
    }

    /**
     * 创建文件夹
     */
    @PostMapping("/folders")
    public ResponseResult<FileInfo> createFolder(
            @RequestParam String folderName,
            @RequestParam(required = false) Long parentId) {
        
        try {
            logger.info("创建文件夹: {} in {}", folderName, parentId);
            
            // 获取当前用户ID
            Long userId = getCurrentUserId();
            
            FileInfo folder = fileService.createFolder(folderName, parentId, userId);
            if (folder == null) {
                return ResponseResult.error("创建文件夹失败");
            }
            
            return ResponseResult.success(folder);
        } catch (Exception e) {
            logger.error("创建文件夹失败", e);
            return ResponseResult.error("创建文件夹失败: " + e.getMessage());
        }
    }

    /**
     * 获取上传进度（预留接口）
     */
    @GetMapping("/upload/progress/{taskId}")
    public ResponseResult<Object> getUploadProgress(@PathVariable String taskId) {
        try {
            logger.info("获取上传进度: taskId={}", taskId);
            
            // TODO: 实现上传进度查询
            Object progress = new Object();
            return ResponseResult.success(progress);
        } catch (Exception e) {
            logger.error("获取上传进度失败", e);
            return ResponseResult.error("获取上传进度失败: " + e.getMessage());
        }
    }

    /**
     * 取消上传（预留接口）
     */
    @PostMapping("/upload/cancel/{taskId}")
    public ResponseResult<String> cancelUpload(@PathVariable String taskId) {
        try {
            logger.info("取消上传: taskId={}", taskId);
            
            // TODO: 实现上传取消逻辑
            return ResponseResult.success("上传已取消");
        } catch (Exception e) {
            logger.error("取消上传失败", e);
            return ResponseResult.error("取消上传失败: " + e.getMessage());
        }
    }

    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                // TODO: 从UserDetails中获取实际的用户ID
                // 这里暂时返回固定值，实际应该从用户服务获取
                return 1L;
            }
        }
        return 1L; // 默认用户ID
    }
}
