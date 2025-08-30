package com.esfile.controller;

import com.esfile.common.vo.ResponseResult;
import com.esfile.entity.mybatis.User;
import com.esfile.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 * 提供完整的用户管理API接口
 */
@RestController
@RequestMapping("/api/system/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    /**
     * 分页查询用户列表
     */
    @GetMapping
    public ResponseResult<Map<String, Object>> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String dept) {
        
        try {
            Map<String, Object> result = userService.getUserList(page, size, keyword, status, role, dept);
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.fail("获取用户列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    public ResponseResult<User> getUserDetail(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                return ResponseResult.fail("用户不存在");
            }
            return ResponseResult.success(user);
        } catch (Exception e) {
            return ResponseResult.fail("获取用户详情失败: " + e.getMessage());
        }
    }

    /**
     * 创建用户
     */
    @PostMapping
    public ResponseResult<User> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseResult.success(createdUser, "用户创建成功");
        } catch (Exception e) {
            return ResponseResult.fail("用户创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public ResponseResult<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            user.setId(id);
            User updatedUser = userService.updateUser(user);
            return ResponseResult.success(updatedUser, "用户更新成功");
        } catch (Exception e) {
            return ResponseResult.fail("用户更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseResult<Boolean> deleteUser(@PathVariable Long id) {
        try {
            boolean result = userService.deleteUser(id);
            if (result) {
                return ResponseResult.success(true, "用户删除成功");
            } else {
                return ResponseResult.fail("用户删除失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("用户删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除用户
     */
    @PostMapping("/batch-delete")
    public ResponseResult<Boolean> batchDeleteUsers(@RequestBody List<Long> ids) {
        try {
            boolean result = userService.batchDeleteUsers(ids);
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
     * 重置用户密码
     */
    @PostMapping("/{id}/reset-password")
    public ResponseResult<Boolean> resetPassword(@PathVariable Long id) {
        try {
            boolean result = userService.resetPassword(id);
            if (result) {
                return ResponseResult.success(true, "密码重置成功");
            } else {
                return ResponseResult.fail("密码重置失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("密码重置失败: " + e.getMessage());
        }
    }

    /**
     * 修改用户密码
     */
    @PostMapping("/{id}/change-password")
    public ResponseResult<Boolean> changePassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> passwordData) {
        
        try {
            String oldPassword = passwordData.get("oldPassword");
            String newPassword = passwordData.get("newPassword");
            
            if (oldPassword == null || newPassword == null) {
                return ResponseResult.fail("旧密码和新密码不能为空");
            }
            
            boolean result = userService.changePassword(id, oldPassword, newPassword);
            if (result) {
                return ResponseResult.success(true, "密码修改成功");
            } else {
                return ResponseResult.fail("密码修改失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("密码修改失败: " + e.getMessage());
        }
    }

    /**
     * 切换用户状态
     */
    @PostMapping("/{id}/toggle-status")
    public ResponseResult<Boolean> toggleUserStatus(@PathVariable Long id) {
        try {
            boolean result = userService.toggleStatus(id);
            if (result) {
                return ResponseResult.success(true, "状态切换成功");
            } else {
                return ResponseResult.fail("状态切换失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("状态切换失败: " + e.getMessage());
        }
    }

    /**
     * 批量更新用户状态
     */
    @PostMapping("/batch-update-status")
    public ResponseResult<Boolean> batchUpdateUserStatus(
            @RequestBody List<Long> ids,
            @RequestParam String status) {
        
        try {
            boolean result = userService.batchUpdateStatus(ids, status);
            if (result) {
                return ResponseResult.success(true, "批量状态更新成功");
            } else {
                return ResponseResult.fail("批量状态更新失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("批量状态更新失败: " + e.getMessage());
        }
    }

    /**
     * 分配用户角色
     */
    @PostMapping("/{id}/assign-roles")
    public ResponseResult<Boolean> assignRoles(
            @PathVariable Long id,
            @RequestBody List<Long> roleIds) {
        
        try {
            boolean result = userService.assignRoles(id, roleIds);
            if (result) {
                return ResponseResult.success(true, "角色分配成功");
            } else {
                return ResponseResult.fail("角色分配失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("角色分配失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户角色
     */
    @GetMapping("/{id}/roles")
    public ResponseResult<List<Map<String, Object>>> getUserRoles(@PathVariable Long id) {
        try {
            List<Map<String, Object>> roles = userService.getUserRoles(id);
            return ResponseResult.success(roles);
        } catch (Exception e) {
            return ResponseResult.fail("获取用户角色失败: " + e.getMessage());
        }
    }

    /**
     * 导入用户
     */
    @PostMapping("/import")
    public ResponseResult<Map<String, Object>> importUsers(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> result = userService.importUsers(file);
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.fail("用户导入失败: " + e.getMessage());
        }
    }

    /**
     * 导出用户
     */
    @GetMapping("/export")
    public void exportUsers(
            @RequestParam(required = false) String format,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long roleId,
            HttpServletResponse response) {
        
        try {
            userService.exportUsers(format, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 获取用户统计信息
     */
    @GetMapping("/stats")
    public ResponseResult<Map<String, Object>> getUserStats() {
        try {
            Map<String, Object> stats = userService.getUserStats();
            return ResponseResult.success(stats);
        } catch (Exception e) {
            return ResponseResult.fail("获取用户统计失败: " + e.getMessage());
        }
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check-username")
    public ResponseResult<Boolean> checkUsername(@RequestParam String username) {
        try {
            boolean exists = userService.isUsernameExists(username);
            return ResponseResult.success(exists);
        } catch (Exception e) {
            return ResponseResult.fail("检查用户名失败: " + e.getMessage());
        }
    }

    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/check-email")
    public ResponseResult<Boolean> checkEmail(@RequestParam String email) {
        try {
            boolean exists = userService.isEmailExists(email);
            return ResponseResult.success(exists);
        } catch (Exception e) {
            return ResponseResult.fail("检查邮箱失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户权限列表
     */
    @GetMapping("/{id}/permissions")
    public ResponseResult<List<String>> getUserPermissions(@PathVariable Long id) {
        try {
            List<String> permissions = userService.getUserPermissions(id);
            return ResponseResult.success(permissions);
        } catch (Exception e) {
            return ResponseResult.fail("获取用户权限失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户角色名称列表
     */
    @GetMapping("/{id}/role-names")
    public ResponseResult<List<String>> getUserRoleNames(@PathVariable Long id) {
        try {
            List<String> roleNames = userService.getUserRoleNames(id);
            return ResponseResult.success(roleNames);
        } catch (Exception e) {
            return ResponseResult.fail("获取用户角色名称失败: " + e.getMessage());
        }
    }
}
