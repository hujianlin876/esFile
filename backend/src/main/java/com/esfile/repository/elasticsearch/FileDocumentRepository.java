package com.esfile.repository.elasticsearch;

import com.esfile.entity.elasticsearch.FileDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 文件文档ES仓库
 * 提供文件搜索和索引功能
 * 
 * @author esfile
 * @since 1.0.0
 */
@Repository
public interface FileDocumentRepository extends ElasticsearchRepository<FileDocument, String> {

    /**
     * 根据文件名搜索文件
     */
    List<FileDocument> findByFileNameContaining(String fileName);

    /**
     * 根据文件类型搜索文件
     */
    List<FileDocument> findByFileType(String fileType);

    /**
     * 根据上传用户搜索文件
     */
    List<FileDocument> findByUploadUserNameContaining(String uploadUserName);

    /**
     * 根据标签搜索文件
     */
    List<FileDocument> findByTagsContaining(String tags);

    /**
     * 根据描述搜索文件
     */
    List<FileDocument> findByDescriptionContaining(String description);

    /**
     * 根据文件大小范围搜索文件
     */
    List<FileDocument> findByFileSizeBetween(Long minSize, Long maxSize);

    /**
     * 根据创建时间范围搜索文件
     */
    List<FileDocument> findByCreateTimeBetween(Date startTime, Date endTime);

    /**
     * 根据父文件夹ID搜索文件
     */
    List<FileDocument> findByParentFolderId(Long parentFolderId);

    /**
     * 根据上传用户ID搜索文件
     */
    List<FileDocument> findByUploadUserId(Long uploadUserId);

    /**
     * 根据状态搜索文件
     */
    List<FileDocument> findByStatus(Integer status);

    /**
     * 根据是否公开搜索文件
     */
    List<FileDocument> findByIsPublic(Integer isPublic);

    /**
     * 根据分类搜索文件
     */
    List<FileDocument> findByCategory(String category);

    /**
     * 根据优先级搜索文件
     */
    List<FileDocument> findByPriority(Integer priority);

    /**
     * 复合搜索：文件名、描述、标签、内容
     */
    @Query("{\"bool\": {\"should\": [{\"match\": {\"fileName\": \"?0\"}}, {\"match\": {\"description\": \"?0\"}}, {\"match\": {\"tags\": \"?0\"}}, {\"match\": {\"content\": \"?0\"}}]}}")
    Page<FileDocument> searchByKeyword(String keyword, Pageable pageable);

    /**
     * 高级搜索：多条件组合
     */
    @Query("{\"bool\": {\"must\": [{\"match\": {\"fileName\": \"?0\"}}, {\"match\": {\"fileType\": \"?1\"}}, {\"range\": {\"fileSize\": {\"gte\": ?2, \"lte\": ?3}}}, {\"range\": {\"createTime\": {\"gte\": \"?4\", \"lte\": \"?5\"}}}]}}")
    Page<FileDocument> advancedSearch(String fileName, String fileType, Long minSize, Long maxSize, 
                                     String startTime, String endTime, Pageable pageable);

    /**
     * 全文搜索：在所有文本字段中搜索
     */
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"fileName^2\", \"description^1.5\", \"tags^1.2\", \"content^1\", \"uploadUserName^0.8\"]}}")
    Page<FileDocument> fullTextSearch(String query, Pageable pageable);

    /**
     * 根据文件类型统计
     */
    @Query("{\"aggs\": {\"file_types\": {\"terms\": {\"field\": \"fileType\"}}}}")
    List<FileDocument> getFileTypeStats();

    /**
     * 根据上传用户统计
     */
    @Query("{\"aggs\": {\"upload_users\": {\"terms\": {\"field\": \"uploadUserName\"}}}}")
    List<FileDocument> getUploadUserStats();

    /**
     * 根据标签统计
     */
    @Query("{\"aggs\": {\"tags\": {\"terms\": {\"field\": \"tags\"}}}}")
    List<FileDocument> getTagStats();

    /**
     * 根据创建时间统计（按天）
     */
    @Query("{\"aggs\": {\"daily_uploads\": {\"date_histogram\": {\"field\": \"createTime\", \"interval\": \"1d\"}}}}")
    List<FileDocument> getDailyUploadStats();

    /**
     * 根据文件大小统计
     */
    @Query("{\"aggs\": {\"size_ranges\": {\"range\": {\"field\": \"fileSize\", \"ranges\": [{\"to\": 1048576}, {\"from\": 1048576, \"to\": 10485760}, {\"from\": 10485760}]}}}}")
    List<FileDocument> getSizeRangeStats();

    /**
     * 搜索建议（自动补全）
     */
    @Query("{\"suggest\": {\"file_suggest\": {\"prefix\": \"?0\", \"completion\": {\"field\": \"fileName_suggest\"}}}}")
    List<FileDocument> getSearchSuggestions(String prefix);

    /**
     * 相似文件搜索（基于标签和描述）
     */
    @Query("{\"more_like_this\": {\"fields\": [\"tags\", \"description\"], \"like\": [{\"_index\": \"file_documents\", \"_id\": \"?0\"}]}}")
    Page<FileDocument> findSimilarFiles(String fileId, Pageable pageable);

    /**
     * 热门文件搜索（基于下载和预览次数）
     */
    @Query("{\"sort\": [{\"downloadCount\": {\"order\": \"desc\"}}, {\"previewCount\": {\"order\": \"desc\"}}]}")
    Page<FileDocument> findPopularFiles(Pageable pageable);

    /**
     * 最近上传文件
     */
    @Query("{\"sort\": [{\"createTime\": {\"order\": \"desc\"}}]}")
    Page<FileDocument> findRecentFiles(Pageable pageable);

    /**
     * 根据MD5查找重复文件
     */
    List<FileDocument> findByFileMd5(String fileMd5);

    /**
     * 根据文件扩展名搜索
     */
    List<FileDocument> findByFileExtension(String fileExtension);

    /**
     * 根据内容类型搜索（图片、文档、视频等）
     */
    @Query("{\"bool\": {\"should\": [{\"prefix\": {\"fileType\": \"image/\"}}, {\"prefix\": {\"fileType\": \"video/\"}}, {\"prefix\": {\"fileType\": \"audio/\"}}, {\"prefix\": {\"fileType\": \"text/\"}}, {\"prefix\": {\"fileType\": \"application/\"}}]}}")
    List<FileDocument> findByContentType(String contentType);
}
