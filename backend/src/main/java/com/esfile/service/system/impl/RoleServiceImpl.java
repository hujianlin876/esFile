package com.esfile.service.system.impl;

import com.esfile.common.vo.PageResult;
import com.esfile.entity.mybatis.Role;
import com.esfile.mapper.RoleMapper;
import com.esfile.service.system.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色服务实现类
 * 
 * @author esfile
 * @since 2024-01-01
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRole(Role role) {
        try {
            // 检查角色编码是否已存在
            if (isRoleCodeExists(role.getRoleCode(), null)) {
                log.warn("角色编码已存在: {}", role.getRoleCode());
                return false;
            }
            
            // 设置默认值
            if (role.getStatus() == null) {
                role.setStatus(1);
            }
            if (role.getSort() == null) {
                role.setSort(0);
            }
            if (role.getIsSystem() == null) {
                role.setIsSystem(0);
            }
            
            int result = roleMapper.insert(role);
            return result > 0;
        } catch (Exception e) {
            log.error("创建角色失败", e);
            throw new RuntimeException("创建角色失败", e);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(Role role) {
        try {
            // 检查角色编码是否已存在（排除当前角色）
            if (isRoleCodeExists(role.getRoleCode(), role.getId())) {
                log.warn("角色编码已存在: {}", role.getRoleCode());
                return false;
            }
            
            int result = roleMapper.updateById(role);
            return result > 0;
        } catch (Exception e) {
            log.error("更新角色失败", e);
            throw new RuntimeException("更新角色失败", e);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Long id) {
        try {
            // 检查是否为系统角色
            Role role = roleMapper.selectById(id);
            if (role != null && role.getIsSystem() == 1) {
                log.warn("系统角色不能删除: {}", id);
                return false;
            }
            
            int result = roleMapper.deleteById(id);
            return result > 0;
        } catch (Exception e) {
            log.error("删除角色失败", e);
            throw new RuntimeException("删除角色失败", e);
        }
    }
    
    @Override
    public Role getRoleById(Long id) {
        try {
            return roleMapper.selectById(id);
        } catch (Exception e) {
            log.error("查询角色失败", e);
            throw new RuntimeException("查询角色失败", e);
        }
    }
    
    @Override
    public Role getRoleByCode(String roleCode) {
        try {
            return roleMapper.selectByRoleCode(roleCode);
        } catch (Exception e) {
            log.error("查询角色失败", e);
            throw new RuntimeException("查询角色失败", e);
        }
    }
    
    @Override
    public List<Role> getAllRoles() {
        try {
            return roleMapper.selectAll();
        } catch (Exception e) {
            log.error("查询所有角色失败", e);
            throw new RuntimeException("查询所有角色失败", e);
        }
    }
    
    @Override
    public PageResult<Role> getRolePage(int page, int size) {
        try {
            int offset = (page - 1) * size;
            List<Role> roles = roleMapper.selectPage(offset, size);
            long total = roleMapper.selectCount();
            
            PageResult<Role> result = new PageResult<>();
            result.setData(roles);
            result.setTotal(total);
            result.setCurrentPage(page);
            result.setPageSize(size);
            result.setTotalPages((int) Math.ceil((double) total / size));
            
            return result;
        } catch (Exception e) {
            log.error("分页查询角色失败", e);
            throw new RuntimeException("分页查询角色失败", e);
        }
    }
    
    @Override
    public List<Role> getRolesByUserId(Long userId) {
        try {
            return roleMapper.selectByUserId(userId);
        } catch (Exception e) {
            log.error("查询用户角色失败", e);
            throw new RuntimeException("查询用户角色失败", e);
        }
    }
    
    @Override
    public List<Role> getRolesByStatus(Integer status) {
        try {
            return roleMapper.selectByStatus(status);
        } catch (Exception e) {
            log.error("查询角色失败", e);
            throw new RuntimeException("查询角色失败", e);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignPermissions(Long roleId, List<Long> permissionIds) {
        try {
            // TODO: 实现角色权限分配逻辑
            log.info("分配角色权限: roleId={}, permissionIds={}", roleId, permissionIds);
            return true;
        } catch (Exception e) {
            log.error("分配角色权限失败", e);
            throw new RuntimeException("分配角色权限失败", e);
        }
    }
    
    @Override
    public boolean isRoleCodeExists(String roleCode, Long excludeId) {
        try {
            Role existingRole = roleMapper.selectByRoleCode(roleCode);
            if (existingRole == null) {
                return false;
            }
            return !existingRole.getId().equals(excludeId);
        } catch (Exception e) {
            log.error("检查角色编码是否存在失败", e);
            throw new RuntimeException("检查角色编码是否存在失败", e);
        }
    }
}
