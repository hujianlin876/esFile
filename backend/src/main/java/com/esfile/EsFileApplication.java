package com.esfile;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// ES自动配置已通过spring-boot-starter-data-elasticsearch自动启用

/**
 * ES File Management System Application
 * 简化版本，暂时禁用一些自动配置
 */
@SpringBootApplication
@MapperScan("com.esfile.mapper")
public class EsFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsFileApplication.class, args);
        System.out.println("ES File Management System started successfully!");
        System.out.println("Application is running on port 8080");
    }
}


