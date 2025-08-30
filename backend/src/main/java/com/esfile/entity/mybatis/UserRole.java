package com.esfile.entity.mybatis;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户角色关联实体类
 * 
 * @author esfile
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRole extends BaseEntity {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 角色ID
     */
    private Long roleId;
}
