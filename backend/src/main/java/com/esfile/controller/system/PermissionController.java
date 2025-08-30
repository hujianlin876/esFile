package com.esfile.controller.system;

import com.esfile.common.vo.ResponseResult;
import com.esfile.entity.mybatis.Role;
import com.esfile.entity.mybatis.Permission;
import com.esfile.service.system.RoleService;
import com.esfile.service.system.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 权限管理控制器
 */
@RestController
@RequestMapping("/api/system/permissions")
public class PermissionController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 获取角色列表
     */
    @GetMapping("/roles")
    public ResponseResult<List<Role>> getRoleList() {
        try {
            List<Role> roles = roleService.getRoleList();
            return ResponseResult.success(roles);
        } catch (Exception e) {
            return ResponseResult.fail("获取角色列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取权限列表
     */
    @GetMapping
    public ResponseResult<List<Permission>> getPermissionList() {
        try {
            List<Permission> permissions = permissionService.getPermissionList();
            return ResponseResult.success(permissions);
        } catch (Exception e) {
            return ResponseResult.fail("获取权限列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取权限树结构
     */
    @GetMapping("/tree")
    public ResponseResult<List<Map<String, Object>>> getPermissionTree() {
        try {
            List<Map<String, Object>> tree = permissionService.getPermissionTree();
            return ResponseResult.success(tree);
        } catch (Exception e) {
            return ResponseResult.fail("获取权限树失败: " + e.getMessage());
        }
    }

    /**
     * 分配角色权限
     */
    @PostMapping("/roles/{roleId}/permissions")
    public ResponseResult<String> assignRolePermissions(
            @PathVariable Long roleId,
            @RequestBody List<Long> permissionIds) {
        try {
            boolean success = roleService.assignPermissions(roleId, permissionIds);
            if (success) {
                return ResponseResult.success("权限分配成功");
            } else {
                return ResponseResult.fail("权限分配失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("权限分配失败: " + e.getMessage());
        }
    }

    /**
     * 获取角色权限
     */
    @GetMapping("/roles/{roleId}/permissions")
    public ResponseResult<List<Long>> getRolePermissions(@PathVariable Long roleId) {
        try {
            List<Long> permissionIds = roleService.getRolePermissions(roleId);
            return ResponseResult.success(permissionIds);
        } catch (Exception e) {
            return ResponseResult.fail("获取角色权限失败: " + e.getMessage());
        }
    }
}
