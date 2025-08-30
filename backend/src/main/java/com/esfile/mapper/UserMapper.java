package com.esfile.mapper;

import com.esfile.entity.mybatis.User;
import com.esfile.entity.mybatis.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {

    /**
     * 根据ID查询用户
     */
    User selectById(Long id);

    /**
     * 根据用户名查询用户
     */
    User selectByUsername(String username);

    /**
     * 根据邮箱查询用户
     */
    User selectByEmail(String email);

    /**
     * 分页查询用户列表
     */
    List<User> selectUserList(@Param("offset") int offset, 
                             @Param("size") int size, 
                             @Param("keyword") String keyword, 
                             @Param("status") String status, 
                             @Param("role") String role, 
                             @Param("dept") String dept);

    /**
     * 统计用户数量
     */
    long countUsers(@Param("keyword") String keyword, 
                   @Param("status") String status, 
                   @Param("role") String role, 
                   @Param("dept") String dept);

    /**
     * 根据时间范围统计用户数量
     */
    long countUsersByDateRange(@Param("startTime") LocalDateTime startTime, 
                              @Param("endTime") LocalDateTime endTime);

    /**
     * 插入用户
     */
    int insert(User user);

    /**
     * 更新用户
     */
    int updateById(User user);

    /**
     * 根据ID删除用户
     */
    int deleteById(Long id);

    /**
     * 批量删除用户
     */
    int batchDeleteByIds(List<Long> ids);

    /**
     * 插入用户角色关联
     */
    int insertUserRole(UserRole userRole);

    /**
     * 删除用户角色关联
     */
    int deleteUserRoles(Long userId);

    /**
     * 查询用户角色
     */
    List<Map<String, Object>> selectUserRoles(Long userId);

    /**
     * 查询角色权限
     */
    List<String> selectRolePermissions(Long roleId);
}
