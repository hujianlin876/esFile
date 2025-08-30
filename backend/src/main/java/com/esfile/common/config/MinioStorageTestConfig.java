package com.esfile.common.config;

import io.minio.MinioClient;
import io.minio.BucketExistsArgs;
import io.minio.ListObjectsArgs;
import io.minio.StatObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * MinIO存储测试配置类
 * 用于验证MinIO连接和配置是否正确
 * 
 * @author esfile
 * @since 1.0.0
 */
@Component
public class MinioStorageTestConfig implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MinioStorageTestConfig.class);

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Override
    public void run(String... args) throws Exception {
        logger.info("开始测试MinIO存储配置...");
        
        try {
            // 测试连接
            testConnection();
            
            // 测试桶操作
            testBucketOperations();
            
            logger.info("MinIO存储配置测试完成，所有功能正常！");
            
        } catch (Exception e) {
            logger.error("MinIO存储配置测试失败", e);
        }
    }

    /**
     * 测试MinIO连接
     */
    private void testConnection() {
        try {
            logger.info("测试MinIO连接: {}", endpoint);
            
            // 检查桶是否存在
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            logger.info("桶 '{}' 存在状态: {}", bucketName, bucketExists);
            
            if (!bucketExists) {
                logger.warn("桶 '{}' 不存在，将在MinioConfig中自动创建", bucketName);
            }
            
        } catch (Exception e) {
            logger.error("MinIO连接测试失败: {}", e.getMessage());
            throw new RuntimeException("MinIO连接测试失败", e);
        }
    }

    /**
     * 测试桶操作
     */
    private void testBucketOperations() {
        try {
            logger.info("测试桶操作...");
            
            // 列出桶中的对象
            Iterable<io.minio.Result<io.minio.messages.Item>> objects = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).build());
            int objectCount = 0;
            for (io.minio.Result<io.minio.messages.Item> object : objects) {
                objectCount++;
                if (objectCount <= 5) { // 只显示前5个对象
                    try {
                        io.minio.messages.Item item = object.get();
                        logger.debug("对象: {}, 大小: {} bytes, 最后修改: {}", 
                            item.objectName(), item.size(), item.lastModified());
                    } catch (Exception e) {
                        logger.debug("无法获取对象信息");
                    }
                }
            }
            
            logger.info("桶中共有 {} 个对象", objectCount);
            
        } catch (Exception e) {
            logger.error("桶操作测试失败: {}", e.getMessage());
            throw new RuntimeException("桶操作测试失败", e);
        }
    }

    /**
     * 获取MinIO状态信息
     */
    public String getMinioStatus() {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            
            if (bucketExists) {
                Iterable<io.minio.Result<io.minio.messages.Item>> objects = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).build());
                int objectCount = 0;
                for (io.minio.Result<io.minio.messages.Item> object : objects) {
                    objectCount++;
                }
                
                return String.format("MinIO连接正常 - 桶: %s, 对象数量: %d", bucketName, objectCount);
            } else {
                return String.format("MinIO连接正常 - 桶: %s (不存在)", bucketName);
            }
            
        } catch (Exception e) {
            return String.format("MinIO连接异常: %s", e.getMessage());
        }
    }
}
