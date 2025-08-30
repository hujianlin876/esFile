package com.esfile.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger配置类
 * 配置API文档生成、分组等
 * 
 * @author esfile
 * @since 1.0.0
 */
@Configuration
public class SwaggerConfig {

    /**
     * 创建API文档分组
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.esfile.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * API信息配置
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("ES文件管理系统API文档")
                .description("基于Spring Boot + Vue3的现代化文件管理系统，集成Elasticsearch、MinIO、Redis、MySQL等组件")
                .version("1.0.0")
                .contact(new Contact("ES文件管理系统", "https://github.com/esfile", "admin@esfile.com"))
                .license("Apache 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }
}
