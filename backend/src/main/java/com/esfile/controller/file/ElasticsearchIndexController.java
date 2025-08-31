package com.esfile.controller.file;

import com.esfile.common.vo.ResponseResult;
import com.esfile.service.file.ElasticsearchIndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Elasticsearch索引管理控制器
 * 提供索引创建、重建、删除等管理功能
 * 
 * @author esfile
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/es/index")
public class ElasticsearchIndexController {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchIndexController.class);

    @Autowired
    private ElasticsearchIndexService elasticsearchIndexService;

    /**
     * 创建索引
     */
    @PostMapping("/create")
    public ResponseResult<String> createIndex() {
        try {
            logger.info("创建ES索引");
            boolean success = elasticsearchIndexService.createIndex();
            if (success) {
                return ResponseResult.success("索引创建成功");
            } else {
                return ResponseResult.fail("索引创建失败");
            }
        } catch (Exception e) {
            logger.error("创建索引失败", e);
            return ResponseResult.fail("创建索引失败: " + e.getMessage());
        }
    }

    /**
     * 删除索引
     */
    @DeleteMapping("/delete")
    public ResponseResult<String> deleteIndex() {
        try {
            logger.info("删除ES索引");
            boolean success = elasticsearchIndexService.deleteIndex();
            if (success) {
                return ResponseResult.success("索引删除成功");
            } else {
                return ResponseResult.fail("索引删除失败");
            }
        } catch (Exception e) {
            logger.error("删除索引失败", e);
            return ResponseResult.fail("删除索引失败: " + e.getMessage());
        }
    }

    /**
     * 重建索引
     */
    @PostMapping("/rebuild")
    public ResponseResult<String> rebuildIndex() {
        try {
            logger.info("重建ES索引");
            boolean success = elasticsearchIndexService.rebuildIndex();
            if (success) {
                return ResponseResult.success("索引重建成功");
            } else {
                return ResponseResult.fail("索引重建失败");
            }
        } catch (Exception e) {
            logger.error("重建索引失败", e);
            return ResponseResult.fail("重建索引失败: " + e.getMessage());
        }
    }

    /**
     * 检查索引状态
     */
    @GetMapping("/status")
    public ResponseResult<String> getIndexStatus() {
        try {
            logger.info("获取ES索引状态");
            String status = elasticsearchIndexService.getIndexStatus();
            return ResponseResult.success(status);
        } catch (Exception e) {
            logger.error("获取索引状态失败", e);
            return ResponseResult.fail("获取索引状态失败: " + e.getMessage());
        }
    }

    /**
     * 检查索引是否存在
     */
    @GetMapping("/exists")
    public ResponseResult<Boolean> indexExists() {
        try {
            logger.info("检查ES索引是否存在");
            boolean exists = elasticsearchIndexService.indexExists();
            return ResponseResult.success(exists);
        } catch (Exception e) {
            logger.error("检查索引存在性失败", e);
            return ResponseResult.fail("检查索引存在性失败: " + e.getMessage());
        }
    }

    /**
     * 优化索引
     */
    @PostMapping("/optimize")
    public ResponseResult<String> optimizeIndex() {
        try {
            logger.info("优化ES索引");
            boolean success = elasticsearchIndexService.optimizeIndex();
            if (success) {
                return ResponseResult.success("索引优化成功");
            } else {
                return ResponseResult.fail("索引优化失败");
            }
        } catch (Exception e) {
            logger.error("优化索引失败", e);
            return ResponseResult.fail("优化索引失败: " + e.getMessage());
        }
    }

    /**
     * 刷新索引
     */
    @PostMapping("/refresh")
    public ResponseResult<String> refreshIndex() {
        try {
            logger.info("刷新ES索引");
            boolean success = elasticsearchIndexService.refreshIndex();
            if (success) {
                return ResponseResult.success("索引刷新成功");
            } else {
                return ResponseResult.fail("索引刷新失败");
            }
        } catch (Exception e) {
            logger.error("刷新索引失败", e);
            return ResponseResult.fail("刷新索引失败: " + e.getMessage());
        }
    }
}
