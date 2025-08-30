package com.esfile.entity.mybatis;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色权限关联实体类
 * 
 * @author esfile
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RolePermission extends BaseEntity {
    
    /**
     * 角色ID
     */
    private Long roleId;
    
    /**
     * 权限ID
     */
    private Long permissionId;
}
