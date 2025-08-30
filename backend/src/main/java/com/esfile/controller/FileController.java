package com.esfile.controller;

import com.esfile.common.vo.PageResult;
import com.esfile.common.vo.ResponseResult;
import com.esfile.entity.dto.FileSearchDto;
import com.esfile.entity.dto.FileUploadDto;
import com.esfile.entity.mybatis.FileInfo;
import com.esfile.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 文件管理控制器
 * 提供完整的文件管理API接口
 */
@RestController
@RequestMapping("/api/files")
public class FileController {
    
    @Autowired
    private FileService fileService;

    /**
     * 分页查询文件列表
     */
    @GetMapping
    public ResponseResult<Map<String, Object>> listFiles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String fileType,
            @RequestParam(required = false) String dateRange,
            @RequestParam(required = false) String sizeRange,
            @RequestParam(required = false) String uploader,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        
        try {
            FileSearchDto searchDto = new FileSearchDto();
            searchDto.setPage(page);
            searchDto.setSize(size);
            searchDto.setKeyword(keyword);
            searchDto.setFileType(fileType);
            searchDto.setDateRange(dateRange != null ? dateRange.split(",") : null);
            searchDto.setSizeRange(sizeRange != null ? parseSizeRange(sizeRange) : null);
            searchDto.setUploader(uploader);
            searchDto.setTags(tags);
            searchDto.setSortBy(sortBy);
            searchDto.setSortOrder(sortOrder);
            
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
            if (fileInfo == null) {
                return ResponseResult.fail("文件不存在");
            }
            return ResponseResult.success(fileInfo);
        } catch (Exception e) {
            return ResponseResult.fail("获取文件详情失败: " + e.getMessage());
        }
    }

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public ResponseResult<FileInfo> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long userId,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false, defaultValue = "0") Integer isPublic) {
        
        try {
            FileUploadDto uploadDto = new FileUploadDto();
            uploadDto.setFile(file);
            uploadDto.setUserId(userId);
            uploadDto.setDescription(description);
            uploadDto.setTags(tags);
            uploadDto.setIsPublic(isPublic);
            
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
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam Long userId,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false, defaultValue = "0") Integer isPublic) {
        
        try {
            List<FileUploadDto> uploadDtos = files.stream()
                    .map(file -> {
                        FileUploadDto dto = new FileUploadDto();
                        dto.setFile(file);
                        dto.setUserId(userId);
                        dto.setDescription(description);
                        dto.setTags(tags);
                        dto.setIsPublic(isPublic);
                        return dto;
                    })
                    .collect(java.util.stream.Collectors.toList());
            
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
            fileService.downloadFile(id, response);
        } catch (Exception e) {
            // 下载失败时设置错误响应
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
    public ResponseResult<Boolean> deleteFile(@PathVariable Long id, @RequestParam Long userId) {
        try {
            boolean result = fileService.deleteFile(id, userId);
            if (result) {
                return ResponseResult.success(true, "文件删除成功");
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
    public ResponseResult<Boolean> batchDeleteFiles(@RequestBody List<Long> ids, @RequestParam Long userId) {
        try {
            boolean result = fileService.batchDeleteFiles(ids, userId);
            if (result) {
                return ResponseResult.success(true, "批量删除成功");
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
    public ResponseResult<Boolean> moveFile(
            @PathVariable Long id,
            @RequestParam Long targetFolderId,
            @RequestParam Long userId) {
        try {
            boolean result = fileService.moveFile(id, targetFolderId, userId);
            if (result) {
                return ResponseResult.success(true, "文件移动成功");
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
    public ResponseResult<FileInfo> copyFile(
            @PathVariable Long id,
            @RequestParam Long targetFolderId,
            @RequestParam Long userId) {
        try {
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
            Map<String, Object> preview = fileService.getFilePreview(id);
            return ResponseResult.success(preview);
        } catch (Exception e) {
            return ResponseResult.fail("获取预览信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件内容（文本文件）
     */
    @GetMapping("/{id}/content")
    public ResponseResult<String> getFileContent(@PathVariable Long id) {
        try {
            String content = fileService.getFileContent(id);
            return ResponseResult.success(content);
        } catch (Exception e) {
            return ResponseResult.fail("获取文件内容失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件缩略图
     */
    @GetMapping("/{id}/thumbnail")
    public void getFileThumbnail(
            @PathVariable Long id,
            @RequestParam(defaultValue = "medium") String size,
            HttpServletResponse response) {
        try {
            byte[] thumbnail = fileService.getFileThumbnail(id);
            response.setContentType("image/jpeg");
            response.getOutputStream().write(thumbnail);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 搜索文件
     */
    @GetMapping("/search")
    public ResponseResult<Map<String, Object>> searchFiles(
            @RequestParam String keyword,
            @RequestParam(required = false) String fileType,
            @RequestParam(required = false) String dateRange,
            @RequestParam(required = false) String sizeRange,
            @RequestParam(required = false) String uploader,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        try {
            FileSearchDto searchDto = new FileSearchDto();
            searchDto.setKeyword(keyword);
            searchDto.setFileType(fileType);
            searchDto.setDateRange(dateRange != null ? dateRange.split(",") : null);
            searchDto.setSizeRange(sizeRange != null ? parseSizeRange(sizeRange) : null);
            searchDto.setUploader(uploader);
            searchDto.setTags(tags);
            searchDto.setPage(page);
            searchDto.setSize(size);
            
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
            List<Map<String, Object>> structure = fileService.getFolderStructure(parentId);
            return ResponseResult.success(structure);
        } catch (Exception e) {
            return ResponseResult.fail("获取文件夹结构失败: " + e.getMessage());
        }
    }

    /**
     * 创建文件夹
     */
    @PostMapping("/folders")
    public ResponseResult<FileInfo> createFolder(
            @RequestParam String name,
            @RequestParam(required = false) Long parentId,
            @RequestParam Long userId,
            @RequestParam(required = false) String description) {
        try {
            FileInfo folder = fileService.createFolder(name, parentId, userId);
            return ResponseResult.success(folder);
        } catch (Exception e) {
            return ResponseResult.fail("创建文件夹失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件夹
     */
    @DeleteMapping("/folders/{id}")
    public ResponseResult<Boolean> deleteFolder(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "false") boolean recursive) {
        try {
            boolean result = fileService.deleteFolder(id, userId);
            if (result) {
                return ResponseResult.success(true, "文件夹删除成功");
            } else {
                return ResponseResult.fail("文件夹删除失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("文件夹删除失败: " + e.getMessage());
        }
    }

    /**
     * 解析大小范围
     */
    private Long[] parseSizeRange(String sizeRange) {
        try {
            String[] parts = sizeRange.split("-");
            if (parts.length == 2) {
                return new Long[]{Long.parseLong(parts[0]), Long.parseLong(parts[1])};
            }
        } catch (NumberFormatException e) {
            // 忽略解析错误
        }
        return null;
    }
}
