package com.esfile.service.system.impl;

import com.esfile.entity.mybatis.User;
import com.esfile.entity.mybatis.UserRole;
import com.esfile.mapper.UserMapper;
import com.esfile.service.system.UserService;
import com.esfile.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户管理服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> getUserList(int page, int size, String keyword, String status, String role, String dept) {
        // 计算偏移量
        int offset = (page - 1) * size;
        
        // 查询用户列表
        List<User> users = userMapper.selectUserList(offset, size, keyword, status, role, dept);
        
        // 查询总数
        long total = userMapper.countUsers(keyword, status, role, dept);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", users);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("pages", (total + size - 1) / size);
        
        return result;
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    @Override
    @Transactional
    public User createUser(User user) {
        // 检查用户名和邮箱是否已存在
        if (isUsernameExists(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (isEmailExists(user.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 设置默认值
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setStatus("ACTIVE");
        user.setLoginFailCount(0);
        user.setLastLoginTime(null);
        user.setLastLoginIp(null);
        
        // 加密密码
        if (user.getPassword() != null) {
            user.setPassword(PasswordUtil.encrypt(user.getPassword()));
        }
        
        // 插入用户
        userMapper.insert(user);
        return user;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        User existingUser = getUserById(user.getId());
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 检查用户名和邮箱是否被其他用户使用
        User userByUsername = getUserByUsername(user.getUsername());
        if (userByUsername != null && !userByUsername.getId().equals(user.getId())) {
            throw new RuntimeException("用户名已存在");
        }
        
        User userByEmail = getUserByEmail(user.getEmail());
        if (userByEmail != null && !userByEmail.getId().equals(user.getId())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 设置更新时间
        user.setUpdateTime(LocalDateTime.now());
        
        // 如果密码不为空，则加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(PasswordUtil.encrypt(user.getPassword()));
        } else {
            // 保持原密码
            user.setPassword(existingUser.getPassword());
        }
        
        // 更新用户
        userMapper.updateById(user);
        return getUserById(user.getId());
    }

    @Override
    @Transactional
    public boolean deleteUser(Long id) {
        // 删除用户角色关联
        userMapper.deleteUserRoles(id);
        
        // 删除用户
        return userMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional
    public boolean batchDeleteUsers(List<Long> ids) {
        // 批量删除用户角色关联
        for (Long id : ids) {
            userMapper.deleteUserRoles(id);
        }
        
        // 批量删除用户
        return userMapper.batchDeleteByIds(ids) > 0;
    }

    @Override
    public boolean resetPassword(Long id) {
        User user = getUserById(id);
        if (user == null) {
            return false;
        }
        
        // 重置为默认密码
        String defaultPassword = "123456";
        String encryptedPassword = PasswordUtil.encrypt(defaultPassword);
        
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setPassword(encryptedPassword);
        updateUser.setUpdateTime(LocalDateTime.now());
        
        return userMapper.updateById(updateUser) > 0;
    }

    @Override
    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        User user = getUserById(id);
        if (user == null) {
            return false;
        }
        
        // 验证旧密码
        if (!PasswordUtil.matches(oldPassword, user.getPassword())) {
            return false;
        }
        
        // 更新新密码
        String encryptedPassword = PasswordUtil.encrypt(newPassword);
        
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setPassword(encryptedPassword);
        updateUser.setUpdateTime(LocalDateTime.now());
        
        return userMapper.updateById(updateUser) > 0;
    }

    @Override
    public boolean toggleStatus(Long id) {
        User user = getUserById(id);
        if (user == null) {
            return false;
        }
        
        String newStatus = "ACTIVE".equals(user.getStatus()) ? "INACTIVE" : "ACTIVE";
        
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setStatus(newStatus);
        updateUser.setUpdateTime(LocalDateTime.now());
        
        return userMapper.updateById(updateUser) > 0;
    }

    @Override
    @Transactional
    public boolean batchUpdateStatus(List<Long> ids, String status) {
        for (Long id : ids) {
            User updateUser = new User();
            updateUser.setId(id);
            updateUser.setStatus(status);
            updateUser.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(updateUser);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean assignRoles(Long userId, List<Long> roleIds) {
        // 先删除原有角色关联
        userMapper.deleteUserRoles(userId);
        
        // 添加新的角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                UserRole userRole = new UserRole(userId, roleId);
                userMapper.insertUserRole(userRole);
            }
        }
        
        return true;
    }

    @Override
    public List<Map<String, Object>> getUserRoles(Long userId) {
        return userMapper.selectUserRoles(userId);
    }

    @Override
    public Map<String, Object> importUsers(MultipartFile file) {
        // TODO: 实现Excel导入逻辑
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", 0);
        result.put("failCount", 0);
        result.put("errors", new String[0]);
        return result;
    }

    @Override
    public void exportUsers(String format, HttpServletResponse response) {
        // TODO: 实现用户导出逻辑
        // 根据format参数导出为Excel或CSV格式
    }

    @Override
    public Map<String, Object> getUserStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 获取各种统计信息
        stats.put("totalUsers", userMapper.countUsers(null, null, null, null));
        stats.put("activeUsers", userMapper.countUsers(null, "ACTIVE", null, null));
        stats.put("inactiveUsers", userMapper.countUsers(null, "INACTIVE", null, null));
        stats.put("lockedUsers", userMapper.countUsers(null, "LOCKED", null, null));
        
        // 获取今日、本周、本月新增用户数
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDateTime monthStart = today.withDayOfMonth(1);
        
        stats.put("newUsersToday", userMapper.countUsersByDateRange(today, LocalDateTime.now()));
        stats.put("newUsersThisWeek", userMapper.countUsersByDateRange(weekStart, LocalDateTime.now()));
        stats.put("newUsersThisMonth", userMapper.countUsersByDateRange(monthStart, LocalDateTime.now()));
        
        return stats;
    }

    @Override
    public boolean isUsernameExists(String username) {
        return getUserByUsername(username) != null;
    }

    @Override
    public boolean isEmailExists(String email) {
        return getUserByEmail(email) != null;
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        List<Map<String, Object>> userRoles = getUserRoles(userId);
        Set<String> permissions = new HashSet<>();
        
        // 从用户角色中获取权限
        for (Map<String, Object> userRole : userRoles) {
            Long roleId = (Long) userRole.get("roleId");
            List<String> rolePermissions = userMapper.selectRolePermissions(roleId);
            permissions.addAll(rolePermissions);
        }
        
        return new ArrayList<>(permissions);
    }

    @Override
    public List<String> getUserRoleNames(Long userId) {
        List<Map<String, Object>> userRoles = getUserRoles(userId);
        return userRoles.stream()
                .map(role -> (String) role.get("roleName"))
                .collect(Collectors.toList());
    }
}

