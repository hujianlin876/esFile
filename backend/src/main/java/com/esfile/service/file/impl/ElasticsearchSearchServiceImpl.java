package com.esfile.service.file.impl;

import com.esfile.entity.dto.FileSearchDto;
import com.esfile.entity.elasticsearch.FileDocument;
import com.esfile.repository.elasticsearch.FileDocumentRepository;
import com.esfile.service.file.ElasticsearchSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Elasticsearch搜索服务实现类
 * 提供基于ES的文件搜索功能
 * 
 * @author esfile
 * @since 1.0.0
 */
@Service
public class ElasticsearchSearchServiceImpl implements ElasticsearchSearchService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchSearchServiceImpl.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private FileDocumentRepository fileDocumentRepository;

    @Override
    public Page<FileDocument> fullTextSearch(String query, Pageable pageable) {
        try {
            logger.info("执行全文搜索: {}", query);
            return fileDocumentRepository.fullTextSearch(query, pageable);
        } catch (Exception e) {
            logger.error("全文搜索失败: {}", query, e);
            return Page.empty(pageable);
        }
    }

    @Override
    public Page<FileDocument> keywordSearch(String keyword, Pageable pageable) {
        try {
            logger.info("执行关键词搜索: {}", keyword);
            return fileDocumentRepository.searchByKeyword(keyword, pageable);
        } catch (Exception e) {
            logger.error("关键词搜索失败: {}", keyword, e);
            return Page.empty(pageable);
        }
    }

    @Override
    public Page<FileDocument> advancedSearch(FileSearchDto searchDto, Pageable pageable) {
        try {
            logger.info("执行高级搜索: {}", searchDto);
            
            // 构建搜索条件
            List<FileDocument> results = new ArrayList<>();
            
            // 关键词搜索
            if (searchDto.getKeyword() != null && !searchDto.getKeyword().trim().isEmpty()) {
                Page<FileDocument> keywordResults = keywordSearch(searchDto.getKeyword(), pageable);
                results.addAll(keywordResults.getContent());
            }
            
            // 文件类型过滤
            if (searchDto.getFileType() != null && !searchDto.getFileType().trim().isEmpty()) {
                List<FileDocument> typeResults = searchByFileType(searchDto.getFileType());
                results = filterResults(results, typeResults);
            }
            
            // 标签过滤
            if (searchDto.getTags() != null && !searchDto.getTags().trim().isEmpty()) {
                List<FileDocument> tagResults = searchByTags(searchDto.getTags());
                results = filterResults(results, tagResults);
            }
            
            // 上传用户过滤
            if (searchDto.getUploadUserName() != null && !searchDto.getUploadUserName().trim().isEmpty()) {
                List<FileDocument> userResults = searchByUploadUser(searchDto.getUploadUserName());
                results = filterResults(results, userResults);
            }
            
            // 文件大小范围过滤
            if (searchDto.getMinSize() != null || searchDto.getMaxSize() != null) {
                Long minSize = searchDto.getMinSize() != null ? searchDto.getMinSize() : 0L;
                Long maxSize = searchDto.getMaxSize() != null ? searchDto.getMaxSize() : Long.MAX_VALUE;
                List<FileDocument> sizeResults = searchByFileSizeRange(minSize, maxSize);
                results = filterResults(results, sizeResults);
            }
            
            // 时间范围过滤
            if (searchDto.getStartTime() != null || searchDto.getEndTime() != null) {
                String startTime = searchDto.getStartTime() != null ? searchDto.getStartTime() : "1970-01-01 00:00:00";
                String endTime = searchDto.getEndTime() != null ? searchDto.getEndTime() : "2099-12-31 23:59:59";
                List<FileDocument> timeResults = searchByTimeRange(startTime, endTime);
                results = filterResults(results, timeResults);
            }
            
            // 去重和排序
            results = results.stream()
                .distinct()
                .sorted(Comparator.comparing(FileDocument::getCreateTime).reversed())
                .collect(Collectors.toList());
            
            // 分页
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), results.size());
            List<FileDocument> pageContent = results.subList(start, end);
            
            return new PageImpl<>(pageContent, pageable, results.size());
            
        } catch (Exception e) {
            logger.error("高级搜索失败: {}", searchDto, e);
            return Page.empty(pageable);
        }
    }

    @Override
    public List<FileDocument> searchByFileType(String fileType) {
        try {
            logger.info("根据文件类型搜索: {}", fileType);
            return fileDocumentRepository.findByFileType(fileType);
        } catch (Exception e) {
            logger.error("根据文件类型搜索失败: {}", fileType, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<FileDocument> searchByTags(String tags) {
        try {
            logger.info("根据标签搜索: {}", tags);
            return fileDocumentRepository.findByTagsContaining(tags);
        } catch (Exception e) {
            logger.error("根据标签搜索失败: {}", tags, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<FileDocument> searchByUploadUser(String uploadUserName) {
        try {
            logger.info("根据上传用户搜索: {}", uploadUserName);
            return fileDocumentRepository.findByUploadUserNameContaining(uploadUserName);
        } catch (Exception e) {
            logger.error("根据上传用户搜索失败: {}", uploadUserName, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<FileDocument> searchByFileSizeRange(Long minSize, Long maxSize) {
        try {
            logger.info("根据文件大小范围搜索: {} - {}", minSize, maxSize);
            return fileDocumentRepository.findByFileSizeBetween(minSize, maxSize);
        } catch (Exception e) {
            logger.error("根据文件大小范围搜索失败: {} - {}", minSize, maxSize, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<FileDocument> searchByTimeRange(String startTime, String endTime) {
        try {
            logger.info("根据时间范围搜索: {} - {}", startTime, endTime);
            Date startDate = DATE_FORMAT.parse(startTime);
            Date endDate = DATE_FORMAT.parse(endTime);
            return fileDocumentRepository.findByCreateTimeBetween(startDate, endDate);
        } catch (ParseException e) {
            logger.error("时间格式解析失败: {} - {}", startTime, endTime, e);
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("根据时间范围搜索失败: {} - {}", startTime, endTime, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> getSearchSuggestions(String prefix) {
        try {
            logger.info("获取搜索建议: {}", prefix);
            List<FileDocument> suggestions = fileDocumentRepository.getSearchSuggestions(prefix);
            return suggestions.stream()
                .map(FileDocument::getFileName)
                .distinct()
                .limit(10)
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("获取搜索建议失败: {}", prefix, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<FileDocument> findSimilarFiles(String fileId, Pageable pageable) {
        try {
            logger.info("查找相似文件: {}", fileId);
            Page<FileDocument> page = fileDocumentRepository.findSimilarFiles(fileId, pageable);
            return page != null ? page.getContent() : new ArrayList<>();
        } catch (Exception e) {
            logger.error("查找相似文件失败: {}", fileId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public Page<FileDocument> findPopularFiles(Pageable pageable) {
        try {
            logger.info("查找热门文件");
            return fileDocumentRepository.findPopularFiles(pageable);
        } catch (Exception e) {
            logger.error("查找热门文件失败", e);
            return Page.empty(pageable);
        }
    }

    @Override
    public Page<FileDocument> findRecentFiles(Pageable pageable) {
        try {
            logger.info("查找最近上传文件");
            return fileDocumentRepository.findRecentFiles(pageable);
        } catch (Exception e) {
            logger.error("查找最近上传文件失败", e);
            return Page.empty(pageable);
        }
    }

    @Override
    public Map<String, Object> getFileTypeStats() {
        try {
            logger.info("获取文件类型统计");
            // 由于聚合查询可能返回空结果，这里使用基础查询
            Iterable<FileDocument> allFilesIter = fileDocumentRepository.findAll();
            List<FileDocument> allFiles = new ArrayList<>();
            allFilesIter.forEach(allFiles::add);
            
            Map<String, Long> typeStats = allFiles.stream()
                .collect(Collectors.groupingBy(
                    doc -> doc.getFileType() != null ? doc.getFileType() : "unknown",
                    Collectors.counting()
                ));
            
            Map<String, Object> result = new HashMap<>();
            result.put("fileTypes", typeStats);
            result.put("totalTypes", typeStats.size());
            return result;
        } catch (Exception e) {
            logger.error("获取文件类型统计失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> getUploadUserStats() {
        try {
            logger.info("获取上传用户统计");
            // 使用基础查询统计用户
            Iterable<FileDocument> allFilesIter = fileDocumentRepository.findAll();
            List<FileDocument> allFiles = new ArrayList<>();
            allFilesIter.forEach(allFiles::add);
            
            Map<String, Long> userStats = allFiles.stream()
                .collect(Collectors.groupingBy(
                    doc -> doc.getUploadUserName() != null ? doc.getUploadUserName() : "unknown",
                    Collectors.counting()
                ));
            
            Map<String, Object> result = new HashMap<>();
            result.put("uploadUsers", userStats);
            result.put("totalUsers", userStats.size());
            return result;
        } catch (Exception e) {
            logger.error("获取上传用户统计失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> getTagStats() {
        try {
            logger.info("获取标签统计");
            List<FileDocument> stats = fileDocumentRepository.getTagStats();
            Map<String, Object> result = new HashMap<>();
            result.put("tags", stats);
            result.put("totalTags", stats.size());
            return result;
        } catch (Exception e) {
            logger.error("获取标签统计失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> getDailyUploadStats() {
        try {
            logger.info("获取每日上传统计");
            List<FileDocument> stats = fileDocumentRepository.getDailyUploadStats();
            Map<String, Object> result = new HashMap<>();
            result.put("dailyUploads", stats);
            result.put("totalDays", stats.size());
            return result;
        } catch (Exception e) {
            logger.error("获取每日上传统计失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> getSizeRangeStats() {
        try {
            logger.info("获取文件大小分布统计");
            List<FileDocument> stats = fileDocumentRepository.getSizeRangeStats();
            Map<String, Object> result = new HashMap<>();
            result.put("sizeRanges", stats);
            result.put("totalRanges", stats.size());
            return result;
        } catch (Exception e) {
            logger.error("获取文件大小分布统计失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public boolean indexFile(FileDocument fileDocument) {
        try {
            logger.info("索引文件到ES: {}", fileDocument.getFileName());
            fileDocumentRepository.save(fileDocument);
            return true;
        } catch (Exception e) {
            logger.error("索引文件到ES失败: {}", fileDocument.getFileName(), e);
            return false;
        }
    }

    @Override
    public boolean batchIndexFiles(List<FileDocument> fileDocuments) {
        try {
            logger.info("批量索引文件到ES: {}", fileDocuments.size());
            fileDocumentRepository.saveAll(fileDocuments);
            return true;
        } catch (Exception e) {
            logger.error("批量索引文件到ES失败: {}", fileDocuments.size(), e);
            return false;
        }
    }

    @Override
    public boolean deleteFileIndex(String fileId) {
        try {
            logger.info("从ES删除文件索引: {}", fileId);
            fileDocumentRepository.deleteById(fileId);
            return true;
        } catch (Exception e) {
            logger.error("从ES删除文件索引失败: {}", fileId, e);
            return false;
        }
    }

    @Override
    public boolean updateFileIndex(FileDocument fileDocument) {
        try {
            logger.info("更新ES中的文件索引: {}", fileDocument.getFileName());
            fileDocumentRepository.save(fileDocument);
            return true;
        } catch (Exception e) {
            logger.error("更新ES中的文件索引失败: {}", fileDocument.getFileName(), e);
            return false;
        }
    }

    @Override
    public boolean rebuildIndex() {
        try {
            logger.info("重建ES索引");
            // 这里可以实现重建索引的逻辑
            // 比如从数据库读取所有文件信息，重新索引到ES
            return true;
        } catch (Exception e) {
            logger.error("重建ES索引失败", e);
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> getSearchHistory(Long userId) {
        try {
            logger.info("获取用户搜索历史: {}", userId);
            // TODO: 实现搜索历史记录功能
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("获取用户搜索历史失败: {}", userId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public boolean saveSearchHistory(Long userId, String query, Map<String, Object> filters) {
        try {
            logger.info("保存用户搜索历史: {} - {}", userId, query);
            // TODO: 实现保存搜索历史功能
            return true;
        } catch (Exception e) {
            logger.error("保存用户搜索历史失败: {} - {}", userId, query, e);
            return false;
        }
    }

    @Override
    public List<String> getHotSearchTerms() {
        try {
            logger.info("获取热门搜索词");
            // TODO: 实现热门搜索词功能
            return Arrays.asList("文档", "图片", "视频", "PDF", "Excel");
        } catch (Exception e) {
            logger.error("获取热门搜索词失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> getRelatedSearchTerms(String query) {
        try {
            logger.info("获取相关搜索词: {}", query);
            // TODO: 实现相关搜索词推荐功能
            return Arrays.asList(query + "相关", query + "文档", query + "文件");
        } catch (Exception e) {
            logger.error("获取相关搜索词失败: {}", query, e);
            return new ArrayList<>();
        }
    }

    /**
     * 过滤搜索结果
     */
    private List<FileDocument> filterResults(List<FileDocument> mainResults, List<FileDocument> filterResults) {
        if (mainResults.isEmpty()) {
            return filterResults;
        }
        if (filterResults.isEmpty()) {
            return mainResults;
        }
        
        Set<Long> filterIds = filterResults.stream()
            .map(FileDocument::getFileId)
            .collect(Collectors.toSet());
        
        return mainResults.stream()
            .filter(doc -> filterIds.contains(doc.getFileId()))
            .collect(Collectors.toList());
    }
}
