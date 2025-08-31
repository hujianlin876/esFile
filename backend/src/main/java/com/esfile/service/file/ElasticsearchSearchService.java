package com.esfile.service.file;

import com.esfile.entity.dto.FileSearchDto;
import com.esfile.entity.elasticsearch.FileDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Elasticsearch搜索服务接口
 * 提供基于ES的文件搜索功能
 * 
 * @author esfile
 * @since 1.0.0
 */
public interface ElasticsearchSearchService {

    /**
     * 全文搜索
     */
    Page<FileDocument> fullTextSearch(String query, Pageable pageable);

    /**
     * 关键词搜索
     */
    Page<FileDocument> keywordSearch(String keyword, Pageable pageable);

    /**
     * 高级搜索
     */
    Page<FileDocument> advancedSearch(FileSearchDto searchDto, Pageable pageable);

    /**
     * 根据文件类型搜索
     */
    List<FileDocument> searchByFileType(String fileType);

    /**
     * 根据标签搜索
     */
    List<FileDocument> searchByTags(String tags);

    /**
     * 根据上传用户搜索
     */
    List<FileDocument> searchByUploadUser(String uploadUserName);

    /**
     * 根据文件大小范围搜索
     */
    List<FileDocument> searchByFileSizeRange(Long minSize, Long maxSize);

    /**
     * 根据时间范围搜索
     */
    List<FileDocument> searchByTimeRange(String startTime, String endTime);

    /**
     * 搜索建议（自动补全）
     */
    List<String> getSearchSuggestions(String prefix);

    /**
     * 相似文件搜索
     */
    List<FileDocument> findSimilarFiles(String fileId, Pageable pageable);

    /**
     * 热门文件搜索
     */
    Page<FileDocument> findPopularFiles(Pageable pageable);

    /**
     * 最近上传文件
     */
    Page<FileDocument> findRecentFiles(Pageable pageable);

    /**
     * 文件类型统计
     */
    Map<String, Object> getFileTypeStats();

    /**
     * 上传用户统计
     */
    Map<String, Object> getUploadUserStats();

    /**
     * 标签统计
     */
    Map<String, Object> getTagStats();

    /**
     * 每日上传统计
     */
    Map<String, Object> getDailyUploadStats();

    /**
     * 文件大小分布统计
     */
    Map<String, Object> getSizeRangeStats();

    /**
     * 索引文件到ES
     */
    boolean indexFile(FileDocument fileDocument);

    /**
     * 批量索引文件到ES
     */
    boolean batchIndexFiles(List<FileDocument> fileDocuments);

    /**
     * 从ES删除文件索引
     */
    boolean deleteFileIndex(String fileId);

    /**
     * 更新ES中的文件索引
     */
    boolean updateFileIndex(FileDocument fileDocument);

    /**
     * 重建索引
     */
    boolean rebuildIndex();

    /**
     * 搜索历史记录
     */
    List<Map<String, Object>> getSearchHistory(Long userId);

    /**
     * 保存搜索历史
     */
    boolean saveSearchHistory(Long userId, String query, Map<String, Object> filters);

    /**
     * 热门搜索词
     */
    List<String> getHotSearchTerms();

    /**
     * 相关搜索词推荐
     */
    List<String> getRelatedSearchTerms(String query);
}
