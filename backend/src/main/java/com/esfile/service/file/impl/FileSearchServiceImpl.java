package com.esfile.service.file.impl;

import com.esfile.entity.dto.SearchDto;
import com.esfile.entity.mybatis.FileInfo;
import com.esfile.mapper.FileInfoMapper;
import com.esfile.service.file.FileSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件搜索服务实现类
 * 专门处理文件搜索相关功能
 */
@Service
public class FileSearchServiceImpl implements FileSearchService {

    @Autowired
    private FileInfoMapper fileInfoMapper;

    // 搜索历史缓存
    private final Map<Long, List<String>> searchHistoryCache = new ConcurrentHashMap<>();
    
    // 热门搜索缓存
    private final List<String> hotSearchesCache = new ArrayList<>();
    
    // 搜索统计缓存
    private final Map<String, Integer> searchStatsCache = new ConcurrentHashMap<>();

    /**
     * 搜索文件（兼容性方法）
     */
    @Override
    public String search(SearchDto searchDto) {
        Map<String, Object> result = searchFiles(searchDto);
        return "搜索完成，找到 " + result.get("total") + " 个文件";
    }

    /**
     * 搜索文件
     */
    @Override
    public Map<String, Object> searchFiles(SearchDto searchDto) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 构建搜索条件
            FileInfo searchCondition = buildSearchCondition(searchDto);
            
            // 执行搜索
            List<FileInfo> files = fileInfoMapper.selectByCondition(searchCondition);
            
            // 分页处理
            int page = searchDto.getPage() != null ? searchDto.getPage() : 1;
            int size = searchDto.getSize() != null ? searchDto.getSize() : 20;
            int total = files.size();
            int start = (page - 1) * size;
            int end = Math.min(start + size, total);
            
            List<FileInfo> pagedFiles = files.subList(start, end);
            
            // 记录搜索日志
            if (searchDto.getKeyword() != null && !searchDto.getKeyword().trim().isEmpty()) {
                logSearch(searchDto.getKeyword(), null, null, null);
                updateSearchStats(searchDto.getKeyword());
            }
            
