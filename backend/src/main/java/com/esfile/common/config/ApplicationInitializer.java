package com.esfile.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 应用初始化配置类
 * 用于系统启动后的初始化工作
 * 
 * @author esfile
 * @since 1.0.0
 */
@Component
public class ApplicationInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationInitializer.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        logger.info("ES文件管理系统启动完成，开始初始化...");
        
        try {
            // 初始化Redis连接测试
            initRedis();
            
            // 初始化系统配置
            initSystemConfig();
            
            logger.info("ES文件管理系统初始化完成！");
            logger.info("后端服务地址: http://localhost:8080");
            logger.info("API文档地址: http://localhost:8080/swagger-ui.html");
            logger.info("健康检查地址: http://localhost:8080/api/health");
            
        } catch (Exception e) {
            logger.error("系统初始化失败", e);
        }
    }

    /**
     * 初始化Redis连接
     */
    private void initRedis() {
        try {
            // 测试Redis连接
            redisTemplate.opsForValue().set("system_init", "ES文件管理系统已启动");
            String value = (String) redisTemplate.opsForValue().get("system_init");
            logger.info("Redis连接测试成功: {}", value);
        } catch (Exception e) {
            logger.warn("Redis连接测试失败: {}", e.getMessage());
        }
    }

    /**
     * 初始化系统配置
     */
    private void initSystemConfig() {
        try {
            // 设置系统启动时间
            redisTemplate.opsForValue().set("system:startup_time", System.currentTimeMillis());
            
            // 设置系统版本信息
            redisTemplate.opsForValue().set("system:version", "1.0.0");
            redisTemplate.opsForValue().set("system:name", "ES文件管理系统");
            
            logger.info("系统配置初始化完成");
        } catch (Exception e) {
            logger.warn("系统配置初始化失败: {}", e.getMessage());
        }
    }
}
