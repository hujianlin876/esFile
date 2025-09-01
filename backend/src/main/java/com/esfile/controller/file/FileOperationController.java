package com.esfile.controller.file;

import com.esfile.common.vo.ResponseResult;
import com.esfile.entity.mybatis.FileInfo;
import com.esfile.service.file.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文件操作控制器
 * 负责文件的增删改查、移动、复制等操作
 * 
 * @author esfile
 * @since 1.0.0
 */
@RestController
@RequestMapping("/files/operation")
public class FileOperationController {

    private static final Logger logger = LoggerFactory.getLogger(FileOperationController.class);

    @Autowired
    private FileService fileService;

    /**
     * 下载文件
     */
    @GetMapping("/{id}/download")
    public void downloadFile(@PathVariable Long id, HttpServletResponse response) {
        try {
            logger.info("下载文件: id={}", id);
            fileService.downloadFile(id, response);
        } catch (Exception e) {
            logger.error("下载文件失败", e);
            // 设置错误响应
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 批量下载文件
     */
    @PostMapping("/batch-download")
    public void batchDownloadFiles(@RequestBody List<Long> ids, HttpServletResponse response) {
        try {
            logger.info("批量下载文件: {} 个文件", ids.size());
            fileService.batchDownloadFiles(ids, response);
        } catch (Exception e) {
            logger.error("批量下载文件失败", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/{id}")
    public ResponseResult<String> deleteFile(@PathVariable Long id) {
        try {
            logger.info("删除文件: id={}", id);
            
            // 获取当前用户ID
            Long userId = getCurrentUserId();
            
            boolean success = fileService.deleteFile(id, userId);
            if (success) {
                return ResponseResult.success("文件删除成功");
            } else {
                return ResponseResult.error("文件删除失败");
            }
        } catch (Exception e) {
            logger.error("删除文件失败", e);
            return ResponseResult.error("删除文件失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除文件
     */
    @PostMapping("/batch-delete")
    public ResponseResult<String> batchDeleteFiles(@RequestBody List<Long> ids) {
        try {
            logger.info("批量删除文件: {} 个文件", ids.size());
            
            // 获取当前用户ID
            Long userId = getCurrentUserId();
            
            boolean success = fileService.batchDeleteFiles(ids, userId);
            if (success) {
                return ResponseResult.success("文件批量删除成功");
            } else {
                return ResponseResult.error("文件批量删除失败");
            }
        } catch (Exception e) {
            logger.error("批量删除文件失败", e);
            return ResponseResult.error("批量删除文件失败: " + e.getMessage());
        }
    }

    /**
     * 更新文件信息
     */
    @PutMapping("/{id}")
    public ResponseResult<FileInfo> updateFile(@PathVariable Long id, @RequestBody FileInfo fileInfo) {
        try {
            logger.info("更新文件信息: id={}", id);
            
            fileInfo.setId(id);
            FileInfo updatedFile = fileService.updateFile(fileInfo);
            if (updatedFile != null) {
                return ResponseResult.success(updatedFile);
            } else {
                return ResponseResult.error("文件更新失败");
            }
        } catch (Exception e) {
            logger.error("更新文件失败", e);
            return ResponseResult.error("更新文件失败: " + e.getMessage());
        }
    }

    /**
     * 移动文件
     */
    @PostMapping("/{id}/move")
    public ResponseResult<String> moveFile(@PathVariable Long id, @RequestParam Long targetFolderId) {
        try {
            logger.info("移动文件: id={} -> folderId={}", id, targetFolderId);
            
            // 获取当前用户ID
            Long userId = getCurrentUserId();
            
            boolean success = fileService.moveFile(id, targetFolderId, userId);
            if (success) {
                return ResponseResult.success("文件移动成功");
            } else {
                return ResponseResult.error("文件移动失败");
            }
        } catch (Exception e) {
            logger.error("移动文件失败", e);
            return ResponseResult.error("移动文件失败: " + e.getMessage());
        }
    }

    /**
     * 复制文件
     */
    @PostMapping("/{id}/copy")
    public ResponseResult<FileInfo> copyFile(@PathVariable Long id, @RequestParam Long targetFolderId) {
        try {
            logger.info("复制文件: id={} -> folderId={}", id, targetFolderId);
            
            // 获取当前用户ID
            Long userId = getCurrentUserId();
            
            FileInfo copiedFile = fileService.copyFile(id, targetFolderId, userId);
            if (copiedFile != null) {
                return ResponseResult.success(copiedFile);
            } else {
                return ResponseResult.error("文件复制失败");
            }
        } catch (Exception e) {
            logger.error("复制文件失败", e);
            return ResponseResult.error("复制文件失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件夹
     */
    @DeleteMapping("/folders/{id}")
    public ResponseResult<String> deleteFolder(@PathVariable Long id) {
        try {
            logger.info("删除文件夹: id={}", id);
            
            // 获取当前用户ID
            Long userId = getCurrentUserId();
            
            boolean success = fileService.deleteFolder(id, userId);
            if (success) {
                return ResponseResult.success("文件夹删除成功");
            } else {
                return ResponseResult.error("文件夹删除失败");
            }
        } catch (Exception e) {
            logger.error("删除文件夹失败", e);
            return ResponseResult.error("删除文件夹失败: " + e.getMessage());
        }
    }

    /**
     * 清理临时文件
     */
    @PostMapping("/cleanup")
    public ResponseResult<String> cleanupTempFiles() {
        try {
            logger.info("清理临时文件");
            
            boolean success = fileService.cleanupTempFiles();
            if (success) {
                return ResponseResult.success("临时文件清理成功");
            } else {
                return ResponseResult.error("临时文件清理失败");
            }
        } catch (Exception e) {
            logger.error("清理临时文件失败", e);
            return ResponseResult.error("清理临时文件失败: " + e.getMessage());
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
