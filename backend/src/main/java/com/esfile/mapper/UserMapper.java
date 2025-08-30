package com.esfile.mapper;

import com.esfile.entity.mybatis.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 * 
 * @author esfile
 * @since 1.0.0
 */
@Mapper
public interface UserMapper {

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    User selectById(@Param("id") Long id);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User selectByEmail(@Param("email") String email);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    User selectByPhone(@Param("phone") String phone);

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    List<User> selectAll();

    /**
     * 分页查询用户
     *
     * @param offset   偏移量
     * @param limit    限制数量
     * @param username 用户名（模糊查询）
     * @param realName 真实姓名（模糊查询）
     * @param email    邮箱（模糊查询）
     * @param phone    手机号（模糊查询）
     * @param status   状态
     * @param deptId   部门ID
     * @return 用户列表
     */
    List<User> selectByPage(@Param("offset") Integer offset,
                           @Param("limit") Integer limit,
                           @Param("username") String username,
                           @Param("realName") String realName,
                           @Param("email") String email,
                           @Param("phone") String phone,
                           @Param("status") String status,
                           @Param("deptId") Long deptId);

    /**
     * 统计用户总数
     *
     * @param username 用户名（模糊查询）
     * @param realName 真实姓名（模糊查询）
     * @param email    邮箱（模糊查询）
     * @param phone    手机号（模糊查询）
     * @param status   状态
     * @param deptId   部门ID
     * @return 用户总数
     */
    Long countByCondition(@Param("username") String username,
                         @Param("realName") String realName,
                         @Param("email") String email,
                         @Param("phone") String phone,
                         @Param("status") String status,
                         @Param("deptId") Long deptId);

    /**
     * 根据部门ID查询用户列表
     *
     * @param deptId 部门ID
     * @return 用户列表
     */
    List<User> selectByDeptId(@Param("deptId") Long deptId);

    /**
     * 根据角色ID查询用户列表
     *
     * @param roleId 角色ID
     * @return 用户列表
     */
    List<User> selectByRoleId(@Param("roleId") Long roleId);

    /**
     * 插入用户
     *
     * @param user 用户信息
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 更新用户
     *
     * @param user 用户信息
     * @return 影响行数
     */
    int update(User user);

    /**
     * 根据ID删除用户（逻辑删除）
     *
     * @param id 用户ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据ID物理删除用户
     *
     * @param id 用户ID
     * @return 影响行数
     */
    int deleteByIdPhysical(@Param("id") Long id);

    /**
     * 批量删除用户（逻辑删除）
     *
     * @param ids 用户ID列表
     * @return 影响行数
     */
    int deleteBatchByIds(@Param("ids") List<Long> ids);

    /**
     * 更新用户状态
     *
     * @param id     用户ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 更新用户密码
     *
     * @param id       用户ID
     * @param password 新密码
     * @return 影响行数
     */
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    /**
     * 更新用户头像
     *
     * @param id     用户ID
     * @param avatar 头像路径
     * @return 影响行数
     */
    int updateAvatar(@Param("id") Long id, @Param("avatar") String avatar);

    /**
     * 更新最后登录信息
     *
     * @param id            用户ID
     * @param lastLoginTime 最后登录时间
     * @param lastLoginIp   最后登录IP
     * @return 影响行数
     */
    int updateLastLoginInfo(@Param("id") Long id,
                           @Param("lastLoginTime") String lastLoginTime,
                           @Param("lastLoginIp") String lastLoginIp);

    /**
     * 更新登录失败次数
     *
     * @param id            用户ID
     * @param loginFailCount 登录失败次数
     * @return 影响行数
     */
    int updateLoginFailCount(@Param("id") Long id, @Param("loginFailCount") Integer loginFailCount);

    /**
     * 锁定用户账户
     *
     * @param id       用户ID
     * @param lockTime 锁定时间
     * @return 影响行数
     */
    int lockUser(@Param("id") Long id, @Param("lockTime") String lockTime);

    /**
     * 解锁用户账户
     *
     * @param id 用户ID
     * @return 影响行数
     */
    int unlockUser(@Param("id") Long id);

    /**
     * 重置用户密码
     *
     * @param id       用户ID
     * @param password 新密码
     * @return 影响行数
     */
    int resetPassword(@Param("id") Long id, @Param("password") String password);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @param excludeId 排除的用户ID（用于更新时检查）
     * @return 是否存在
     */
    boolean existsByUsername(@Param("username") String username, @Param("excludeId") Long excludeId);

    /**
     * 检查邮箱是否存在
     *
     * @param email    邮箱
     * @param excludeId 排除的用户ID（用于更新时检查）
     * @return 是否存在
     */
    boolean existsByEmail(@Param("email") String email, @Param("excludeId") Long excludeId);

    /**
     * 检查手机号是否存在
     *
     * @param phone    手机号
     * @param excludeId 排除的用户ID（用于更新时检查）
     * @return 是否存在
     */
    boolean existsByPhone(@Param("phone") String phone, @Param("excludeId") Long excludeId);
}