            result.put("list", pagedFiles);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            result.put("pages", (total + size - 1) / size);
            
        } catch (Exception e) {
            result.put("error", "搜索失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 高级搜索
     */
    @Override
    public Map<String, Object> advancedSearch(SearchDto searchDto) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 构建高级搜索条件
            Map<String, Object> conditions = new HashMap<>();
            
            if (searchDto.getKeyword() != null && !searchDto.getKeyword().trim().isEmpty()) {
                conditions.put("keyword", searchDto.getKeyword());
            }
            
            if (searchDto.getFileType() != null && !searchDto.getFileType().trim().isEmpty()) {
                conditions.put("fileType", searchDto.getFileType());
            }
            
            if (searchDto.getStartTime() != null && searchDto.getEndTime() != null) {
                conditions.put("startTime", searchDto.getStartTime());
                conditions.put("endTime", searchDto.getEndTime());
            }
            
            // TODO: 实现高级搜索逻辑
            result.put("message", "高级搜索功能暂未实现");
            
        } catch (Exception e) {
            result.put("error", "高级搜索失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 全文搜索
     */
    @Override
    public Map<String, Object> fullTextSearch(String keyword, Integer page, Integer size) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 使用关键词搜索
            List<FileInfo> files = fileInfoMapper.searchByKeyword(keyword);
            
            // 分页处理
            if (page == null) page = 1;
            if (size == null) size = 20;
            
            int total = files.size();
            int start = (page - 1) * size;
            int end = Math.min(start + size, total);
            
            List<FileInfo> pagedFiles = files.subList(start, end);
            
            result.put("list", pagedFiles);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            result.put("pages", (total + size - 1) / size);
            
        } catch (Exception e) {
            result.put("error", "全文搜索失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 按标签搜索
     */
    @Override
    public List<FileInfo> searchByTags(List<String> tags, Integer page, Integer size) {
        if (tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<FileInfo> result = new ArrayList<>();
        
        for (String tag : tags) {
            List<FileInfo> files = fileInfoMapper.selectByTag(tag);
            result.addAll(files);
        }
        
        // 去重
        Set<Long> fileIds = new HashSet<>();
        List<FileInfo> uniqueFiles = new ArrayList<>();
        for (FileInfo file : result) {
            if (!fileIds.contains(file.getId())) {
                fileIds.add(file.getId());
                uniqueFiles.add(file);
            }
        }
        
        // 分页处理
        if (page != null && size != null) {
            int start = (page - 1) * size;
            int end = Math.min(start + size, uniqueFiles.size());
            if (start < uniqueFiles.size()) {
                return uniqueFiles.subList(start, end);
            }
        }
        
        return uniqueFiles;
    }

    /**
     * 按类型搜索
     */
    @Override
    public List<FileInfo> searchByType(String fileType, Integer page, Integer size) {
        List<FileInfo> files = fileInfoMapper.selectByFileType(fileType);
        
        // 分页处理
        if (page != null && size != null) {
            int start = (page - 1) * size;
            int end = Math.min(start + size, files.size());
            if (start < files.size()) {
                return files.subList(start, end);
            }
        }
        
        return files;
    }

    /**
     * 按大小范围搜索
     */
    @Override
    public List<FileInfo> searchBySizeRange(Long minSize, Long maxSize, Integer page, Integer size) {
        // TODO: 实现按大小范围搜索
        return new ArrayList<>();
    }

    /**
     * 按时间范围搜索
     */
    @Override
    public List<FileInfo> searchByTimeRange(String startTime, String endTime, Integer page, Integer size) {
        // TODO: 实现按时间范围搜索
        return new ArrayList<>();
    }

    /**
     * 按用户搜索
     */
    @Override
    public List<FileInfo> searchByUser(Long userId, Integer page, Integer size) {
        List<FileInfo> files = fileInfoMapper.selectByUploadUserId(userId);
        
        // 分页处理
        if (page != null && size != null) {
            int start = (page - 1) * size;
            int end = Math.min(start + size, files.size());
            if (start < files.size()) {
                return files.subList(start, end);
            }
        }
        
        return files;
    }

    /**
     * 获取搜索建议
     */
    @Override
    public List<String> getSearchSuggestions(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<String> suggestions = new ArrayList<>();
        
        // 基于热门搜索生成建议
        for (String hotSearch : hotSearchesCache) {
            if (hotSearch.toLowerCase().contains(keyword.toLowerCase())) {
                suggestions.add(hotSearch);
            }
        }
        
        // 基于搜索历史生成建议
        searchHistoryCache.values().stream()
            .flatMap(List::stream)
            .filter(history -> history.toLowerCase().contains(keyword.toLowerCase()))
            .forEach(suggestions::add);
        
        // 去重并限制数量
        return suggestions.stream()
            .distinct()
            .limit(10)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * 获取搜索历史
     */
    @Override
    public List<String> getSearchHistory(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        
        return searchHistoryCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 获取热门搜索
     */
    @Override
    public List<String> getHotSearches() {
        // 初始化热门搜索
        if (hotSearchesCache.isEmpty()) {
            hotSearchesCache.addAll(Arrays.asList(
                "文档", "图片", "视频", "音乐", "软件", "压缩包", "PDF", "Word", "Excel"
            ));
        }
        
        return new ArrayList<>(hotSearchesCache);
    }

    /**
     * 记录搜索日志
     */
    @Override
    public void logSearch(String keyword, Long userId, String ipAddress, String userAgent) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return;
        }
        
        // 记录到搜索历史
        if (userId != null) {
            searchHistoryCache.computeIfAbsent(userId, k -> new ArrayList<>());
            List<String> history = searchHistoryCache.get(userId);
            
            // 添加到历史记录开头
            history.add(0, keyword);
            
            // 限制历史记录数量
            if (history.size() > 50) {
                history.subList(50, history.size()).clear();
            }
        }
        
        // 记录搜索统计
        searchStatsCache.merge(keyword, 1, Integer::sum);
        
        System.out.println("搜索日志: 关键词=" + keyword + ", 用户ID=" + userId + 
                          ", IP=" + ipAddress + ", UserAgent=" + userAgent);
    }

    /**
     * 更新搜索统计
     */
    @Override
    public void updateSearchStats(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            searchStatsCache.merge(keyword, 1, Integer::sum);
        }
    }

    /**
     * 获取搜索统计
     */
    @Override
    public Map<String, Object> getSearchStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 总搜索次数
        int totalSearches = searchStatsCache.values().stream().mapToInt(Integer::intValue).sum();
        stats.put("totalSearches", totalSearches);
        
        // 热门关键词
        List<Map<String, Object>> hotKeywords = searchStatsCache.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(10)
            .map(entry -> {
                Map<String, Object> keyword = new HashMap<>();
                keyword.put("keyword", entry.getKey());
                keyword.put("count", entry.getValue());
                return keyword;
            })
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        
        stats.put("hotKeywords", hotKeywords);
        
        // 今日搜索次数
        stats.put("todaySearches", totalSearches); // TODO: 实现真实统计
        
        return stats;
    }

    /**
     * 清理搜索缓存
     */
    @Override
    public boolean cleanupSearchCache() {
        try {
            searchHistoryCache.clear();
            hotSearchesCache.clear();
            searchStatsCache.clear();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 重建搜索索引
     */
    @Override
    public boolean rebuildSearchIndex() {
        // TODO: 实现搜索索引重建
        return true;
    }

    /**
     * 构建搜索条件
     */
    private FileInfo buildSearchCondition(SearchDto searchDto) {
        FileInfo condition = new FileInfo();
        
        if (searchDto.getKeyword() != null && !searchDto.getKeyword().trim().isEmpty()) {
            // 关键词搜索，这里简化处理
            // 实际应该使用全文搜索或模糊查询
        }
        
        if (searchDto.getFileType() != null && !searchDto.getFileType().trim().isEmpty()) {
            condition.setFileType(searchDto.getFileType());
        }
        
        if (searchDto.getUploadUserId() != null) {
            condition.setUploadUserId(searchDto.getUploadUserId());
        }
        
        if (searchDto.getStatus() != null) {
            condition.setStatus(searchDto.getStatus());
        }
        
        if (searchDto.getIsPublic() != null) {
            condition.setIsPublic(searchDto.getIsPublic());
        }
        
        return condition;
    }
}

