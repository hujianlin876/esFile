package com.esfile.service.system;

import com.esfile.entity.mybatis.Role;
import java.util.List;
import java.util.Map;

/**
 * 角色管理服务接口
 */
public interface RoleService {

    /**
     * 获取角色列表
     */
    List<Role> getRoleList();

    /**
     * 根据ID获取角色
     */
    Role getRoleById(Long id);

    /**
     * 创建角色
     */
    Role createRole(Role role);

    /**
     * 更新角色
     */
    Role updateRole(Role role);

    /**
     * 删除角色
     */
    boolean deleteRole(Long id);

    /**
     * 批量删除角色
     */
    boolean batchDeleteRoles(List<Long> ids);

    /**
     * 获取角色权限
     */
    List<Long> getRolePermissions(Long roleId);

    /**
     * 分配角色权限
     */
    boolean assignPermissions(Long roleId, List<Long> permissionIds);

    /**
     * 获取角色用户列表
     */
    List<Map<String, Object>> getRoleUsers(Long roleId);

    /**
     * 检查角色编码是否存在
     */
    boolean isRoleCodeExists(String roleCode);
}
