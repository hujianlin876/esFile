package com.esfile.service.system.impl;

import com.esfile.common.constant.CommonConstant;
import com.esfile.common.vo.PageResult;
import com.esfile.entity.mybatis.User;
import com.esfile.mapper.UserMapper;
import com.esfile.service.system.UserService;
import com.esfile.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 用户服务实现类
 * 
 * @author esfile
 * @since 1.0.0
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(Long id) {
        if (id == null) {
            log.warn("查询用户失败：用户ID为空");
            return null;
        }
        try {
            return userMapper.selectById(id);
        } catch (Exception e) {
            log.error("查询用户失败，用户ID：{}，错误信息：{}", id, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            log.warn("查询用户失败：用户名为空");
            return null;
        }
        try {
            return userMapper.selectByUsername(username);
        } catch (Exception e) {
            log.error("根据用户名查询用户失败，用户名：{}，错误信息：{}", username, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            log.warn("查询用户失败：邮箱为空");
            return null;
        }
        try {
            return userMapper.selectByEmail(email);
        } catch (Exception e) {
            log.error("根据邮箱查询用户失败，邮箱：{}，错误信息：{}", email, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public User getUserByPhone(String phone) {
        if (!StringUtils.hasText(phone)) {
            log.warn("查询用户失败：手机号为空");
            return null;
        }
        try {
            return userMapper.selectByPhone(phone);
        } catch (Exception e) {
            log.error("根据手机号查询用户失败，手机号：{}，错误信息：{}", phone, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            return userMapper.selectAll();
        } catch (Exception e) {
            log.error("查询所有用户失败，错误信息：{}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public PageResult<User> getUserPage(Integer pageNum, Integer pageSize, String username, String realName,
                                      String email, String phone, String status, Long deptId) {
        try {
            // 参数验证和默认值设置
            if (pageNum == null || pageNum < 1) {
                pageNum = 1;
            }
            if (pageSize == null || pageSize < 1) {
                pageSize = CommonConstant.DEFAULT_PAGE_SIZE;
            }
            if (pageSize > CommonConstant.MAX_PAGE_SIZE) {
                pageSize = CommonConstant.MAX_PAGE_SIZE;
            }

            // 计算偏移量
            int offset = (pageNum - 1) * pageSize;

            // 查询数据
            List<User> userList = userMapper.selectByPage(offset, pageSize, username, realName, email, phone, status, deptId);
            Long total = userMapper.countByCondition(username, realName, email, phone, status, deptId);

            // 构建分页结果
            return PageResult.of(userList, total, pageNum, pageSize);
        } catch (Exception e) {
            log.error("分页查询用户失败，页码：{}，每页大小：{}，错误信息：{}", pageNum, pageSize, e.getMessage(), e);
            return PageResult.empty(pageNum, pageSize);
        }
    }

    @Override
    public List<User> getUsersByDeptId(Long deptId) {
        if (deptId == null) {
            log.warn("查询用户失败：部门ID为空");
            return null;
        }
        try {
            return userMapper.selectByDeptId(deptId);
        } catch (Exception e) {
            log.error("根据部门ID查询用户失败，部门ID：{}，错误信息：{}", deptId, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<User> getUsersByRoleId(Long roleId) {
        if (roleId == null) {
            log.warn("查询用户失败：角色ID为空");
            return null;
        }
        try {
            return userMapper.selectByRoleId(roleId);
        } catch (Exception e) {
            log.error("根据角色ID查询用户失败，角色ID：{}，错误信息：{}", roleId, e.getMessage(), e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createUser(User user) {
        if (user == null) {
            log.warn("创建用户失败：用户信息为空");
            return false;
        }

        try {
            // 设置创建信息
            user.setCreateInfo(user.getCreateBy());
            
            // 设置默认值
            if (!StringUtils.hasText(user.getStatus())) {
                user.setStatus(CommonConstant.ENABLE);
            }
            if (!StringUtils.hasText(user.getGender())) {
                user.setGender("0");
            }
            if (user.getLoginFailCount() == null) {
                user.setLoginFailCount(0);
            }

            // 加密密码
            if (StringUtils.hasText(user.getPassword())) {
                String encryptedPassword = PasswordUtil.encrypt(user.getPassword());
                user.setPassword(encryptedPassword);
            }

            int result = userMapper.insert(user);
            if (result > 0) {
                log.info("创建用户成功，用户ID：{}，用户名：{}", user.getId(), user.getUsername());
                return true;
            } else {
                log.warn("创建用户失败，用户名：{}", user.getUsername());
                return false;
            }
        } catch (Exception e) {
            log.error("创建用户失败，用户名：{}，错误信息：{}", user.getUsername(), e.getMessage(), e);
            throw new RuntimeException("创建用户失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(User user) {
        if (user == null || user.getId() == null) {
            log.warn("更新用户失败：用户信息为空或用户ID为空");
            return false;
        }

        try {
            // 设置更新信息
            user.setUpdateInfo(user.getUpdateBy());

            int result = userMapper.update(user);
            if (result > 0) {
                log.info("更新用户成功，用户ID：{}，用户名：{}", user.getId(), user.getUsername());
                return true;
            } else {
                log.warn("更新用户失败，用户ID：{}", user.getId());
                return false;
            }
        } catch (Exception e) {
            log.error("更新用户失败，用户ID：{}，错误信息：{}", user.getId(), e.getMessage(), e);
            throw new RuntimeException("更新用户失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long id) {
        if (id == null) {
            log.warn("删除用户失败：用户ID为空");
            return false;
        }

        try {
            int result = userMapper.deleteById(id);
            if (result > 0) {
                log.info("删除用户成功，用户ID：{}", id);
                return true;
            } else {
                log.warn("删除用户失败，用户ID：{}", id);
                return false;
            }
        } catch (Exception e) {
            log.error("删除用户失败，用户ID：{}，错误信息：{}", id, e.getMessage(), e);
            throw new RuntimeException("删除用户失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            log.warn("批量删除用户失败：用户ID列表为空");
            return false;
        }

        try {
            int result = userMapper.deleteBatchByIds(ids);
            if (result > 0) {
                log.info("批量删除用户成功，删除数量：{}，用户ID列表：{}", result, ids);
                return true;
            } else {
                log.warn("批量删除用户失败，用户ID列表：{}", ids);
                return false;
            }
        } catch (Exception e) {
            log.error("批量删除用户失败，用户ID列表：{}，错误信息：{}", ids, e.getMessage(), e);
            throw new RuntimeException("批量删除用户失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserStatus(Long id, String status) {
        if (id == null || !StringUtils.hasText(status)) {
            log.warn("更新用户状态失败：用户ID为空或状态为空");
            return false;
        }

        try {
            int result = userMapper.updateStatus(id, status);
            if (result > 0) {
                log.info("更新用户状态成功，用户ID：{}，状态：{}", id, status);
                return true;
            } else {
                log.warn("更新用户状态失败，用户ID：{}，状态：{}", id, status);
                return false;
            }
        } catch (Exception e) {
            log.error("更新用户状态失败，用户ID：{}，状态：{}，错误信息：{}", id, status, e.getMessage(), e);
            throw new RuntimeException("更新用户状态失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserPassword(Long id, String password) {
        if (id == null || !StringUtils.hasText(password)) {
            log.warn("更新用户密码失败：用户ID为空或密码为空");
            return false;
        }

        try {
            // 加密密码
            String encryptedPassword = PasswordUtil.encrypt(password);
            int result = userMapper.updatePassword(id, encryptedPassword);
            if (result > 0) {
                log.info("更新用户密码成功，用户ID：{}", id);
                return true;
            } else {
                log.warn("更新用户密码失败，用户ID：{}", id);
                return false;
            }
        } catch (Exception e) {
            log.error("更新用户密码失败，用户ID：{}，错误信息：{}", id, e.getMessage(), e);
            throw new RuntimeException("更新用户密码失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserAvatar(Long id, String avatar) {
        if (id == null || !StringUtils.hasText(avatar)) {
            log.warn("更新用户头像失败：用户ID为空或头像路径为空");
            return false;
        }

        try {
            int result = userMapper.updateAvatar(id, avatar);
            if (result > 0) {
                log.info("更新用户头像成功，用户ID：{}，头像路径：{}", id, avatar);
                return true;
            } else {
                log.warn("更新用户头像失败，用户ID：{}", id);
                return false;
            }
        } catch (Exception e) {
            log.error("更新用户头像失败，用户ID：{}，错误信息：{}", id, e.getMessage(), e);
            throw new RuntimeException("更新用户头像失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLastLoginInfo(Long id, String lastLoginTime, String lastLoginIp) {
        if (id == null) {
            log.warn("更新最后登录信息失败：用户ID为空");
            return false;
        }

        try {
            int result = userMapper.updateLastLoginInfo(id, lastLoginTime, lastLoginIp);
            if (result > 0) {
                log.info("更新最后登录信息成功，用户ID：{}，登录时间：{}，登录IP：{}", id, lastLoginTime, lastLoginIp);
                return true;
            } else {
                log.warn("更新最后登录信息失败，用户ID：{}", id);
                return false;
            }
        } catch (Exception e) {
            log.error("更新最后登录信息失败，用户ID：{}，错误信息：{}", id, e.getMessage(), e);
            throw new RuntimeException("更新最后登录信息失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLoginFailCount(Long id, Integer loginFailCount) {
        if (id == null || loginFailCount == null) {
            log.warn("更新登录失败次数失败：用户ID为空或登录失败次数为空");
            return false;
        }

        try {
            int result = userMapper.updateLoginFailCount(id, loginFailCount);
            if (result > 0) {
                log.info("更新登录失败次数成功，用户ID：{}，失败次数：{}", id, loginFailCount);
                return true;
            } else {
                log.warn("更新登录失败次数失败，用户ID：{}", id);
                return false;
            }
        } catch (Exception e) {
            log.error("更新登录失败次数失败，用户ID：{}，错误信息：{}", id, e.getMessage(), e);
            throw new RuntimeException("更新登录失败次数失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean lockUser(Long id, String lockTime) {
        if (id == null || !StringUtils.hasText(lockTime)) {
            log.warn("锁定用户账户失败：用户ID为空或锁定时间为空");
            return false;
        }

        try {
            int result = userMapper.lockUser(id, lockTime);
            if (result > 0) {
                log.info("锁定用户账户成功，用户ID：{}，锁定时间：{}", id, lockTime);
                return true;
            } else {
                log.warn("锁定用户账户失败，用户ID：{}", id);
                return false;
            }
        } catch (Exception e) {
            log.error("锁定用户账户失败，用户ID：{}，错误信息：{}", id, e.getMessage(), e);
            throw new RuntimeException("锁定用户账户失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlockUser(Long id) {
        if (id == null) {
            log.warn("解锁用户账户失败：用户ID为空");
            return false;
        }

        try {
            int result = userMapper.unlockUser(id);
            if (result > 0) {
                log.info("解锁用户账户成功，用户ID：{}", id);
                return true;
            } else {
                log.warn("解锁用户账户失败，用户ID：{}", id);
                return false;
            }
        } catch (Exception e) {
            log.error("解锁用户账户失败，用户ID：{}，错误信息：{}", id, e.getMessage(), e);
            throw new RuntimeException("解锁用户账户失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetUserPassword(Long id, String password) {
        if (id == null || !StringUtils.hasText(password)) {
            log.warn("重置用户密码失败：用户ID为空或密码为空");
            return false;
        }

        try {
            // 加密密码
            String encryptedPassword = PasswordUtil.encrypt(password);
            int result = userMapper.resetPassword(id, encryptedPassword);
            if (result > 0) {
                log.info("重置用户密码成功，用户ID：{}", id);
                return true;
            } else {
                log.warn("重置用户密码失败，用户ID：{}", id);
                return false;
            }
        } catch (Exception e) {
            log.error("重置用户密码失败，用户ID：{}，错误信息：{}", id, e.getMessage(), e);
            throw new RuntimeException("重置用户密码失败", e);
        }
    }

    @Override
    public boolean isUsernameExists(String username, Long excludeId) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        try {
            return userMapper.existsByUsername(username, excludeId);
        } catch (Exception e) {
            log.error("检查用户名是否存在失败，用户名：{}，错误信息：{}", username, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean isEmailExists(String email, Long excludeId) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        try {
            return userMapper.existsByEmail(email, excludeId);
        } catch (Exception e) {
            log.error("检查邮箱是否存在失败，邮箱：{}，错误信息：{}", email, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean isPhoneExists(String phone, Long excludeId) {
        if (!StringUtils.hasText(phone)) {
            return false;
        }
        try {
            return userMapper.existsByPhone(phone, excludeId);
        } catch (Exception e) {
            log.error("检查手机号是否存在失败，手机号：{}，错误信息：{}", phone, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public String getUserDisplayName(User user) {
        if (user == null) {
            return "";
        }
        return user.getDisplayName();
    }

    @Override
    public boolean isSuperAdmin(User user) {
        if (user == null) {
            return false;
        }
        return user.isSuperAdmin();
    }

    @Override
    public boolean isUserLocked(User user) {
        if (user == null) {
            return false;
        }
        return user.isLocked();
    }

    @Override
    public boolean isUserEnabled(User user) {
        if (user == null) {
            return false;
        }
        return user.isEnabled();
    }
}

