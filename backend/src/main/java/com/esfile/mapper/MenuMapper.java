package com.esfile.mapper;

import com.esfile.entity.mybatis.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单映射器
 * 定义菜单相关的数据库操作方法
 */
@Mapper
public interface MenuMapper {
    
    /**
     * 查询所有菜单
     */
    List<Menu> selectAll();
    
    /**
     * 根据ID查询菜单
     */
    Menu selectById(@Param("id") Long id);
    
    /**
     * 插入菜单
     */
    int insert(Menu menu);
    
    /**
     * 更新菜单
     */
    int updateById(Menu menu);
    
    /**
     * 根据ID删除菜单
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据父ID查询子菜单
     */
    List<Menu> selectByParentId(@Param("parentId") Long parentId);
    
    /**
     * 根据角色ID查询菜单
     */
    List<Menu> selectByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 根据用户ID查询菜单
     */
    List<Menu> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 查询菜单总数
     */
    int selectCount();
    
    /**
     * 根据状态查询菜单数量
     */
    int selectCountByStatus(@Param("status") Integer status);
    
    /**
     * 根据名称和父ID查询菜单（用于检查重复）
     */
    Menu selectByNameAndParentId(@Param("name") String name, 
                                @Param("parentId") Long parentId, 
                                @Param("excludeId") Long excludeId);
}
