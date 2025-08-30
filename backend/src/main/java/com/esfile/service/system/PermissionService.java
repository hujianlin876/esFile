package com.esfile.service.system;

import com.esfile.common.vo.PageResult;
import com.esfile.entity.mybatis.Permission;

import java.util.List;

/**
 * 权限服务接口
 * 
 * @author esfile
 * @since 2024-01-01
 */
public interface PermissionService {
    
    /**
     * 创建权限
     * 
     * @param permission 权限信息
     * @return 是否成功
     */
    boolean createPermission(Permission permission);
    
    /**
     * 更新权限
     * 
     * @param permission 权限信息
     * @return 是否成功
     */
    boolean updatePermission(Permission permission);
    
    /**
     * 删除权限
     * 
     * @param id 权限ID
     * @return 是否成功
     */
    boolean deletePermission(Long id);
    
    /**
     * 根据ID查询权限
     * 
     * @param id 权限ID
     * @return 权限信息
     */
    Permission getPermissionById(Long id);
    
    /**
     * 根据权限编码查询权限
     * 
     * @param permissionCode 权限编码
     * @return 权限信息
     */
    Permission getPermissionByCode(String permissionCode);
    
    /**
     * 查询所有权限
     * 
     * @return 权限列表
     */
    List<Permission> getAllPermissions();
    
    /**
     * 分页查询权限
     * 
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    PageResult<Permission> getPermissionPage(int page, int size);
    
    /**
     * 根据父级权限ID查询权限列表
     * 
     * @param parentId 父级权限ID
     * @return 权限列表
     */
    List<Permission> getPermissionsByParentId(Long parentId);
    
    /**
     * 根据权限类型查询权限列表
     * 
     * @param permissionType 权限类型
     * @return 权限列表
     */
    List<Permission> getPermissionsByType(Integer permissionType);
    
    /**
     * 根据角色ID查询权限列表
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> getPermissionsByRoleId(Long roleId);
    
    /**
     * 根据用户ID查询权限列表
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Permission> getPermissionsByUserId(Long userId);
    
    /**
     * 查询权限树
     * 
     * @return 权限树
     */
    List<Permission> getPermissionTree();
    
    /**
     * 检查权限编码是否存在
     * 
     * @param permissionCode 权限编码
     * @param excludeId 排除的权限ID
     * @return 是否存在
     */
    boolean isPermissionCodeExists(String permissionCode, Long excludeId);
    
    /**
     * 检查用户是否有指定权限
     * 
     * @param userId 用户ID
     * @param permissionCode 权限编码
     * @return 是否有权限
     */
    boolean hasPermission(Long userId, String permissionCode);
}
