package com.esfile.service.system.impl;

import com.esfile.entity.mybatis.Permission;
import com.esfile.mapper.PermissionMapper;
import com.esfile.service.system.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限管理服务实现类
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> getPermissionList() {
        return permissionMapper.selectAll();
    }

    @Override
    public Permission getPermissionById(Long id) {
        return permissionMapper.selectById(id);
    }

    @Override
    @Transactional
    public Permission createPermission(Permission permission) {
        // 检查权限编码是否已存在
        if (isPermissionCodeExists(permission.getPermissionCode())) {
            throw new RuntimeException("权限编码已存在");
        }
        
        // 设置默认值
        permission.setCreateTime(LocalDateTime.now());
        permission.setUpdateTime(LocalDateTime.now());
        permission.setStatus(1);
        permission.setSort(0);
        
        // 插入权限
        permissionMapper.insert(permission);
        return permission;
    }

    @Override
    @Transactional
    public Permission updatePermission(Permission permission) {
        Permission existingPermission = getPermissionById(permission.getId());
        if (existingPermission == null) {
            throw new RuntimeException("权限不存在");
        }
        
        // 检查权限编码是否被其他权限使用
        Permission permissionByCode = permissionMapper.selectByPermissionCode(permission.getPermissionCode());
        if (permissionByCode != null && !permissionByCode.getId().equals(permission.getId())) {
            throw new RuntimeException("权限编码已存在");
        }
        
        // 设置更新时间
        permission.setUpdateTime(LocalDateTime.now());
        
        // 更新权限
        permissionMapper.updateById(permission);
        return getPermissionById(permission.getId());
    }

    @Override
    @Transactional
    public boolean deletePermission(Long id) {
        // 检查是否有子权限
        List<Permission> children = permissionMapper.selectByParentId(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("存在子权限，无法删除");
        }
        
        // 删除角色权限关联
        permissionMapper.deleteRolePermissions(id);
        
        // 删除权限
        return permissionMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional
    public boolean batchDeletePermissions(List<Long> ids) {
        // 检查是否有子权限
        for (Long id : ids) {
            List<Permission> children = permissionMapper.selectByParentId(id);
            if (!children.isEmpty()) {
                throw new RuntimeException("权限ID " + id + " 存在子权限，无法删除");
            }
        }
        
        // 批量删除角色权限关联
        for (Long id : ids) {
            permissionMapper.deleteRolePermissions(id);
        }
        
        // 批量删除权限
        return permissionMapper.batchDeleteByIds(ids) > 0;
    }

    @Override
    public List<Map<String, Object>> getPermissionTree() {
        List<Permission> allPermissions = getPermissionList();
        
        // 构建权限树
        Map<Long, List<Permission>> parentChildMap = allPermissions.stream()
                .collect(Collectors.groupingBy(p -> p.getParentId() != null ? p.getParentId() : 0L));
        
        List<Map<String, Object>> tree = new ArrayList<>();
        
        // 获取根权限
        List<Permission> rootPermissions = parentChildMap.get(0L);
        if (rootPermissions != null) {
            for (Permission root : rootPermissions) {
                Map<String, Object> node = buildPermissionNode(root, parentChildMap);
                tree.add(node);
            }
        }
        
        return tree;
    }

    private Map<String, Object> buildPermissionNode(Permission permission, Map<Long, List<Permission>> parentChildMap) {
        Map<String, Object> node = new HashMap<>();
        node.put("id", permission.getId());
        node.put("name", permission.getPermissionName());
        node.put("code", permission.getPermissionCode());
        node.put("type", permission.getPermissionType());
        node.put("url", permission.getPermissionPath());
        node.put("icon", permission.getPermissionIcon());
        node.put("sort", permission.getSort());
        node.put("status", permission.getStatus());
        
        // 递归构建子节点
        List<Permission> children = parentChildMap.get(permission.getId());
        if (children != null && !children.isEmpty()) {
            List<Map<String, Object>> childNodes = new ArrayList<>();
            for (Permission child : children) {
                Map<String, Object> childNode = buildPermissionNode(child, parentChildMap);
                childNodes.add(childNode);
            }
            node.put("children", childNodes);
        }
        
        return node;
    }

    @Override
    public List<Permission> getPermissionsByRoleId(Long roleId) {
        return permissionMapper.selectByRoleId(roleId);
    }

    @Override
    public List<String> getPermissionsByUserId(Long userId) {
        return permissionMapper.selectPermissionCodesByUserId(userId);
    }

    @Override
    public boolean isPermissionCodeExists(String permissionCode) {
        return permissionMapper.selectByPermissionCode(permissionCode) != null;
    }

    @Override
    public List<Map<String, Object>> getMenuPermissions() {
        // 获取菜单类型的权限
        List<Permission> menuPermissions = permissionMapper.selectByType(1); // 1表示菜单类型
        
        // 构建菜单树
        Map<Long, List<Permission>> parentChildMap = menuPermissions.stream()
                .collect(Collectors.groupingBy(p -> p.getParentId() != null ? p.getParentId() : 0L));
        
        List<Map<String, Object>> menuTree = new ArrayList<>();
        
        // 获取根菜单
        List<Permission> rootMenus = parentChildMap.get(0L);
        if (rootMenus != null) {
            for (Permission root : rootMenus) {
                Map<String, Object> menuNode = buildMenuNode(root, parentChildMap);
                menuTree.add(menuNode);
            }
        }
        
        return menuTree;
    }

    private Map<String, Object> buildMenuNode(Permission permission, Map<Long, List<Permission>> parentChildMap) {
        Map<String, Object> node = new HashMap<>();
        node.put("id", permission.getId());
        node.put("title", permission.getPermissionName());
        node.put("path", permission.getPermissionPath());
        node.put("icon", permission.getPermissionIcon());
        node.put("sort", permission.getSort());
        node.put("status", permission.getStatus());
        
        // 递归构建子菜单
        List<Permission> children = parentChildMap.get(permission.getId());
        if (children != null && !children.isEmpty()) {
            List<Map<String, Object>> childNodes = new ArrayList<>();
            for (Permission child : children) {
                Map<String, Object> childNode = buildMenuNode(child, parentChildMap);
                childNodes.add(childNode);
            }
            node.put("children", childNodes);
        }
        
        return node;
    }
}
