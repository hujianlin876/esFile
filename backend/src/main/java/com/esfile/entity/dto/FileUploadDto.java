package com.esfile.entity.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传数据传输对象
 */
public class FileUploadDto {
    
    /**
     * 上传的文件
     */
    private MultipartFile file;
    
    /**
     * 父级文件夹ID
     */
    private Long parentFolderId;
    
    /**
     * 文件描述
     */
    private String description;
    
    /**
     * 标签
     */
    private String tags;
    
    /**
     * 是否公开：0-私有，1-公开
     */
    private Integer isPublic;
    
    /**
     * 上传用户ID
     */
    private Long uploadUserId;
    
    /**
     * 上传用户名
     */
    private String uploadUserName;
    
    /**
     * 文件分类
     */
    private String category;
    
    /**
     * 优先级：1-低，2-中，3-高
     */
    private Integer priority;
    
    /**
     * 过期时间
     */
    private String expireTime;
    
    /**
     * 是否加密：0-否，1-是
     */
    private Integer isEncrypted;
    
    /**
     * 加密密码
     */
    private String encryptPassword;

    // Getters and Setters
    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Long getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(Long parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public Long getUploadUserId() {
        return uploadUserId;
    }

    public void setUploadUserId(Long uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    public String getUploadUserName() {
        return uploadUserName;
    }

    public void setUploadUserName(String uploadUserName) {
        this.uploadUserName = uploadUserName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getIsEncrypted() {
        return isEncrypted;
    }

    public void setIsEncrypted(Integer isEncrypted) {
        this.isEncrypted = isEncrypted;
    }

    public String getEncryptPassword() {
        return encryptPassword;
    }

    public void setEncryptPassword(String encryptPassword) {
        this.encryptPassword = encryptPassword;
    }

    // 添加缺失的方法
    public void setUserId(Long userId) {
        this.uploadUserId = userId;
    }
}
