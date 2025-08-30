package com.esfile.service.file;

import com.esfile.common.vo.PageResult;
import com.esfile.entity.mybatis.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 文件服务接口
 * 
 * @author esfile
 * @since 2024-01-01
 */
public interface FileService {
    
    /**
     * 上传文件
     * 
     * @param file 文件
     * @param userId 上传用户ID
     * @param description 文件描述
     * @param tags 标签
     * @param isPublic 是否公开
     * @return 文件信息
     */
    FileInfo uploadFile(MultipartFile file, Long userId, String description, String tags, Integer isPublic);
    
    /**
     * 批量上传文件
     * 
     * @param files 文件列表
     * @param userId 上传用户ID
     * @param description 文件描述
     * @param tags 标签
     * @param isPublic 是否公开
     * @return 文件信息列表
     */
    List<FileInfo> batchUploadFiles(List<MultipartFile> files, Long userId, String description, String tags, Integer isPublic);
    
    /**
     * 下载文件
     * 
     * @param fileId 文件ID
     * @param response HTTP响应
     * @throws IOException IO异常
     */
    void downloadFile(Long fileId, HttpServletResponse response) throws IOException;
    
    /**
     * 删除文件
     * 
     * @param fileId 文件ID
     * @param userId 操作用户ID
     * @return 是否成功
     */
    boolean deleteFile(Long fileId, Long userId);
    
    /**
     * 根据ID查询文件信息
     * 
     * @param fileId 文件ID
     * @return 文件信息
     */
    FileInfo getFileById(Long fileId);
    
    /**
     * 分页查询文件
     * 
     * @param page 页码
     * @param size 每页大小
     * @param fileName 文件名
     * @param fileType 文件类型
     * @param uploadUserId 上传用户ID
     * @return 分页结果
     */
    PageResult<FileInfo> getFilePage(int page, int size, String fileName, String fileType, Long uploadUserId);
    
    /**
     * 根据条件查询文件
     * 
     * @param fileInfo 查询条件
     * @return 文件列表
     */
    List<FileInfo> getFilesByCondition(FileInfo fileInfo);
    
    /**
     * 更新文件信息
     * 
     * @param fileInfo 文件信息
     * @return 是否成功
     */
    boolean updateFileInfo(FileInfo fileInfo);
    
    /**
     * 更新下载次数
     * 
     * @param fileId 文件ID
     */
    void updateDownloadCount(Long fileId);
    
    /**
     * 更新预览次数
     * 
     * @param fileId 文件ID
     */
    void updatePreviewCount(Long fileId);
    
    /**
     * 检查文件是否存在
     * 
     * @param fileMd5 文件MD5值
     * @return 是否存在
     */
    boolean isFileExists(String fileMd5);
    
    /**
     * 获取文件预览URL
     * 
     * @param fileId 文件ID
     * @return 预览URL
     */
    String getFilePreviewUrl(Long fileId);
    
    /**
     * 获取文件下载URL
     * 
     * @param fileId 文件ID
     * @return 下载URL
     */
    String getFileDownloadUrl(Long fileId);
}
