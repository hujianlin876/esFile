package com.esfile.mapper;

import com.esfile.entity.mybatis.Role;
import com.esfile.entity.mybatis.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 角色Mapper接口
 */
@Mapper
public interface RoleMapper {

    /**
     * 查询所有角色
     */
    List<Role> selectAll();

    /**
     * 根据ID查询角色
     */
    Role selectById(Long id);

    /**
     * 根据角色编码查询角色
     */
    Role selectByRoleCode(String roleCode);

    /**
     * 插入角色
     */
    int insert(Role role);

    /**
     * 更新角色
     */
    int updateById(Role role);

    /**
     * 根据ID删除角色
     */
    int deleteById(Long id);

    /**
     * 批量删除角色
     */
    int batchDeleteByIds(List<Long> ids);

    /**
     * 插入角色权限关联
     */
    int insertRolePermission(RolePermission rolePermission);

    /**
     * 删除角色权限关联
     */
    int deleteRolePermissions(Long roleId);

    /**
     * 删除用户角色关联
     */
    int deleteUserRoles(Long roleId);

    /**
     * 查询角色权限ID列表
     */
    List<Long> selectRolePermissionIds(Long roleId);

    /**
     * 查询角色用户列表
     */
    List<Map<String, Object>> selectRoleUsers(Long roleId);
}
