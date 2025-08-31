package com.esfile.service.file.impl;

import com.esfile.entity.elasticsearch.FileDocument;
import com.esfile.service.file.ElasticsearchIndexService;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Elasticsearch索引服务实现类
 * 提供索引创建、重建、删除等功能
 * 
 * @author esfile
 * @since 1.0.0
 */
@Service
public class ElasticsearchIndexServiceImpl implements ElasticsearchIndexService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchIndexServiceImpl.class);
    private static final String INDEX_NAME = "file_documents";

    @Autowired
    private RestHighLevelClient elasticsearchClient;

    @Override
    public boolean createIndex() {
        try {
            logger.info("创建ES索引: {}", INDEX_NAME);
            
            // 检查索引是否已存在
            if (indexExists()) {
                logger.info("索引已存在: {}", INDEX_NAME);
                return true;
            }

            // 创建索引请求
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(INDEX_NAME);
            
            // 设置分片和副本
            createIndexRequest.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
                .put("index.max_result_window", 10000)
                .put("index.refresh_interval", "1s")
            );

            // 读取映射文件
            try {
                ClassPathResource mappingResource = new ClassPathResource("es-mapping.json");
                String mapping = readInputStream(mappingResource.getInputStream());
                createIndexRequest.mapping(mapping, XContentType.JSON);
            } catch (IOException e) {
                logger.warn("无法读取映射文件，使用默认映射: {}", e.getMessage());
                // 使用默认映射
                createIndexRequest.mapping("{\"properties\":{\"fileName\":{\"type\":\"text\"}}}", XContentType.JSON);
            }

            // 读取设置文件
            try {
                ClassPathResource settingsResource = new ClassPathResource("es-settings.json");
                String settings = readInputStream(settingsResource.getInputStream());
                createIndexRequest.settings(settings, XContentType.JSON);
            } catch (IOException e) {
                logger.warn("无法读取设置文件，使用默认设置: {}", e.getMessage());
            }

            // 执行创建索引
            boolean acknowledged = elasticsearchClient.indices().create(createIndexRequest, RequestOptions.DEFAULT).isAcknowledged();
            
            if (acknowledged) {
                logger.info("索引创建成功: {}", INDEX_NAME);
                return true;
            } else {
                logger.error("索引创建失败: {}", INDEX_NAME);
                return false;
            }
            
        } catch (Exception e) {
            logger.error("创建索引异常: {}", INDEX_NAME, e);
            return false;
        }
    }

    @Override
    public boolean deleteIndex() {
        try {
            logger.info("删除ES索引: {}", INDEX_NAME);
            
            if (!indexExists()) {
                logger.info("索引不存在: {}", INDEX_NAME);
                return true;
            }

            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(INDEX_NAME);
            boolean acknowledged = elasticsearchClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT).isAcknowledged();
            
            if (acknowledged) {
                logger.info("索引删除成功: {}", INDEX_NAME);
                return true;
            } else {
                logger.error("索引删除失败: {}", INDEX_NAME);
                return false;
            }
            
        } catch (Exception e) {
            logger.error("删除索引异常: {}", INDEX_NAME, e);
            return false;
        }
    }

    @Override
    public boolean rebuildIndex() {
        try {
            logger.info("重建ES索引: {}", INDEX_NAME);
            
            // 删除旧索引
            if (!deleteIndex()) {
                logger.error("删除旧索引失败");
                return false;
            }
            
            // 等待删除完成
            Thread.sleep(1000);
            
            // 创建新索引
            if (!createIndex()) {
                logger.error("创建新索引失败");
                return false;
            }
            
            logger.info("索引重建成功: {}", INDEX_NAME);
            return true;
            
        } catch (Exception e) {
            logger.error("重建索引异常: {}", INDEX_NAME, e);
            return false;
        }
    }

    @Override
    public boolean indexExists() {
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest(INDEX_NAME);
            return elasticsearchClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            logger.error("检查索引存在性异常: {}", INDEX_NAME, e);
            return false;
        }
    }

    @Override
    public String getIndexStatus() {
        try {
            if (!indexExists()) {
                return "NOT_EXISTS";
            }
            
            // 这里可以添加更详细的索引状态检查
            return "EXISTS";
            
        } catch (Exception e) {
            logger.error("获取索引状态异常: {}", INDEX_NAME, e);
            return "ERROR";
        }
    }

    @Override
    public boolean optimizeIndex() {
        try {
            if (!indexExists()) {
                logger.warn("索引不存在，无法优化: {}", INDEX_NAME);
                return false;
            }
            
            logger.info("优化ES索引: {}", INDEX_NAME);
            // 在ES 7.x中，optimize已被forcemerge替代，这里暂时跳过
            logger.info("索引优化跳过（ES 7.x中已废弃）: {}", INDEX_NAME);
            return true;
            
        } catch (Exception e) {
            logger.error("优化索引异常: {}", INDEX_NAME, e);
            return false;
        }
    }

    @Override
    public boolean refreshIndex() {
        try {
            if (!indexExists()) {
                logger.warn("索引不存在，无法刷新: {}", INDEX_NAME);
                return false;
            }
            
            logger.info("刷新ES索引: {}", INDEX_NAME);
            RefreshRequest refreshRequest = new RefreshRequest(INDEX_NAME);
            elasticsearchClient.indices().refresh(refreshRequest, RequestOptions.DEFAULT);
            
            logger.info("索引刷新成功: {}", INDEX_NAME);
            return true;
            
        } catch (Exception e) {
            logger.error("刷新索引异常: {}", INDEX_NAME, e);
            return false;
        }
    }

    /**
     * 读取输入流内容（Java 8兼容）
     */
    private String readInputStream(java.io.InputStream inputStream) throws IOException {
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            return content.toString();
        }
    }
}
