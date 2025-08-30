package com.esfile.service.system;

import com.esfile.common.vo.PageResult;
import com.esfile.entity.mybatis.Role;

import java.util.List;

/**
 * 角色服务接口
 * 
 * @author esfile
 * @since 2024-01-01
 */
public interface RoleService {
    
    /**
     * 创建角色
     * 
     * @param role 角色信息
     * @return 是否成功
     */
    boolean createRole(Role role);
    
    /**
     * 更新角色
     * 
     * @param role 角色信息
     * @return 是否成功
     */
    boolean updateRole(Role role);
    
    /**
     * 删除角色
     * 
     * @param id 角色ID
     * @return 是否成功
     */
    boolean deleteRole(Long id);
    
    /**
     * 根据ID查询角色
     * 
     * @param id 角色ID
     * @return 角色信息
     */
    Role getRoleById(Long id);
    
    /**
     * 根据角色编码查询角色
     * 
     * @param roleCode 角色编码
     * @return 角色信息
     */
    Role getRoleByCode(String roleCode);
    
    /**
     * 查询所有角色
     * 
     * @return 角色列表
     */
    List<Role> getAllRoles();
    
    /**
     * 分页查询角色
     * 
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    PageResult<Role> getRolePage(int page, int size);
    
    /**
     * 根据用户ID查询角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> getRolesByUserId(Long userId);
    
    /**
     * 根据状态查询角色列表
     * 
     * @param status 状态
     * @return 角色列表
     */
    List<Role> getRolesByStatus(Integer status);
    
    /**
     * 分配角色权限
     * 
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return 是否成功
     */
    boolean assignPermissions(Long roleId, List<Long> permissionIds);
    
    /**
     * 检查角色编码是否存在
     * 
     * @param roleCode 角色编码
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    boolean isRoleCodeExists(String roleCode, Long excludeId);
}
