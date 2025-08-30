package com.esfile;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * ES文件管理系统主启动类
 * 
 * @author esfile
 * @since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.esfile.mapper")
@EnableCaching
@EnableAsync
public class EsFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsFileApplication.class, args);
        System.out.println("ES文件管理系统启动成功！");
        System.out.println("后端服务地址: http://localhost:8080");
        System.out.println("API文档地址: http://localhost:8080/swagger-ui.html");
    }
}


