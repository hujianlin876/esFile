package com.esfile.controller.file;

import com.esfile.common.vo.ResponseResult;
import com.esfile.entity.dto.FileSearchDto;
import com.esfile.entity.mybatis.FileInfo;
import com.esfile.service.file.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 文件查询控制器
 * 负责文件的查询、搜索、统计等功能
 * 
 * @author esfile
 * @since 1.0.0
 */
@RestController
@RequestMapping("/files/query")
public class FileQueryController {

    private static final Logger logger = LoggerFactory.getLogger(FileQueryController.class);

    @Autowired
    private FileService fileService;

    /**
     * 获取文件列表（分页）
     */
    @GetMapping("/list")
    public ResponseResult<Map<String, Object>> getFileList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String fileType,
            @RequestParam(required = false) String dateRange,
            @RequestParam(required = false) String sizeRange,
            @RequestParam(required = false) String uploader,
            @RequestParam(required = false) String[] tags,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        
        try {
            logger.info("获取文件列表: page={}, size={}, keyword={}", page, size, keyword);
            
            FileSearchDto searchDto = new FileSearchDto();
            searchDto.setPage(page);
            searchDto.setSize(size);
            searchDto.setKeyword(keyword);
            searchDto.setFileType(fileType);
            if (dateRange != null) {
                String[] dateRangeArray = dateRange.split(",");
                searchDto.setDateRange(dateRangeArray);
            }
            if (sizeRange != null) {
                String[] sizeRangeArray = sizeRange.split(",");
                if (sizeRangeArray.length >= 2) {
                    try {
                        Long[] sizeRangeLong = {Long.parseLong(sizeRangeArray[0]), Long.parseLong(sizeRangeArray[1])};
                        searchDto.setSizeRange(sizeRangeLong);
                    } catch (NumberFormatException e) {
                        logger.warn("Invalid size range format: {}", sizeRange);
                    }
                }
            }
            searchDto.setUploader(uploader);
            if (tags != null && tags.length > 0) {
                searchDto.setTags(String.join(",", tags));
            }
            searchDto.setSortBy(sortBy);
            searchDto.setSortOrder(sortOrder);
            
            Map<String, Object> result = fileService.getFileList(searchDto);
            return ResponseResult.success(result);
        } catch (Exception e) {
            logger.error("获取文件列表失败", e);
            return ResponseResult.error("获取文件列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件详情
     */
    @GetMapping("/{id}")
    public ResponseResult<FileInfo> getFileDetail(@PathVariable Long id) {
        try {
            logger.info("获取文件详情: id={}", id);
            
            FileInfo fileInfo = fileService.getFileDetail(id);
            if (fileInfo == null) {
                return ResponseResult.error("文件不存在");
            }
            
            return ResponseResult.success(fileInfo);
        } catch (Exception e) {
            logger.error("获取文件详情失败", e);
            return ResponseResult.error("获取文件详情失败: " + e.getMessage());
        }
    }

    /**
     * 搜索文件
     */
    @PostMapping("/search")
    public ResponseResult<Map<String, Object>> searchFiles(@RequestBody FileSearchDto searchDto) {
        try {
            logger.info("搜索文件: keyword={}", searchDto.getKeyword());
            
            Map<String, Object> result = fileService.searchFiles(searchDto);
            return ResponseResult.success(result);
        } catch (Exception e) {
            logger.error("搜索文件失败", e);
            return ResponseResult.error("搜索文件失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件统计信息
     */
    @GetMapping("/stats")
    public ResponseResult<Map<String, Object>> getFileStats() {
        try {
            logger.info("获取文件统计信息");
            
            Map<String, Object> stats = fileService.getFileStats();
            return ResponseResult.success(stats);
        } catch (Exception e) {
            logger.error("获取文件统计信息失败", e);
            return ResponseResult.error("获取文件统计信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件类型统计
     */
    @GetMapping("/stats/types")
    public ResponseResult<?> getFileTypeStats() {
        try {
            logger.info("获取文件类型统计");
            
            List<Map<String, Object>> stats = fileService.getFileTypeStats();
            return ResponseResult.success(stats);
        } catch (Exception e) {
            logger.error("获取文件类型统计失败", e);
            return ResponseResult.error("获取文件类型统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取存储使用情况
     */
    @GetMapping("/stats/storage")
    public ResponseResult<Map<String, Object>> getStorageUsageStats() {
        try {
            logger.info("获取存储使用情况");
            
            Map<String, Object> usage = fileService.getStorageUsageStats();
            return ResponseResult.success(usage);
        } catch (Exception e) {
            logger.error("获取存储使用情况失败", e);
            return ResponseResult.error("获取存储使用情况失败: " + e.getMessage());
        }
    }

    /**
     * 查找重复文件
     */
    @GetMapping("/duplicates")
    public ResponseResult<?> findDuplicateFiles() {
        try {
            logger.info("查找重复文件");
            
            List<Map<String, Object>> duplicates = fileService.findDuplicateFiles();
            return ResponseResult.success(duplicates);
        } catch (Exception e) {
            logger.error("查找重复文件失败", e);
            return ResponseResult.error("查找重复文件失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件夹结构
     */
    @GetMapping("/folders/structure")
    public ResponseResult<?> getFolderStructure(@RequestParam(required = false) Long parentId) {
        try {
            logger.info("获取文件夹结构: parentId={}", parentId);
            
            List<Map<String, Object>> structure = fileService.getFolderStructure(parentId);
            return ResponseResult.success(structure);
        } catch (Exception e) {
            logger.error("获取文件夹结构失败", e);
            return ResponseResult.error("获取文件夹结构失败: " + e.getMessage());
        }
    }
}
