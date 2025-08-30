package com.esfile.common.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * MinIO存储工具类
 * 提供文件处理相关的工具方法
 * 
 * @author esfile
 * @since 1.0.0
 */
public class MinioStorageUtil {

    // 允许的文件类型
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
        "jpg", "jpeg", "png", "gif", "bmp", "webp",
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
        "txt", "md", "json", "xml", "html", "css", "js",
        "zip", "rar", "7z", "tar", "gz",
        "mp4", "avi", "mov", "wmv", "flv", "mkv",
        "mp3", "wav", "flac", "aac", "ogg"
    ));

    // 图片文件类型
    private static final Set<String> IMAGE_EXTENSIONS = new HashSet<>(Arrays.asList(
        "jpg", "jpeg", "png", "gif", "bmp", "webp", "svg", "ico"
    ));

    // 文档文件类型
    private static final Set<String> DOCUMENT_EXTENSIONS = new HashSet<>(Arrays.asList(
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
        "txt", "md", "json", "xml", "html", "css", "js"
    ));

    // 视频文件类型
    private static final Set<String> VIDEO_EXTENSIONS = new HashSet<>(Arrays.asList(
        "mp4", "avi", "mov", "wmv", "flv", "mkv", "webm", "m4v"
    ));

    // 音频文件类型
    private static final Set<String> AUDIO_EXTENSIONS = new HashSet<>(Arrays.asList(
        "mp3", "wav", "flac", "aac", "ogg", "wma", "m4a"
    ));

    // 压缩文件类型
    private static final Set<String> ARCHIVE_EXTENSIONS = new HashSet<>(Arrays.asList(
        "zip", "rar", "7z", "tar", "gz", "bz2", "xz"
    ));

    /**
     * 生成文件存储路径
     * 
     * @param userId 用户ID
     * @param fileName 文件名
     * @return 存储路径
     */
    public static String generateStoragePath(Long userId, String fileName) {
        LocalDateTime now = LocalDateTime.now();
        String datePath = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String userIdStr = userId != null ? userId.toString() : "anonymous";
        
        return String.format("files/%s/%s/%s", userIdStr, datePath, fileName);
    }

    /**
     * 生成带时间戳的文件名
     * 
     * @param originalFileName 原始文件名
     * @return 新文件名
     */
    public static String generateFileNameWithTimestamp(String originalFileName) {
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            return "file_" + System.currentTimeMillis();
        }
        
        String extension = getFileExtension(originalFileName);
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        return String.format("%s_%s.%s", baseName, timestamp, extension);
    }

    /**
     * 生成唯一文件名
     * 
     * @param originalFileName 原始文件名
     * @return 唯一文件名
     */
    public static String generateUniqueFileName(String originalFileName) {
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            return UUID.randomUUID().toString();
        }
        
        String extension = getFileExtension(originalFileName);
        String uuid = UUID.randomUUID().toString();
        
        return String.format("%s.%s", uuid, extension);
    }

    /**
     * 获取文件扩展名
     * 
     * @param fileName 文件名
     * @return 文件扩展名
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 获取文件名（不含扩展名）
     * 
     * @param fileName 文件名
     * @return 不含扩展名的文件名
     */
    public static String getFileNameWithoutExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return fileName;
        }
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * 验证文件类型是否允许
     * 
     * @param fileName 文件名
     * @return 是否允许
     */
    public static boolean isAllowedFileType(String fileName) {
        String extension = getFileExtension(fileName);
        return ALLOWED_EXTENSIONS.contains(extension);
    }

    /**
     * 判断是否为图片文件
     * 
     * @param fileName 文件名
     * @return 是否为图片
     */
    public static boolean isImageFile(String fileName) {
        String extension = getFileExtension(fileName);
        return IMAGE_EXTENSIONS.contains(extension);
    }

    /**
     * 判断是否为文档文件
     * 
     * @param fileName 文件名
     * @return 是否为文档
     */
    public static boolean isDocumentFile(String fileName) {
        String extension = getFileExtension(fileName);
        return DOCUMENT_EXTENSIONS.contains(extension);
    }

    /**
     * 判断是否为视频文件
     * 
     * @param fileName 文件名
     * @return 是否为视频
     */
    public static boolean isVideoFile(String fileName) {
        String extension = getFileExtension(fileName);
        return VIDEO_EXTENSIONS.contains(extension);
    }

    /**
     * 判断是否为音频文件
     * 
     * @param fileName 文件名
     * @return 是否为音频
     */
    public static boolean isAudioFile(String fileName) {
        String extension = getFileExtension(fileName);
        return AUDIO_EXTENSIONS.contains(extension);
    }

    /**
     * 判断是否为压缩文件
     * 
     * @param fileName 文件名
     * @return 是否为压缩文件
     */
    public static boolean isArchiveFile(String fileName) {
        String extension = getFileExtension(fileName);
        return ARCHIVE_EXTENSIONS.contains(extension);
    }

    /**
     * 获取文件MIME类型
     * 
     * @param fileName 文件名
     * @return MIME类型
     */
    public static String getMimeType(String fileName) {
        String extension = getFileExtension(fileName);
        
        switch (extension) {
            // 图片
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "bmp":
                return "image/bmp";
            case "webp":
                return "image/webp";
            case "svg":
                return "image/svg+xml";
            case "ico":
                return "image/x-icon";
                
            // 文档
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ppt":
                return "application/vnd.ms-powerpoint";
            case "pptx":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "txt":
                return "text/plain";
            case "md":
                return "text/markdown";
            case "json":
                return "application/json";
            case "xml":
                return "application/xml";
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "js":
                return "application/javascript";
                
            // 视频
            case "mp4":
                return "video/mp4";
            case "avi":
                return "video/x-msvideo";
            case "mov":
                return "video/quicktime";
            case "wmv":
                return "video/x-ms-wmv";
            case "flv":
                return "video/x-flv";
            case "mkv":
                return "video/x-matroska";
            case "webm":
                return "video/webm";
                
            // 音频
            case "mp3":
                return "audio/mpeg";
            case "wav":
                return "audio/wav";
            case "flac":
                return "audio/flac";
            case "aac":
                return "audio/aac";
            case "ogg":
                return "audio/ogg";
                
            // 压缩
            case "zip":
                return "application/zip";
            case "rar":
                return "application/vnd.rar";
            case "7z":
                return "application/x-7z-compressed";
            case "tar":
                return "application/x-tar";
            case "gz":
                return "application/gzip";
                
            default:
                return "application/octet-stream";
        }
    }

    /**
     * 计算文件MD5值
     * 
     * @param file 文件
     * @return MD5值
     */
    public static String calculateFileMd5(MultipartFile file) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(file.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("计算文件MD5失败", e);
        }
    }

    /**
     * 计算文件MD5值
     * 
     * @param filePath 文件路径
     * @return MD5值
     */
    public static String calculateFileMd5(String filePath) {
        try {
            Path path = Paths.get(filePath);
            byte[] fileBytes = Files.readAllBytes(path);
            
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(fileBytes);
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("计算文件MD5失败", e);
        }
    }

    /**
     * 格式化文件大小
     * 
     * @param size 文件大小（字节）
     * @return 格式化后的文件大小
     */
    public static String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }

    /**
     * 创建临时目录
     * 
     * @param baseDir 基础目录
     * @return 临时目录路径
     */
    public static String createTempDirectory(String baseDir) {
        try {
            Path tempDir = Files.createTempDirectory(Paths.get(baseDir), "esfile_");
            return tempDir.toString();
        } catch (IOException e) {
            throw new RuntimeException("创建临时目录失败", e);
        }
    }

    /**
     * 清理临时文件
     * 
     * @param tempDir 临时目录
     */
    public static void cleanupTempFiles(String tempDir) {
        try {
            Path path = Paths.get(tempDir);
            if (Files.exists(path)) {
                Files.walk(path)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(filePath -> {
                        try {
                            Files.delete(filePath);
                        } catch (IOException e) {
                            // 忽略删除失败的文件
                        }
                    });
            }
        } catch (IOException e) {
            // 忽略清理失败
        }
    }

    /**
     * 验证文件大小
     * 
     * @param fileSize 文件大小
     * @param maxSize 最大允许大小
     * @return 是否有效
     */
    public static boolean isValidFileSize(long fileSize, long maxSize) {
        return fileSize > 0 && fileSize <= maxSize;
    }

    /**
     * 生成安全的文件名
     * 
     * @param originalFileName 原始文件名
     * @return 安全的文件名
     */
    public static String generateSafeFileName(String originalFileName) {
        if (originalFileName == null) {
            return UUID.randomUUID().toString();
        }
        
        // 移除特殊字符
        String safeName = originalFileName.replaceAll("[^a-zA-Z0-9._-]", "_");
        
        // 确保文件名不为空
        if (safeName.trim().isEmpty()) {
            safeName = "file_" + System.currentTimeMillis();
        }
        
        return safeName;
    }
}
