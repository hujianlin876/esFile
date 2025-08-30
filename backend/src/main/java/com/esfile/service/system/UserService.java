package com.esfile.service.system;

import com.esfile.common.vo.PageResult;
import com.esfile.entity.mybatis.User;

import java.util.List;

/**
 * 用户服务接口
 * 
 * @author esfile
 * @since 1.0.0
 */
public interface UserService {

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    User getUserById(Long id);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User getUserByEmail(String email);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    User getUserByPhone(String phone);

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    List<User> getAllUsers();

    /**
     * 分页查询用户
     *
     * @param pageNum   当前页码
     * @param pageSize  每页大小
     * @param username  用户名（模糊查询）
     * @param realName  真实姓名（模糊查询）
     * @param email     邮箱（模糊查询）
     * @param phone     手机号（模糊查询）
     * @param status    状态
     * @param deptId    部门ID
     * @return 分页结果
     */
    PageResult<User> getUserPage(Integer pageNum, Integer pageSize, String username, String realName,
                               String email, String phone, String status, Long deptId);

    /**
     * 根据部门ID查询用户列表
     *
     * @param deptId 部门ID
     * @return 用户列表
     */
    List<User> getUsersByDeptId(Long deptId);

    /**
     * 根据角色ID查询用户列表
     *
     * @param roleId 角色ID
     * @return 用户列表
     */
    List<User> getUsersByRoleId(Long roleId);

    /**
     * 创建用户
     *
     * @param user 用户信息
     * @return 是否成功
     */
    boolean createUser(User user);

    /**
     * 更新用户
     *
     * @param user 用户信息
     * @return 是否成功
     */
    boolean updateUser(User user);

    /**
     * 根据ID删除用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteUser(Long id);

    /**
     * 批量删除用户
     *
     * @param ids 用户ID列表
     * @return 是否成功
     */
    boolean deleteUsers(List<Long> ids);

    /**
     * 更新用户状态
     *
     * @param id     用户ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateUserStatus(Long id, String status);

    /**
     * 更新用户密码
     *
     * @param id       用户ID
     * @param password 新密码
     * @return 是否成功
     */
    boolean updateUserPassword(Long id, String password);

    /**
     * 更新用户头像
     *
     * @param id     用户ID
     * @param avatar 头像路径
     * @return 是否成功
     */
    boolean updateUserAvatar(Long id, String avatar);

    /**
     * 更新最后登录信息
     *
     * @param id           用户ID
     * @param lastLoginTime 最后登录时间
     * @param lastLoginIp   最后登录IP
     * @return 是否成功
     */
    boolean updateLastLoginInfo(Long id, String lastLoginTime, String lastLoginIp);

    /**
     * 更新登录失败次数
     *
     * @param id            用户ID
     * @param loginFailCount 登录失败次数
     * @return 是否成功
     */
    boolean updateLoginFailCount(Long id, Integer loginFailCount);

    /**
     * 锁定用户账户
     *
     * @param id       用户ID
     * @param lockTime 锁定时间
     * @return 是否成功
     */
    boolean lockUser(Long id, String lockTime);

    /**
     * 解锁用户账户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    boolean unlockUser(Long id);

    /**
     * 重置用户密码
     *
     * @param id       用户ID
     * @param password 新密码
     * @return 是否成功
     */
    boolean resetUserPassword(Long id, String password);

    /**
     * 检查用户名是否存在
     *
     * @param username  用户名
     * @param excludeId 排除的用户ID（用于更新时检查）
     * @return 是否存在
     */
    boolean isUsernameExists(String username, Long excludeId);

    /**
     * 检查邮箱是否存在
     *
     * @param email     邮箱
     * @param excludeId 排除的用户ID（用于更新时检查）
     * @return 是否存在
     */
    boolean isEmailExists(String email, Long excludeId);

    /**
     * 检查手机号是否存在
     *
     * @param phone     手机号
     * @param excludeId 排除的用户ID（用于更新时检查）
     * @return 是否存在
     */
    boolean isPhoneExists(String phone, Long excludeId);

    /**
     * 获取用户显示名称
     *
     * @param user 用户信息
     * @return 显示名称
     */
    String getUserDisplayName(User user);

    /**
     * 判断用户是否为超级管理员
     *
     * @param user 用户信息
     * @return 是否为超级管理员
     */
    boolean isSuperAdmin(User user);

    /**
     * 判断用户是否被锁定
     *
     * @param user 用户信息
     * @return 是否被锁定
     */
    boolean isUserLocked(User user);

    /**
     * 判断用户是否启用
     *
     * @param user 用户信息
     * @return 是否启用
     */
    boolean isUserEnabled(User user);
}

