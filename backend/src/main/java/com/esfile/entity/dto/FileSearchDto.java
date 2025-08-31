package com.esfile.entity.dto;

/**
 * 文件搜索数据传输对象
 */
public class FileSearchDto {
    
    /**
     * 搜索关键词
     */
    private String keyword;
    
    /**
     * 文件类型
     */
    private String fileType;
    
    /**
     * 文件大小范围（最小值，单位：字节）
     */
    private Long minSize;
    
    /**
     * 文件大小范围（最大值，单位：字节）
     */
    private Long maxSize;
    
    /**
     * 上传用户ID
     */
    private Long uploadUserId;
    
    /**
     * 上传用户名
     */
    private String uploadUserName;
    
    /**
     * 创建时间范围（开始时间）
     */
    private String startTime;
    
    /**
     * 创建时间范围（结束时间）
     */
    private String endTime;
    
    /**
     * 文件状态：0-删除，1-正常
     */
    private Integer status;
    
    /**
     * 是否公开：0-私有，1-公开
     */
    private Integer isPublic;
    
    /**
     * 标签
     */
    private String tags;
    
    /**
     * 文件分类
     */
    private String category;
    
    /**
     * 优先级：1-低，2-中，3-高
     */
    private Integer priority;
    
    /**
     * 排序字段
     */
    private String orderBy;
    
    /**
     * 排序方向：asc-升序，desc-降序
     */
    private String orderDirection;
    
    /**
     * 页码
     */
    private Integer page;
    
    /**
     * 每页大小
     */
    private Integer size;
    
    /**
     * 父级文件夹ID
     */
    private Long parentFolderId;
    
    /**
     * 是否包含子文件夹
     */
    private Boolean includeSubFolders;

    // Getters and Setters
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getMinSize() {
        return minSize;
    }

    public void setMinSize(Long minSize) {
        this.minSize = minSize;
    }

    public Long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(Long parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public Boolean getIncludeSubFolders() {
        return includeSubFolders;
    }

    public void setIncludeSubFolders(Boolean includeSubFolders) {
        this.includeSubFolders = includeSubFolders;
    }

    // 添加缺失的方法
    public void setDateRange(String[] dateRange) {
        if (dateRange != null && dateRange.length >= 2) {
            this.startTime = dateRange[0];
            this.endTime = dateRange[1];
        }
    }

    public void setSizeRange(Long[] sizeRange) {
        if (sizeRange != null && sizeRange.length >= 2) {
            this.minSize = sizeRange[0];
            this.maxSize = sizeRange[1];
        }
    }

    public void setUploader(String uploader) {
        this.uploadUserName = uploader;
    }

    public void setSortBy(String sortBy) {
        this.orderBy = sortBy;
    }

    public void setSortOrder(String sortOrder) {
        this.orderDirection = sortOrder;
    }
}
