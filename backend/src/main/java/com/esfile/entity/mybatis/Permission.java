package com.esfile.entity.mybatis;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权限实体类
 * 
 * @author esfile
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Permission extends BaseEntity {
    
    /**
     * 权限名称
     */
    private String permissionName;
    
    /**
     * 权限编码
     */
    private String permissionCode;
    
    /**
     * 权限类型：1-菜单，2-按钮，3-接口
     */
    private Integer permissionType;
    
    /**
     * 权限路径
     */
    private String permissionPath;
    
    /**
     * 权限描述
     */
    private String description;
    
    /**
     * 父级权限ID
     */
    private Long parentId;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 权限状态：0-禁用，1-启用
     */
    private Integer status;
    
    /**
     * 图标
     */
    private String icon;
    
    /**
     * 组件路径
     */
    private String component;
    
    /**
     * 是否外链：0-否，1-是
     */
    private Integer isExternal;
}
