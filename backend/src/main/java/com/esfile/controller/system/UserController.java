package com.esfile.controller.system;

import com.esfile.common.util.ControllerUtil;
import com.esfile.common.vo.ResponseResult;
import com.esfile.entity.mybatis.User;
import com.esfile.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/system/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户列表（分页）
     */
    @GetMapping
    public ResponseResult<Map<String, Object>> getUserList(
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
            if (user != null) {
                return ResponseResult.success(user);
            } else {
                return ResponseResult.fail("用户不存在");
            }
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
            return ResponseResult.fail("创建用户失败: " + e.getMessage());
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
            return ResponseResult.fail("更新用户失败: " + e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseResult<String> deleteUser(@PathVariable Long id) {
        try {
            boolean success = userService.deleteUser(id);
            return ControllerUtil.createOperationResult(success, "用户删除成功", "用户删除失败");
        } catch (Exception e) {
            return ControllerUtil.handleException(e, "删除用户");
        }
    }

    /**
     * 批量删除用户
     */
    @PostMapping("/batch-delete")
    public ResponseResult<String> batchDeleteUsers(@RequestBody List<Long> ids) {
        try {
            boolean success = userService.batchDeleteUsers(ids);
            return ControllerUtil.createOperationResult(success, "批量删除成功", "批量删除失败");
        } catch (Exception e) {
            return ControllerUtil.handleException(e, "批量删除用户");
        }
    }

    /**
     * 重置用户密码
     */
    @PostMapping("/{id}/reset-password")
    public ResponseResult<String> resetUserPassword(@PathVariable Long id) {
        try {
            boolean success = userService.resetPassword(id);
            return ControllerUtil.createOperationResult(success, "密码重置成功", "密码重置失败");
        } catch (Exception e) {
            return ControllerUtil.handleException(e, "重置用户密码");
        }
    }

    /**
     * 修改用户密码
     */
    @PostMapping("/{id}/change-password")
    public ResponseResult<String> changeUserPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> passwordInfo) {
        try {
            String oldPassword = passwordInfo.get("oldPassword");
            String newPassword = passwordInfo.get("newPassword");
            
            boolean success = userService.changePassword(id, oldPassword, newPassword);
            return ControllerUtil.createOperationResult(success, "密码修改成功", "密码修改失败");
        } catch (Exception e) {
            return ResponseResult.fail("修改密码失败: " + e.getMessage());
        }
    }

    /**
     * 切换用户状态
     */
    @PostMapping("/{id}/toggle-status")
    public ResponseResult<String> toggleUserStatus(@PathVariable Long id) {
        try {
            boolean success = userService.toggleStatus(id);
            return ControllerUtil.createOperationResult(success, "用户状态切换成功", "用户状态切换失败");
        } catch (Exception e) {
            return ControllerUtil.handleException(e, "切换用户状态");
        }
    }

    /**
     * 批量更新用户状态
     */
    @PostMapping("/batch-update-status")
    public ResponseResult<String> batchUpdateUserStatus(
            @RequestBody Map<String, Object> request) {
        try {
            List<Long> ids = (List<Long>) request.get("ids");
            String status = (String) request.get("status");
            
            boolean success = userService.batchUpdateStatus(ids, status);
            return ControllerUtil.createOperationResult(success, "批量更新状态成功", "批量更新状态失败");
        } catch (Exception e) {
            return ControllerUtil.handleException(e, "批量更新用户状态");
        }
    }

    /**
     * 分配用户角色
     */
    @PostMapping("/{id}/assign-roles")
    public ResponseResult<String> assignUserRoles(
            @PathVariable Long id,
            @RequestBody List<Long> roleIds) {
        try {
            boolean success = userService.assignRoles(id, roleIds);
            return ControllerUtil.createOperationResult(success, "角色分配成功", "角色分配失败");
        } catch (Exception e) {
            return ControllerUtil.handleException(e, "分配用户角色");
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
    public ResponseResult<Map<String, Object>> importUsers(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            Map<String, Object> result = userService.importUsers(file);
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.fail("导入用户失败: " + e.getMessage());
        }
    }

    /**
     * 导出用户
     */
    @GetMapping("/export")
    public void exportUsers(
            @RequestParam(required = false) String format,
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
}
