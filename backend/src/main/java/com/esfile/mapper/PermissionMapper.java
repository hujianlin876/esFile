package com.esfile.mapper;

import com.esfile.entity.mybatis.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限Mapper接口
 * 
 * @author esfile
 * @since 2024-01-01
 */
@Mapper
public interface PermissionMapper {
    
    /**
     * 插入权限
     * 
     * @param permission 权限信息
     * @return 影响行数
     */
    int insert(Permission permission);
    
    /**
     * 根据ID更新权限
     * 
     * @param permission 权限信息
     * @return 影响行数
     */
    int updateById(Permission permission);
    
    /**
     * 根据ID删除权限
     * 
     * @param id 权限ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据ID查询权限
     * 
     * @param id 权限ID
     * @return 权限信息
     */
    Permission selectById(@Param("id") Long id);
    
    /**
     * 根据权限编码查询权限
     * 
     * @param permissionCode 权限编码
     * @return 权限信息
     */
    Permission selectByPermissionCode(@Param("permissionCode") String permissionCode);
    
    /**
     * 查询所有权限
     * 
     * @return 权限列表
     */
    List<Permission> selectAll();
    
    /**
     * 分页查询权限
     * 
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 权限列表
     */
    List<Permission> selectPage(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 查询权限总数
     * 
     * @return 总数
     */
    long selectCount();
    
    /**
     * 根据父级权限ID查询权限列表
     * 
     * @param parentId 父级权限ID
     * @return 权限列表
     */
    List<Permission> selectByParentId(@Param("parentId") Long parentId);
    
    /**
     * 根据权限类型查询权限列表
     * 
     * @param permissionType 权限类型
     * @return 权限列表
     */
    List<Permission> selectByType(@Param("permissionType") Integer permissionType);
    
    /**
     * 根据角色ID查询权限列表
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> selectByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 根据用户ID查询权限列表
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Permission> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 查询权限树
     * 
     * @return 权限树
     */
    List<Permission> selectTree();
}
