package com.esfile.entity.dto;
import org.springframework.web.multipart.MultipartFile;
/**
 * 文件上传DTO
 */
public class FileUploadDto {
    private MultipartFile file;
    private String description;
    private String tags;
    private Integer isPublic;
    // getter/setter
}
