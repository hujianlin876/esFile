package com.esfile.controller;

import com.esfile.common.vo.ResponseResult;
import com.esfile.service.file.MinioStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MinIO存储控制器
 * 提供文件存储相关的API接口
 * 
 * @author esfile
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/storage")
public class MinioStorageController {

    @Autowired
    private MinioStorageService minioStorageService;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public ResponseResult<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "path", defaultValue = "") String path) {
        
        try {
            // 生成对象名称
            String objectName = generateObjectName(file.getOriginalFilename(), path);
            
            // 上传文件到MinIO
            String fileUrl = minioStorageService.uploadFile(file, objectName);
            
            Map<String, Object> result = new HashMap<>();
            result.put("objectName", objectName);
            result.put("fileUrl", fileUrl);
            result.put("fileName", file.getOriginalFilename());
            result.put("fileSize", file.getSize());
            result.put("contentType", file.getContentType());
            
            return ResponseResult.success(result);
            
        } catch (Exception e) {
            return ResponseResult.fail("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 下载文件
     */
    @GetMapping("/download")
    public void downloadFile(
            @RequestParam("objectName") String objectName,
            @RequestParam(value = "fileName", required = false) String fileName,
            HttpServletResponse response) {
        
        try {
            // 获取文件信息
            Map<String, Object> fileInfo = minioStorageService.getFileInfo(objectName);
            
            // 设置响应头
            String downloadFileName = fileName != null ? fileName : objectName.substring(objectName.lastIndexOf("/") + 1);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
            
            if (fileInfo.containsKey("size")) {
                response.setContentLengthLong((Long) fileInfo.get("size"));
            }
            
            // 下载文件
            try (InputStream inputStream = minioStorageService.downloadFile(objectName);
                 java.io.OutputStream outputStream = response.getOutputStream()) {
                
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }
            
        } catch (Exception e) {
            try {
                response.setStatus(500);
                response.getWriter().write("文件下载失败: " + e.getMessage());
            } catch (Exception ex) {
                // 忽略异常
            }
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/delete")
    public ResponseResult<Boolean> deleteFile(@RequestParam("objectName") String objectName) {
        try {
            boolean result = minioStorageService.deleteFile(objectName);
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.fail("文件删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除文件
     */
    @DeleteMapping("/batch-delete")
    public ResponseResult<Map<String, Object>> batchDeleteFiles(@RequestBody List<String> objectNames) {
        try {
            int successCount = minioStorageService.batchDeleteFiles(objectNames);
            
            Map<String, Object> result = new HashMap<>();
            result.put("total", objectNames.size());
            result.put("success", successCount);
            result.put("failed", objectNames.size() - successCount);
            
            return ResponseResult.success(result);
            
        } catch (Exception e) {
            return ResponseResult.fail("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件信息
     */
    @GetMapping("/info")
    public ResponseResult<Map<String, Object>> getFileInfo(@RequestParam("objectName") String objectName) {
        try {
            Map<String, Object> fileInfo = minioStorageService.getFileInfo(objectName);
            return ResponseResult.success(fileInfo);
        } catch (Exception e) {
            return ResponseResult.fail("获取文件信息失败: " + e.getMessage());
        }
    }

    /**
     * 检查文件是否存在
     */
    @GetMapping("/exists")
    public ResponseResult<Boolean> fileExists(@RequestParam("objectName") String objectName) {
        try {
            boolean exists = minioStorageService.fileExists(objectName);
            return ResponseResult.success(exists);
        } catch (Exception e) {
            return ResponseResult.fail("检查文件存在性失败: " + e.getMessage());
        }
    }

    /**
     * 获取预签名上传URL
     */
    @GetMapping("/presigned-upload-url")
    public ResponseResult<String> getPresignedUploadUrl(
            @RequestParam("objectName") String objectName,
            @RequestParam(value = "expirationMinutes", defaultValue = "60") int expirationMinutes) {
        
        try {
            String url = minioStorageService.getPresignedUploadUrl(objectName, expirationMinutes);
            return ResponseResult.success(url);
        } catch (Exception e) {
            return ResponseResult.fail("获取预签名上传URL失败: " + e.getMessage());
        }
    }

    /**
     * 获取预签名下载URL
     */
    @GetMapping("/presigned-download-url")
    public ResponseResult<String> getPresignedDownloadUrl(
            @RequestParam("objectName") String objectName,
            @RequestParam(value = "expirationMinutes", defaultValue = "60") int expirationMinutes) {
        
        try {
            String url = minioStorageService.getPresignedDownloadUrl(objectName, expirationMinutes);
            return ResponseResult.success(url);
        } catch (Exception e) {
            return ResponseResult.fail("获取预签名下载URL失败: " + e.getMessage());
        }
    }

    /**
     * 复制文件
     */
    @PostMapping("/copy")
    public ResponseResult<Boolean> copyFile(
            @RequestParam("sourceObjectName") String sourceObjectName,
            @RequestParam("targetObjectName") String targetObjectName) {
        
        try {
            boolean result = minioStorageService.copyFile(sourceObjectName, targetObjectName);
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.fail("文件复制失败: " + e.getMessage());
        }
    }

    /**
     * 移动文件
     */
    @PostMapping("/move")
    public ResponseResult<Boolean> moveFile(
            @RequestParam("sourceObjectName") String sourceObjectName,
            @RequestParam("targetObjectName") String targetObjectName) {
        
        try {
            boolean result = minioStorageService.moveFile(sourceObjectName, targetObjectName);
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.fail("文件移动失败: " + e.getMessage());
        }
    }

    /**
     * 生成对象名称
     */
    private String generateObjectName(String fileName, String path) {
        if (fileName == null) {
            fileName = "file_" + System.currentTimeMillis();
        }
        
        String timestamp = String.valueOf(System.currentTimeMillis());
        String objectName = path.isEmpty() ? "uploads/" + timestamp + "/" + fileName 
                                        : path + "/" + timestamp + "/" + fileName;
        
        return objectName;
    }
}
