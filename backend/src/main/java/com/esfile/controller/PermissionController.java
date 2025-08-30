package com.esfile.controller;

import com.esfile.common.vo.ResponseResult;
import com.esfile.entity.mybatis.Permission;
import com.esfile.service.system.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 权限管理控制器
 * 提供完整的权限管理API接口
 */
@RestController
@RequestMapping("/api/system/permissions")
public class PermissionController {
    
    @Autowired
    private PermissionService permissionService;

    /**
     * 获取权限列表
     */
    @GetMapping
    public ResponseResult<List<Permission>> listPermissions() {
        try {
            List<Permission> permissions = permissionService.getPermissionList();
            return ResponseResult.success(permissions);
        } catch (Exception e) {
            return ResponseResult.fail("获取权限列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取权限详情
     */
    @GetMapping("/{id}")
    public ResponseResult<Permission> getPermissionDetail(@PathVariable Long id) {
        try {
            Permission permission = permissionService.getPermissionById(id);
            if (permission == null) {
                return ResponseResult.fail("权限不存在");
            }
            return ResponseResult.success(permission);
        } catch (Exception e) {
            return ResponseResult.fail("获取权限详情失败: " + e.getMessage());
        }
    }

    /**
     * 创建权限
     */
    @PostMapping
    public ResponseResult<Permission> createPermission(@RequestBody Permission permission) {
        try {
            Permission createdPermission = permissionService.createPermission(permission);
            return ResponseResult.success(createdPermission, "权限创建成功");
        } catch (Exception e) {
            return ResponseResult.fail("权限创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新权限
     */
    @PutMapping("/{id}")
    public ResponseResult<Permission> updatePermission(@PathVariable Long id, @RequestBody Permission permission) {
        try {
            permission.setId(id);
            Permission updatedPermission = permissionService.updatePermission(permission);
            return ResponseResult.success(updatedPermission, "权限更新成功");
        } catch (Exception e) {
            return ResponseResult.fail("权限更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{id}")
    public ResponseResult<Boolean> deletePermission(@PathVariable Long id) {
        try {
            boolean result = permissionService.deletePermission(id);
            if (result) {
                return ResponseResult.success(true, "权限删除成功");
            } else {
                return ResponseResult.fail("权限删除失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("权限删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除权限
     */
    @PostMapping("/batch-delete")
    public ResponseResult<Boolean> batchDeletePermissions(@RequestBody List<Long> ids) {
        try {
            boolean result = permissionService.batchDeletePermissions(ids);
            if (result) {
                return ResponseResult.success(true, "批量删除成功");
            } else {
                return ResponseResult.fail("批量删除失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("批量删除失败: " + e.getMessage());
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
     * 获取子权限
     */
    @GetMapping("/{id}/children")
    public ResponseResult<List<Permission>> getChildPermissions(@PathVariable Long id) {
        try {
            // TODO: 实现获取子权限逻辑
            return ResponseResult.success(null);
        } catch (Exception e) {
            return ResponseResult.fail("获取子权限失败: " + e.getMessage());
        }
    }

    /**
     * 获取权限路径
     */
    @GetMapping("/{id}/path")
    public ResponseResult<List<Permission>> getPermissionPath(@PathVariable Long id) {
        try {
            // TODO: 实现获取权限路径逻辑
            return ResponseResult.success(null);
        } catch (Exception e) {
            return ResponseResult.fail("获取权限路径失败: " + e.getMessage());
        }
    }

    /**
     * 检查权限编码是否存在
     */
    @GetMapping("/check-code")
    public ResponseResult<Boolean> checkPermissionCode(@RequestParam String permissionCode) {
        try {
            boolean exists = permissionService.isPermissionCodeExists(permissionCode);
            return ResponseResult.success(exists);
        } catch (Exception e) {
            return ResponseResult.fail("检查权限编码失败: " + e.getMessage());
        }
    }

    /**
     * 获取权限统计信息
     */
    @GetMapping("/stats")
    public ResponseResult<Map<String, Object>> getPermissionStats() {
        try {
            // TODO: 实现获取权限统计逻辑
            return ResponseResult.success(null);
        } catch (Exception e) {
            return ResponseResult.fail("获取权限统计失败: " + e.getMessage());
        }
    }
}
