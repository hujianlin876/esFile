package com.esfile.service.system;

import com.esfile.entity.mybatis.Permission;
import java.util.List;
import java.util.Map;

/**
 * 权限管理服务接口
 */
public interface PermissionService {

    /**
     * 获取权限列表
     */
    List<Permission> getPermissionList();

    /**
     * 根据ID获取权限
     */
    Permission getPermissionById(Long id);

    /**
     * 创建权限
     */
    Permission createPermission(Permission permission);

    /**
     * 更新权限
     */
    Permission updatePermission(Permission permission);

    /**
     * 删除权限
     */
    boolean deletePermission(Long id);

    /**
     * 批量删除权限
     */
    boolean batchDeletePermissions(List<Long> ids);

    /**
     * 获取权限树结构
     */
    List<Map<String, Object>> getPermissionTree();

    /**
     * 根据角色ID获取权限列表
     */
    List<Permission> getPermissionsByRoleId(Long roleId);

    /**
     * 根据用户ID获取权限列表
     */
    List<String> getPermissionsByUserId(Long userId);

    /**
     * 检查权限编码是否存在
     */
    boolean isPermissionCodeExists(String permissionCode);

    /**
     * 获取菜单权限
     */
    List<Map<String, Object>> getMenuPermissions();
}
