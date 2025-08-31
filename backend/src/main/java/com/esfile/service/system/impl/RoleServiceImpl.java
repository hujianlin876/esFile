package com.esfile.service.system.impl;

import com.esfile.entity.mybatis.Role;
import com.esfile.entity.mybatis.RolePermission;
import com.esfile.mapper.RoleMapper;
import com.esfile.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 角色管理服务实现类
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRoleList() {
        return roleMapper.selectAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleMapper.selectById(id);
    }

    @Override
    @Transactional
    public Role createRole(Role role) {
        // 检查角色编码是否已存在
        if (isRoleCodeExists(role.getRoleCode())) {
            throw new RuntimeException("角色编码已存在");
        }
        
        // 设置默认值
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        role.setStatus(1);
        
        // 插入角色
        roleMapper.insert(role);
        return role;
    }

    @Override
    @Transactional
    public Role updateRole(Role role) {
        Role existingRole = getRoleById(role.getId());
        if (existingRole == null) {
            throw new RuntimeException("角色不存在");
        }
        
        // 检查角色编码是否被其他角色使用
        Role roleByCode = roleMapper.selectByRoleCode(role.getRoleCode());
        if (roleByCode != null && !roleByCode.getId().equals(role.getId())) {
            throw new RuntimeException("角色编码已存在");
        }
        
        // 设置更新时间
        role.setUpdateTime(LocalDateTime.now());
        
        // 更新角色
        roleMapper.updateById(role);
        return getRoleById(role.getId());
    }

    @Override
    @Transactional
    public boolean deleteRole(Long id) {
        // 删除角色权限关联
        roleMapper.deleteRolePermissions(id);
        
        // 删除用户角色关联
        roleMapper.deleteUserRoles(id);
        
        // 删除角色
        return roleMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional
    public boolean batchDeleteRoles(List<Long> ids) {
        // 批量删除角色权限关联
        for (Long id : ids) {
            roleMapper.deleteRolePermissions(id);
            roleMapper.deleteUserRoles(id);
        }
        
        // 批量删除角色
        return roleMapper.batchDeleteByIds(ids) > 0;
    }

    @Override
    public List<Long> getRolePermissions(Long roleId) {
        return roleMapper.selectRolePermissionIds(roleId);
    }

    @Override
    @Transactional
    public boolean assignPermissions(Long roleId, List<Long> permissionIds) {
        // 先删除原有权限关联
        roleMapper.deleteRolePermissions(roleId);
        
        // 添加新的权限关联
        if (permissionIds != null && !permissionIds.isEmpty()) {
            for (Long permissionId : permissionIds) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(permissionId);
                roleMapper.insertRolePermission(rolePermission);
            }
        }
        
        return true;
    }

    @Override
    public List<Map<String, Object>> getRoleUsers(Long roleId) {
        return roleMapper.selectRoleUsers(roleId);
    }

    @Override
    public boolean isRoleCodeExists(String roleCode) {
        return roleMapper.selectByRoleCode(roleCode) != null;
    }
}
