package com.esfile.mapper;

import com.esfile.entity.mybatis.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限Mapper接口
 */
@Mapper
public interface PermissionMapper {

    /**
     * 查询所有权限
     */
    List<Permission> selectAll();

    /**
     * 根据ID查询权限
     */
    Permission selectById(Long id);

    /**
     * 根据权限编码查询权限
     */
    Permission selectByPermissionCode(String permissionCode);

    /**
     * 根据父级权限ID查询权限列表
     */
    List<Permission> selectByParentId(Long parentId);

    /**
     * 根据权限类型查询权限列表
     */
    List<Permission> selectByType(Integer permissionType);

    /**
     * 根据角色ID查询权限列表
     */
    List<Permission> selectByRoleId(Long roleId);

    /**
     * 根据用户ID查询权限编码列表
     */
    List<String> selectPermissionCodesByUserId(Long userId);

    /**
     * 插入权限
     */
    int insert(Permission permission);

    /**
     * 更新权限
     */
    int updateById(Permission permission);

    /**
     * 根据ID删除权限
     */
    int deleteById(Long id);

    /**
     * 批量删除权限
     */
    int batchDeleteByIds(List<Long> ids);

    /**
     * 删除角色权限关联
     */
    int deleteRolePermissions(Long permissionId);
}
