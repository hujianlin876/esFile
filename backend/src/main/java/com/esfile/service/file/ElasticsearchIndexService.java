package com.esfile.service.file;

/**
 * Elasticsearch索引服务接口
 * 提供索引创建、重建、删除等功能
 * 
 * @author esfile
 * @since 1.0.0
 */
public interface ElasticsearchIndexService {

    /**
     * 创建索引
     */
    boolean createIndex();

    /**
     * 删除索引
     */
    boolean deleteIndex();

    /**
     * 重建索引
     */
    boolean rebuildIndex();

    /**
     * 检查索引是否存在
     */
    boolean indexExists();

    /**
     * 获取索引状态
     */
    String getIndexStatus();

    /**
     * 优化索引
     */
    boolean optimizeIndex();

    /**
     * 刷新索引
     */
    boolean refreshIndex();
}
