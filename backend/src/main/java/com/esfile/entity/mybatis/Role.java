package com.esfile.entity.mybatis;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色实体类
 * 
 * @author esfile
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 角色编码
     */
    private String roleCode;
    
    /**
     * 角色描述
     */
    private String description;
    
    /**
     * 角色状态：0-禁用，1-启用
     */
    private Integer status;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 是否系统角色：0-否，1-是
     */
    private Integer isSystem;
}
