package com.esfile.entity.elasticsearch;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


import java.util.Date;

/**
 * 文件文档ES实体类
 * 用于Elasticsearch索引和搜索
 * 
 * @author esfile
 * @since 1.0.0
 */
@Document(indexName = "file_documents")
public class FileDocument {

    @Id
    private String id;

    @Field(type = FieldType.Long)
    private Long fileId;

    @Field(type = FieldType.Text)
    private String fileName;

    @Field(type = FieldType.Text)
    private String originalFileName;

    @Field(type = FieldType.Text)
    private String filePath;

    @Field(type = FieldType.Text)
    private String fileUrl;

    @Field(type = FieldType.Long)
    private Long fileSize;

    @Field(type = FieldType.Keyword)
    private String fileType;

    @Field(type = FieldType.Keyword)
    private String fileExtension;

    @Field(type = FieldType.Keyword)
    private String fileMd5;

    @Field(type = FieldType.Long)
    private Long parentFolderId;

    @Field(type = FieldType.Long)
    private Long uploadUserId;

    @Field(type = FieldType.Text)
    private String uploadUserName;

    @Field(type = FieldType.Integer)
    private Integer status;

    @Field(type = FieldType.Integer)
    private Integer isPublic;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private String tags;

    @Field(type = FieldType.Integer)
    private Integer downloadCount;

    @Field(type = FieldType.Integer)
    private Integer previewCount;

    @Field(type = FieldType.Keyword)
    private String bucketName;

    @Field(type = FieldType.Keyword)
    private String objectName;

    @Field(type = FieldType.Date)
    private Date createTime;

    @Field(type = FieldType.Date)
    private Date updateTime;

    @Field(type = FieldType.Text)
    private String content; // 文件内容（用于全文搜索）

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Integer)
    private Integer priority;

    // 构造函数
    public FileDocument() {}

    public FileDocument(Long fileId, String fileName, String originalFileName, String filePath, 
                       String fileUrl, Long fileSize, String fileType, String fileExtension, 
                       String fileMd5, Long parentFolderId, Long uploadUserId, String uploadUserName, 
                       Integer status, Integer isPublic, String description, String tags, 
                       Integer downloadCount, Integer previewCount, String bucketName, 
                       String objectName, Date createTime, Date updateTime, String content, 
                       String category, Integer priority) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.filePath = filePath;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.fileExtension = fileExtension;
        this.fileMd5 = fileMd5;
        this.parentFolderId = parentFolderId;
        this.uploadUserId = uploadUserId;
        this.uploadUserName = uploadUserName;
        this.status = status;
        this.isPublic = isPublic;
        this.description = description;
        this.tags = tags;
        this.downloadCount = downloadCount;
        this.previewCount = previewCount;
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.content = content;
        this.category = category;
        this.priority = priority;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public Long getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(Long parentFolderId) {
        this.parentFolderId = parentFolderId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
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

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Integer getPreviewCount() {
        return previewCount;
    }

    public void setPreviewCount(Integer previewCount) {
        this.previewCount = previewCount;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Override
    public String toString() {
        return "FileDocument{" +
                "id='" + id + '\'' +
                ", fileId=" + fileId +
                ", fileName='" + fileName + '\'' +
                ", originalFileName='" + originalFileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", fileSize=" + fileSize +
                ", fileType='" + fileType + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                ", fileMd5='" + fileMd5 + '\'' +
                ", parentFolderId=" + parentFolderId +
                ", uploadUserId=" + uploadUserId +
                ", uploadUserName='" + uploadUserName + '\'' +
                ", status=" + status +
                ", isPublic=" + isPublic +
                ", description='" + description + '\'' +
                ", tags='" + tags + '\'' +
                ", downloadCount=" + downloadCount +
                ", previewCount=" + previewCount +
                ", bucketName='" + bucketName + '\'' +
                ", objectName='" + objectName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", content='" + content + '\'' +
                ", category='" + category + '\'' +
                ", priority=" + priority +
                '}';
    }
}
