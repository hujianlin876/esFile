package com.esfile.entity.mybatis;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统配置实体类
 * 
 * @author esfile
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemConfig extends BaseEntity {
    
    /**
     * 配置键
     */
    private String configKey;
    
    /**
     * 配置值
     */
    private String configValue;
    
    /**
     * 配置描述
     */
    private String description;
    
    /**
     * 配置类型：1-系统，2-业务，3-用户
     */
    private Integer configType;
    
    /**
     * 是否可编辑：0-否，1-是
     */
    private Integer isEditable;
    
    /**
     * 排序
     */
    private Integer sort;
}
