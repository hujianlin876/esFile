package com.esfile.mapper;

import com.esfile.entity.mybatis.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper接口
 * 
 * @author esfile
 * @since 2024-01-01
 */
@Mapper
public interface RoleMapper {
    
    /**
     * 插入角色
     * 
     * @param role 角色信息
     * @return 影响行数
     */
    int insert(Role role);
    
    /**
     * 根据ID更新角色
     * 
     * @param role 角色信息
     * @return 影响行数
     */
    int updateById(Role role);
    
    /**
     * 根据ID删除角色
     * 
     * @param id 角色ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据ID查询角色
     * 
     * @param id 角色ID
     * @return 角色信息
     */
    Role selectById(@Param("id") Long id);
    
    /**
     * 根据角色编码查询角色
     * 
     * @param roleCode 角色编码
     * @return 角色信息
     */
    Role selectByRoleCode(@Param("roleCode") String roleCode);
    
    /**
     * 查询所有角色
     * 
     * @return 角色列表
     */
    List<Role> selectAll();
    
    /**
     * 分页查询角色
     * 
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 角色列表
     */
    List<Role> selectPage(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 查询角色总数
     * 
     * @return 总数
     */
    long selectCount();
    
    /**
     * 根据用户ID查询角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据状态查询角色列表
     * 
     * @param status 状态
     * @return 角色列表
     */
    List<Role> selectByStatus(@Param("status") Integer status);
}
