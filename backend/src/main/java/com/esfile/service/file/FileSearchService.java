package com.esfile.service.file;

import com.esfile.entity.dto.SearchDto;
import com.esfile.entity.mybatis.FileInfo;

import java.util.List;
import java.util.Map;

/**
 * 文件搜索服务接口
 */
public interface FileSearchService {

    /**
     * 搜索文件（兼容性方法）
     */
    String search(SearchDto searchDto);

    /**
     * 搜索文件
     */
    Map<String, Object> searchFiles(SearchDto searchDto);

    /**
     * 高级搜索
     */
    Map<String, Object> advancedSearch(SearchDto searchDto);

    /**
     * 全文搜索
     */
    Map<String, Object> fullTextSearch(String keyword, Integer page, Integer size);

    /**
     * 按标签搜索
     */
    List<FileInfo> searchByTags(List<String> tags, Integer page, Integer size);

    /**
     * 按类型搜索
     */
    List<FileInfo> searchByType(String fileType, Integer page, Integer size);

    /**
     * 按大小范围搜索
     */
    List<FileInfo> searchBySizeRange(Long minSize, Long maxSize, Integer page, Integer size);

    /**
     * 按时间范围搜索
     */
    List<FileInfo> searchByTimeRange(String startTime, String endTime, Integer page, Integer size);

    /**
     * 按用户搜索
     */
    List<FileInfo> searchByUser(Long userId, Integer page, Integer size);

    /**
     * 获取搜索建议
     */
    List<String> getSearchSuggestions(String keyword);

    /**
     * 获取搜索历史
     */
    List<String> getSearchHistory(Long userId);

    /**
     * 获取热门搜索
     */
    List<String> getHotSearches();

    /**
     * 记录搜索日志
     */
    void logSearch(String keyword, Long userId, String ipAddress, String userAgent);

    /**
     * 更新搜索统计
     */
    void updateSearchStats(String keyword);

    /**
     * 获取搜索统计
     */
    Map<String, Object> getSearchStats();

    /**
     * 清理搜索缓存
     */
    boolean cleanupSearchCache();

    /**
     * 重建搜索索引
     */
    boolean rebuildSearchIndex();
}
