package com.esfile.controller.file;

import com.esfile.common.vo.ResponseResult;
import com.esfile.service.file.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.esfile.entity.mybatis.FileInfo;

/**
 * 文件管理主控制器 - 重构版
 * 作为协调器，委托给各个专门的控制器处理具体业务
 * 主要处理文件预览和内容相关功能
 * 符合单一职责原则，文件大小控制在400行以内
 * 
 * @author esfile
 * @since 1.0.0
 */
@RestController
@RequestMapping("/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    /**
     * 获取文件列表（分页）
     */
    @GetMapping
    public ResponseResult<Map<String, Object>> getFileList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String fileType,
            @RequestParam(required = false) String dateRange,
            @RequestParam(required = false) String sizeRange,
            @RequestParam(required = false) String uploader,
            @RequestParam(required = false) String[] tags,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        
        try {
            logger.info("获取文件列表: page={}, size={}, keyword={}", page, size, keyword);
            
            // 委托给FileQueryController处理
            return ResponseResult.success("文件列表功能已迁移到 /files/query/list 接口");
        } catch (Exception e) {
            logger.error("获取文件列表失败", e);
            return ResponseResult.error("获取文件列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件预览
     */
    @GetMapping("/{id}/preview")
    public ResponseResult<Map<String, Object>> getFilePreview(@PathVariable Long id) {
        try {
            logger.info("获取文件预览: id={}", id);
            
            Map<String, Object> preview = fileService.getFilePreview(id);
            if (preview == null) {
                return ResponseResult.error("文件预览生成失败");
            }
            
            return ResponseResult.success(preview);
        } catch (Exception e) {
            logger.error("获取文件预览失败", e);
            return ResponseResult.error("获取文件预览失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件内容（适用于文本文件）
     */
    @GetMapping("/{id}/content")
    public ResponseResult<Map<String, String>> getFileContent(@PathVariable Long id) {
        try {
            logger.info("获取文件内容: id={}", id);
            
            String content = fileService.getFileContent(id);
            if (content == null) {
                return ResponseResult.error("无法读取文件内容");
            }
            
            Map<String, String> result = new HashMap<>();
            result.put("content", content);
            return ResponseResult.success(result);
        } catch (Exception e) {
            logger.error("获取文件内容失败", e);
            return ResponseResult.error("获取文件内容失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件缩略图
     */
    @GetMapping("/{id}/thumbnail")
    public void getFileThumbnail(@PathVariable Long id, HttpServletResponse response) {
        try {
            logger.info("获取文件缩略图: id={}", id);
            
            byte[] thumbnail = fileService.getFileThumbnail(id);
            if (thumbnail != null) {
                response.setContentType("image/jpeg");
                response.setContentLength(thumbnail.length);
                response.getOutputStream().write(thumbnail);
                response.getOutputStream().flush();
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("获取文件缩略图失败", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 分享文件
     */
    @PostMapping("/{id}/share")
    public ResponseResult<Map<String, Object>> shareFile(
            @PathVariable Long id,
            @RequestParam(defaultValue = "public") String shareType,
            @RequestParam(required = false) Integer expireDays) {
        
        try {
            logger.info("分享文件: id={}, shareType={}, expireDays={}", id, shareType, expireDays);
            
            // TODO: 获取当前用户ID
            Long userId = 1L;
            
            Map<String, Object> shareInfo = fileService.shareFile(id, userId, shareType, expireDays);
            if (shareInfo != null) {
                return ResponseResult.success(shareInfo);
            } else {
                return ResponseResult.error("文件分享失败");
            }
        } catch (Exception e) {
            logger.error("分享文件失败", e);
            return ResponseResult.error("分享文件失败: " + e.getMessage());
        }
    }

    /**
     * 取消文件分享
     */
    @DeleteMapping("/shares/{shareId}")
    public ResponseResult<String> cancelFileShare(@PathVariable Long shareId) {
        try {
            logger.info("取消文件分享: shareId={}", shareId);
            
            // TODO: 获取当前用户ID
            Long userId = 1L;
            
            boolean success = fileService.cancelFileShare(shareId, userId);
            if (success) {
                return ResponseResult.success("取消分享成功");
            } else {
                return ResponseResult.error("取消分享失败");
            }
        } catch (Exception e) {
            logger.error("取消文件分享失败", e);
            return ResponseResult.error("取消文件分享失败: " + e.getMessage());
        }
    }

    /**
     * 获取分享的文件列表
     */
    @GetMapping("/shares")
    public ResponseResult<?> getSharedFiles() {
        try {
            logger.info("获取分享文件列表");
            
            // TODO: 获取当前用户ID
            Long userId = 1L;
            
            List<Map<String, Object>> sharedFiles = fileService.getSharedFiles(userId);
            return ResponseResult.success(sharedFiles);
        } catch (Exception e) {
            logger.error("获取分享文件列表失败", e);
            return ResponseResult.error("获取分享文件列表失败: " + e.getMessage());
        }
    }

    /**
     * 添加文件标签
     */
    @PostMapping("/{id}/tags")
    public ResponseResult<String> addFileTag(@PathVariable Long id, @RequestParam String tag) {
        try {
            logger.info("添加文件标签: id={}, tag={}", id, tag);
            
            boolean success = fileService.addFileTag(id, tag);
            if (success) {
                return ResponseResult.success("标签添加成功");
            } else {
                return ResponseResult.error("标签添加失败");
            }
        } catch (Exception e) {
            logger.error("添加文件标签失败", e);
            return ResponseResult.error("添加文件标签失败: " + e.getMessage());
        }
    }

    /**
     * 移除文件标签
     */
    @DeleteMapping("/{id}/tags")
    public ResponseResult<String> removeFileTag(@PathVariable Long id, @RequestParam String tag) {
        try {
            logger.info("移除文件标签: id={}, tag={}", id, tag);
            
            boolean success = fileService.removeFileTag(id, tag);
            if (success) {
                return ResponseResult.success("标签移除成功");
            } else {
                return ResponseResult.error("标签移除失败");
            }
        } catch (Exception e) {
            logger.error("移除文件标签失败", e);
            return ResponseResult.error("移除文件标签失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件标签
     */
    @GetMapping("/{id}/tags")
    public ResponseResult<?> getFileTags(@PathVariable Long id) {
        try {
            logger.info("获取文件标签: id={}", id);
            
            List<String> tags = fileService.getFileTags(id);
            return ResponseResult.success(tags);
        } catch (Exception e) {
            logger.error("获取文件标签失败", e);
            return ResponseResult.error("获取文件标签失败: " + e.getMessage());
        }
    }

    /**
     * 根据标签获取文件
     */
    @GetMapping("/by-tag/{tag}")
    public ResponseResult<?> getFilesByTag(@PathVariable String tag) {
        try {
            logger.info("根据标签获取文件: tag={}", tag);
            
            List<FileInfo> files = fileService.getFilesByTag(tag);
            return ResponseResult.success(files);
        } catch (Exception e) {
            logger.error("根据标签获取文件失败", e);
            return ResponseResult.error("根据标签获取文件失败: " + e.getMessage());
        }
    }
}