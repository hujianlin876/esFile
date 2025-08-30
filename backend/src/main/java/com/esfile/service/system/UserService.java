package com.esfile.service.system;

import com.esfile.entity.mybatis.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 用户管理服务接口
 */
public interface UserService {

    /**
     * 分页查询用户列表
     */
    Map<String, Object> getUserList(int page, int size, String keyword, String status, String role, String dept);

    /**
     * 根据ID获取用户
     */
    User getUserById(Long id);

    /**
     * 根据用户名获取用户
     */
    User getUserByUsername(String username);

    /**
     * 根据邮箱获取用户
     */
    User getUserByEmail(String email);

    /**
     * 创建用户
     */
    User createUser(User user);

    /**
     * 更新用户
     */
    User updateUser(User user);

    /**
     * 删除用户
     */
    boolean deleteUser(Long id);

    /**
     * 批量删除用户
     */
    boolean batchDeleteUsers(List<Long> ids);

    /**
     * 重置用户密码
     */
    boolean resetPassword(Long id);

    /**
     * 修改用户密码
     */
    boolean changePassword(Long id, String oldPassword, String newPassword);

    /**
     * 切换用户状态
     */
    boolean toggleStatus(Long id);

    /**
     * 批量更新用户状态
     */
    boolean batchUpdateStatus(List<Long> ids, String status);

    /**
     * 分配用户角色
     */
    boolean assignRoles(Long userId, List<Long> roleIds);

    /**
     * 获取用户角色
     */
    List<Map<String, Object>> getUserRoles(Long userId);

    /**
     * 导入用户
     */
    Map<String, Object> importUsers(MultipartFile file);

    /**
     * 导出用户
     */
    void exportUsers(String format, HttpServletResponse response);

    /**
     * 获取用户统计信息
     */
    Map<String, Object> getUserStats();

    /**
     * 检查用户名是否存在
     */
    boolean isUsernameExists(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean isEmailExists(String email);

    /**
     * 获取用户权限列表
     */
    List<String> getUserPermissions(Long userId);

    /**
     * 获取用户角色列表
     */
    List<String> getUserRoleNames(Long userId);
}

