package com.esfile.controller.file;

import com.esfile.common.vo.ResponseResult;
import com.esfile.entity.dto.FileUploadDto;
import com.esfile.entity.dto.FileSearchDto;
import com.esfile.entity.mybatis.FileInfo;
import com.esfile.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * 文件管理控制器
 */
@RestController
@RequestMapping("/api/files")
public class FileController {

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
            // 构建搜索DTO
            FileSearchDto searchDto = new FileSearchDto();
            searchDto.setPage(page);
            searchDto.setSize(size);
            searchDto.setKeyword(keyword);
            searchDto.setFileType(fileType);
            searchDto.setOrderBy(sortBy);
            searchDto.setOrderDirection(sortOrder);
            
            // 处理时间范围
            if (dateRange != null && !dateRange.isEmpty()) {
                // TODO: 解析时间范围字符串，设置startTime和endTime
            }
            
            // 处理大小范围
            if (sizeRange != null && !sizeRange.isEmpty()) {
                // TODO: 解析大小范围字符串，设置minSize和maxSize
            }
            
            // 处理标签
            if (tags != null && tags.length > 0) {
                searchDto.setTags(String.join(",", tags));
            }
            
            // 调用正确的Service方法
            Map<String, Object> result = fileService.getFileList(searchDto);
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.fail("获取文件列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件详情
     */
    @GetMapping("/{id}")
    public ResponseResult<FileInfo> getFileDetail(@PathVariable Long id) {
        try {
            FileInfo fileInfo = fileService.getFileDetail(id);
            if (fileInfo != null) {
                return ResponseResult.success(fileInfo);
            } else {
                return ResponseResult.fail("文件不存在");
            }
        } catch (Exception e) {
            return ResponseResult.fail("获取文件详情失败: " + e.getMessage());
        }
    }

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public ResponseResult<FileInfo> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String tags,
            @RequestParam(defaultValue = "0") Integer isPublic,
            @RequestParam(required = false) Long parentFolderId) {
        
        try {
            // 构建上传DTO
            FileUploadDto uploadDto = new FileUploadDto();
            uploadDto.setFile(file);
            uploadDto.setDescription(description);
            uploadDto.setTags(tags);
            uploadDto.setIsPublic(isPublic);
            uploadDto.setParentFolderId(parentFolderId);
            
            // 设置用户信息（从SecurityContext获取）
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                // TODO: 根据用户名获取用户ID
                uploadDto.setUploadUserId(1L); // 临时设置，需要实现用户ID获取逻辑
                uploadDto.setUploadUserName(userDetails.getUsername());
            } else {
                // 如果没有认证信息，设置默认值
                uploadDto.setUploadUserId(1L);
                uploadDto.setUploadUserName("anonymous");
            }
            
            // 调用正确的Service方法
            FileInfo fileInfo = fileService.uploadFile(uploadDto);
            return ResponseResult.success(fileInfo);
        } catch (Exception e) {
            return ResponseResult.fail("文件上传失败: " + e.getMessage());
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
            @RequestParam(defaultValue = "0") Integer isPublic,
            @RequestParam(required = false) Long parentFolderId) {
        
        try {
            // 构建批量上传DTO列表
            List<FileUploadDto> uploadDtos = new ArrayList<>();
            
            for (MultipartFile file : files) {
                FileUploadDto uploadDto = new FileUploadDto();
                uploadDto.setFile(file);
                uploadDto.setDescription(description);
                uploadDto.setTags(tags);
                uploadDto.setIsPublic(isPublic);
                uploadDto.setParentFolderId(parentFolderId);
                
                // 设置用户信息
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.getPrincipal() instanceof UserDetails) {
                    UserDetails userDetails = (UserDetails) auth.getPrincipal();
                    uploadDto.setUploadUserId(1L); // 临时设置
                    uploadDto.setUploadUserName(userDetails.getUsername());
                } else {
                    uploadDto.setUploadUserId(1L);
                    uploadDto.setUploadUserName("anonymous");
                }
                
                uploadDtos.add(uploadDto);
            }
            
            // 调用正确的Service方法
            List<FileInfo> fileInfos = fileService.batchUploadFiles(uploadDtos);
            return ResponseResult.success(fileInfos);
        } catch (Exception e) {
            return ResponseResult.fail("批量上传失败: " + e.getMessage());
        }
    }

    /**
     * 下载文件
     */
    @GetMapping("/{id}/download")
    public void downloadFile(@PathVariable Long id, HttpServletResponse response) {
        try {
            // 获取当前用户ID
            Long userId = getCurrentUserId();
            fileService.downloadFile(id, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 批量下载文件
     */
    @PostMapping("/batch-download")
    public void batchDownloadFiles(@RequestBody List<Long> ids, HttpServletResponse response) {
        try {
            fileService.batchDownloadFiles(ids, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/{id}")
    public ResponseResult<String> deleteFile(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            boolean success = fileService.deleteFile(id, userId);
            if (success) {
                return ResponseResult.success("文件删除成功");
            } else {
                return ResponseResult.fail("文件删除失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("文件删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除文件
     */
    @PostMapping("/batch-delete")
    public ResponseResult<String> batchDeleteFiles(@RequestBody List<Long> ids) {
        try {
            Long userId = getCurrentUserId();
            boolean success = fileService.batchDeleteFiles(ids, userId);
            if (success) {
                return ResponseResult.success("批量删除成功");
            } else {
                return ResponseResult.fail("批量删除失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 更新文件信息
     */
    @PutMapping("/{id}")
    public ResponseResult<FileInfo> updateFile(@PathVariable Long id, @RequestBody FileInfo fileInfo) {
        try {
            fileInfo.setId(id);
            FileInfo updatedFile = fileService.updateFile(fileInfo);
            return ResponseResult.success(updatedFile);
        } catch (Exception e) {
            return ResponseResult.fail("更新文件失败: " + e.getMessage());
        }
    }

    /**
     * 移动文件
     */
    @PostMapping("/{id}/move")
    public ResponseResult<String> moveFile(@PathVariable Long id, @RequestParam Long targetFolderId) {
        try {
            Long userId = getCurrentUserId();
            boolean success = fileService.moveFile(id, targetFolderId, userId);
            if (success) {
                return ResponseResult.success("文件移动成功");
            } else {
                return ResponseResult.fail("文件移动失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("文件移动失败: " + e.getMessage());
        }
    }

    /**
     * 复制文件
     */
    @PostMapping("/{id}/copy")
    public ResponseResult<FileInfo> copyFile(@PathVariable Long id, @RequestParam Long targetFolderId) {
        try {
            Long userId = getCurrentUserId();
            FileInfo copiedFile = fileService.copyFile(id, targetFolderId, userId);
            return ResponseResult.success(copiedFile);
        } catch (Exception e) {
            return ResponseResult.fail("文件复制失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件预览信息
     */
    @GetMapping("/{id}/preview")
    public ResponseResult<Map<String, Object>> getFilePreview(@PathVariable Long id) {
        try {
            Map<String, Object> previewInfo = fileService.getFilePreview(id);
            return ResponseResult.success(previewInfo);
        } catch (Exception e) {
            return ResponseResult.fail("获取预览信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件内容（用于文本文件）
     */
    @GetMapping("/{id}/content")
    public ResponseResult<Map<String, String>> getFileContent(@PathVariable Long id) {
        try {
            String content = fileService.getFileContent(id);
            Map<String, String> result = new HashMap<>();
            result.put("content", content);
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.fail("获取文件内容失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件缩略图
     */
    @GetMapping("/{id}/thumbnail")
    public void getFileThumbnail(@PathVariable Long id, 
                                @RequestParam(defaultValue = "200") Integer width,
                                @RequestParam(defaultValue = "200") Integer height,
                                HttpServletResponse response) {
        try {
            byte[] thumbnail = fileService.getFileThumbnail(id);
            if (thumbnail != null && thumbnail.length > 0) {
                response.setContentType("image/jpeg");
                response.getOutputStream().write(thumbnail);
                response.getOutputStream().flush();
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 搜索文件
     */
    @PostMapping("/search")
    public ResponseResult<Map<String, Object>> searchFiles(@RequestBody FileSearchDto searchDto) {
        try {
            Map<String, Object> result = fileService.searchFiles(searchDto);
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.fail("搜索文件失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件统计信息
     */
    @GetMapping("/stats")
    public ResponseResult<Map<String, Object>> getFileStats() {
        try {
            Map<String, Object> stats = fileService.getFileStats();
            return ResponseResult.success(stats);
        } catch (Exception e) {
            return ResponseResult.fail("获取统计信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件夹结构
     */
    @GetMapping("/folders")
    public ResponseResult<List<Map<String, Object>>> getFolderStructure(
            @RequestParam(required = false) Long parentId) {
        try {
            List<Map<String, Object>> folders = fileService.getFolderStructure(parentId);
            return ResponseResult.success(folders);
        } catch (Exception e) {
            return ResponseResult.fail("获取文件夹结构失败: " + e.getMessage());
        }
    }

    /**
     * 创建文件夹
     */
    @PostMapping("/folders")
    public ResponseResult<FileInfo> createFolder(@RequestBody Map<String, String> folderInfo) {
        try {
            String folderName = folderInfo.get("name");
            Long parentId = folderInfo.get("parentId") != null ? 
                Long.parseLong(folderInfo.get("parentId")) : null;
            String description = folderInfo.get("description");
            
            Long userId = getCurrentUserId();
            FileInfo folder = fileService.createFolder(folderName, parentId, userId);
            return ResponseResult.success(folder);
        } catch (Exception e) {
            return ResponseResult.fail("创建文件夹失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件夹
     */
    @DeleteMapping("/folders/{id}")
    public ResponseResult<String> deleteFolder(@PathVariable Long id, 
                                             @RequestParam(defaultValue = "false") boolean recursive) {
        try {
            Long userId = getCurrentUserId();
            boolean success = fileService.deleteFolder(id, userId);
            if (success) {
                return ResponseResult.success("文件夹删除成功");
            } else {
                return ResponseResult.fail("文件夹删除失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("文件夹删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            // TODO: 根据用户名获取用户ID，这里临时返回1
            return 1L;
        }
        return 1L; // 临时返回默认值
    }
}
