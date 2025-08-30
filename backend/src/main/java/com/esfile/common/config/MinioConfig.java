package com.esfile.common.config;

import io.minio.MinioClient;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.SetBucketPolicyArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;

/**
 * MinIO配置类
 * 配置MinIO客户端连接、桶设置等
 * 
 * @author esfile
 * @since 1.0.0
 */
@Configuration
public class MinioConfig {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.bucket}")
    private String bucket;

    /**
     * 配置MinIO客户端
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    /**
     * 初始化MinIO桶
     */
    @PostConstruct
    @DependsOn("minioClient")
    public void initBucket() {
        try {
            MinioClient client = minioClient();
            
            // 检查桶是否存在
            boolean bucketExists = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            
            if (!bucketExists) {
                // 创建桶
                client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                
                // 设置桶策略（允许公共读取）
                String policy = String.format(
                    "{\n" +
                    "  \"Version\": \"2012-10-17\",\n" +
                    "  \"Statement\": [\n" +
                    "    {\n" +
                    "      \"Effect\": \"Allow\",\n" +
                    "      \"Principal\": {\"AWS\": \"*\"},\n" +
                    "      \"Action\": [\n" +
                    "        \"s3:GetObject\"\n" +
                    "      ],\n" +
                    "      \"Resource\": [\n" +
                    "        \"arn:aws:s3:::%s/*\"\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}", bucket);
                
                client.setBucketPolicy(SetBucketPolicyArgs.builder()
                    .bucket(bucket)
                    .config(policy)
                    .build());
                
                System.out.println("MinIO桶创建成功: " + bucket);
            } else {
                System.out.println("MinIO桶已存在: " + bucket);
            }
        } catch (Exception e) {
            System.err.println("MinIO桶初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取桶名称
     */
    public String getBucket() {
        return bucket;
    }

    /**
     * 获取MinIO端点
     */
    public String getEndpoint() {
        return endpoint;
    }
}
