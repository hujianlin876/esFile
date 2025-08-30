package com.esfile.service.system;

import com.esfile.entity.mybatis.Menu;
import java.util.List;
import java.util.Map;

/**
 * 菜单服务接口
 * 定义菜单管理的所有方法
 */
public interface MenuService {
    
    /**
     * 获取菜单列表
     */
    List<Menu> getMenuList();
    
    /**
     * 根据ID获取菜单
     */
    Menu getMenuById(Long id);
    
    /**
     * 创建菜单
     */
    Menu createMenu(Menu menu);
    
    /**
     * 更新菜单
     */
    Menu updateMenu(Menu menu);
    
    /**
     * 删除菜单
     */
    boolean deleteMenu(Long id);
    
    /**
     * 根据父ID获取子菜单
     */
    List<Menu> getChildrenByParentId(Long parentId);
    
    /**
     * 获取菜单树结构
     */
    List<Map<String, Object>> getMenuTree();
    
    /**
     * 根据角色ID获取菜单
     */
    List<Menu> getMenusByRoleId(Long roleId);
    
    /**
     * 根据用户ID获取菜单
     */
    List<Menu> getUserMenus(Long userId);
    
    /**
     * 更新菜单状态
     */
    boolean updateMenuStatus(Long id, Integer status);
    
    /**
     * 更新菜单排序
     */
    boolean updateMenuSort(Long id, Integer sort);
}
