package com.esfile.controller.file;

import com.esfile.common.vo.ResponseResult;
import com.esfile.entity.dto.FileSearchDto;
import com.esfile.entity.elasticsearch.FileDocument;
import com.esfile.service.file.ElasticsearchSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Elasticsearch搜索控制器
 * 提供基于ES的文件搜索API
 * 
 * @author esfile
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/search")
public class ElasticsearchSearchController {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchSearchController.class);

    @Autowired
    private ElasticsearchSearchService elasticsearchSearchService;

    /**
     * 全文搜索
     */
    @GetMapping("/fulltext")
    public ResponseResult<Page<FileDocument>> fullTextSearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("执行全文搜索: {}", query);
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<FileDocument> results = elasticsearchSearchService.fullTextSearch(query, pageable);
            return ResponseResult.success(results);
        } catch (Exception e) {
            logger.error("全文搜索失败: {}", query, e);
            return ResponseResult.fail("搜索失败: " + e.getMessage());
        }
    }

    /**
     * 关键词搜索
     */
    @GetMapping("/keyword")
    public ResponseResult<Page<FileDocument>> keywordSearch(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("执行关键词搜索: {}", keyword);
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<FileDocument> results = elasticsearchSearchService.keywordSearch(keyword, pageable);
            return ResponseResult.success(results);
        } catch (Exception e) {
            logger.error("关键词搜索失败: {}", keyword, e);
            return ResponseResult.fail("搜索失败: " + e.getMessage());
        }
    }

    /**
     * 高级搜索
     */
    @PostMapping("/advanced")
    public ResponseResult<Page<FileDocument>> advancedSearch(
            @RequestBody FileSearchDto searchDto,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("执行高级搜索: {}", searchDto);
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<FileDocument> results = elasticsearchSearchService.advancedSearch(searchDto, pageable);
            return ResponseResult.success(results);
        } catch (Exception e) {
            logger.error("高级搜索失败: {}", searchDto, e);
            return ResponseResult.fail("搜索失败: " + e.getMessage());
        }
    }

    /**
     * 根据文件类型搜索
     */
    @GetMapping("/type/{fileType}")
    public ResponseResult<List<FileDocument>> searchByFileType(@PathVariable String fileType) {
        try {
            logger.info("根据文件类型搜索: {}", fileType);
            List<FileDocument> results = elasticsearchSearchService.searchByFileType(fileType);
            return ResponseResult.success(results);
        } catch (Exception e) {
            logger.error("根据文件类型搜索失败: {}", fileType, e);
            return ResponseResult.fail("搜索失败: " + e.getMessage());
        }
    }

    /**
     * 根据标签搜索
     */
    @GetMapping("/tags")
    public ResponseResult<List<FileDocument>> searchByTags(@RequestParam String tags) {
        try {
            logger.info("根据标签搜索: {}", tags);
            List<FileDocument> results = elasticsearchSearchService.searchByTags(tags);
            return ResponseResult.success(results);
        } catch (Exception e) {
            logger.error("根据标签搜索失败: {}", tags, e);
            return ResponseResult.fail("搜索失败: " + e.getMessage());
        }
    }

    /**
     * 根据上传用户搜索
     */
    @GetMapping("/uploader")
    public ResponseResult<List<FileDocument>> searchByUploadUser(@RequestParam String uploadUserName) {
        try {
            logger.info("根据上传用户搜索: {}", uploadUserName);
            List<FileDocument> results = elasticsearchSearchService.searchByUploadUser(uploadUserName);
            return ResponseResult.success(results);
        } catch (Exception e) {
            logger.error("根据上传用户搜索失败: {}", uploadUserName, e);
            return ResponseResult.fail("搜索失败: " + e.getMessage());
        }
    }

    /**
     * 根据文件大小范围搜索
     */
    @GetMapping("/size")
    public ResponseResult<List<FileDocument>> searchByFileSizeRange(
            @RequestParam(required = false) Long minSize,
            @RequestParam(required = false) Long maxSize) {
        try {
            logger.info("根据文件大小范围搜索: {} - {}", minSize, maxSize);
            List<FileDocument> results = elasticsearchSearchService.searchByFileSizeRange(minSize, maxSize);
            return ResponseResult.success(results);
        } catch (Exception e) {
            logger.error("根据文件大小范围搜索失败: {} - {}", minSize, maxSize, e);
            return ResponseResult.fail("搜索失败: " + e.getMessage());
        }
    }

    /**
     * 根据时间范围搜索
     */
    @GetMapping("/time")
    public ResponseResult<List<FileDocument>> searchByTimeRange(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        try {
            logger.info("根据时间范围搜索: {} - {}", startTime, endTime);
            List<FileDocument> results = elasticsearchSearchService.searchByTimeRange(startTime, endTime);
            return ResponseResult.success(results);
        } catch (Exception e) {
            logger.error("根据时间范围搜索失败: {} - {}", startTime, endTime, e);
            return ResponseResult.fail("搜索失败: " + e.getMessage());
        }
    }

    /**
     * 获取搜索建议
     */
    @GetMapping("/suggestions")
    public ResponseResult<List<String>> getSearchSuggestions(@RequestParam String prefix) {
        try {
            logger.info("获取搜索建议: {}", prefix);
            List<String> suggestions = elasticsearchSearchService.getSearchSuggestions(prefix);
            return ResponseResult.success(suggestions);
        } catch (Exception e) {
            logger.error("获取搜索建议失败: {}", prefix, e);
            return ResponseResult.fail("获取搜索建议失败: " + e.getMessage());
        }
    }

    /**
     * 查找相似文件
     */
    @GetMapping("/similar/{fileId}")
    public ResponseResult<List<FileDocument>> findSimilarFiles(
            @PathVariable String fileId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("查找相似文件: {}", fileId);
            Pageable pageable = PageRequest.of(page - 1, size);
            List<FileDocument> results = elasticsearchSearchService.findSimilarFiles(fileId, pageable);
            return ResponseResult.success(results);
        } catch (Exception e) {
            logger.error("查找相似文件失败: {}", fileId, e);
            return ResponseResult.fail("查找相似文件失败: " + e.getMessage());
        }
    }

    /**
     * 查找热门文件
     */
    @GetMapping("/popular")
    public ResponseResult<Page<FileDocument>> findPopularFiles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("查找热门文件");
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<FileDocument> results = elasticsearchSearchService.findPopularFiles(pageable);
            return ResponseResult.success(results);
        } catch (Exception e) {
            logger.error("查找热门文件失败", e);
            return ResponseResult.fail("查找热门文件失败: " + e.getMessage());
        }
    }

    /**
     * 查找最近上传文件
     */
    @GetMapping("/recent")
    public ResponseResult<Page<FileDocument>> findRecentFiles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            logger.info("查找最近上传文件");
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<FileDocument> results = elasticsearchSearchService.findRecentFiles(pageable);
            return ResponseResult.success(results);
        } catch (Exception e) {
            logger.error("查找最近上传文件失败", e);
            return ResponseResult.fail("查找最近上传文件失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件类型统计
     */
    @GetMapping("/stats/file-types")
    public ResponseResult<Map<String, Object>> getFileTypeStats() {
        try {
            logger.info("获取文件类型统计");
            Map<String, Object> stats = elasticsearchSearchService.getFileTypeStats();
            return ResponseResult.success(stats);
        } catch (Exception e) {
            logger.error("获取文件类型统计失败", e);
            return ResponseResult.fail("获取文件类型统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取上传用户统计
     */
    @GetMapping("/stats/upload-users")
    public ResponseResult<Map<String, Object>> getUploadUserStats() {
        try {
            logger.info("获取上传用户统计");
            Map<String, Object> stats = elasticsearchSearchService.getUploadUserStats();
            return ResponseResult.success(stats);
        } catch (Exception e) {
            logger.error("获取上传用户统计失败", e);
            return ResponseResult.fail("获取上传用户统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取标签统计
     */
    @GetMapping("/stats/tags")
    public ResponseResult<Map<String, Object>> getTagStats() {
        try {
            logger.info("获取标签统计");
            Map<String, Object> stats = elasticsearchSearchService.getTagStats();
            return ResponseResult.success(stats);
        } catch (Exception e) {
            logger.error("获取标签统计失败", e);
            return ResponseResult.fail("获取标签统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取每日上传统计
     */
    @GetMapping("/stats/daily-uploads")
    public ResponseResult<Map<String, Object>> getDailyUploadStats() {
        try {
            logger.info("获取每日上传统计");
            Map<String, Object> stats = elasticsearchSearchService.getDailyUploadStats();
            return ResponseResult.success(stats);
        } catch (Exception e) {
            logger.error("获取每日上传统计失败", e);
            return ResponseResult.fail("获取每日上传统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件大小分布统计
     */
    @GetMapping("/stats/size-ranges")
    public ResponseResult<Map<String, Object>> getSizeRangeStats() {
        try {
            logger.info("获取文件大小分布统计");
            Map<String, Object> stats = elasticsearchSearchService.getSizeRangeStats();
            return ResponseResult.success(stats);
        } catch (Exception e) {
            logger.error("获取文件大小分布统计失败", e);
            return ResponseResult.fail("获取文件大小分布统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取热门搜索词
     */
    @GetMapping("/hot-terms")
    public ResponseResult<List<String>> getHotSearchTerms() {
        try {
            logger.info("获取热门搜索词");
            List<String> terms = elasticsearchSearchService.getHotSearchTerms();
            return ResponseResult.success(terms);
        } catch (Exception e) {
            logger.error("获取热门搜索词失败", e);
            return ResponseResult.fail("获取热门搜索词失败: " + e.getMessage());
        }
    }

    /**
     * 获取相关搜索词
     */
    @GetMapping("/related-terms")
    public ResponseResult<List<String>> getRelatedSearchTerms(@RequestParam String query) {
        try {
            logger.info("获取相关搜索词: {}", query);
            List<String> terms = elasticsearchSearchService.getRelatedSearchTerms(query);
            return ResponseResult.success(terms);
        } catch (Exception e) {
            logger.error("获取相关搜索词失败: {}", query, e);
            return ResponseResult.fail("获取相关搜索词失败: " + e.getMessage());
        }
    }

    /**
     * 重建索引
     */
    @PostMapping("/rebuild-index")
    public ResponseResult<String> rebuildIndex() {
        try {
            logger.info("重建ES索引");
            boolean success = elasticsearchSearchService.rebuildIndex();
            if (success) {
                return ResponseResult.success("索引重建成功");
            } else {
                return ResponseResult.fail("索引重建失败");
            }
        } catch (Exception e) {
            logger.error("重建ES索引失败", e);
            return ResponseResult.fail("索引重建失败: " + e.getMessage());
        }
    }
}
