package com.esfile.service.system.impl;

import com.esfile.entity.mybatis.Menu;
import com.esfile.mapper.MenuMapper;
import com.esfile.service.system.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单管理服务实现类
 * 提供菜单的增删改查功能
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getMenuList() {
        return menuMapper.selectAll();
    }

    @Override
    public Menu getMenuById(Long id) {
        return menuMapper.selectById(id);
    }

    @Override
    @Transactional
    public Menu createMenu(Menu menu) {
        // 设置创建时间
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        
        // 设置默认值
        if (menu.getSort() == null) {
            menu.setSort(0);
        }
        if (menu.getStatus() == null) {
            menu.setStatus(1);
        }
        
        menuMapper.insert(menu);
        return menu;
    }

    @Override
    @Transactional
    public Menu updateMenu(Menu menu) {
        // 设置更新时间
        menu.setUpdateTime(LocalDateTime.now());
        
        menuMapper.updateById(menu);
        return menu;
    }

    @Override
    @Transactional
    public boolean deleteMenu(Long id) {
        // 检查是否有子菜单
        List<Menu> children = getChildrenByParentId(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("该菜单下还有子菜单，无法删除");
        }
        
        return menuMapper.deleteById(id) > 0;
    }

    @Override
    public List<Menu> getChildrenByParentId(Long parentId) {
        return menuMapper.selectByParentId(parentId);
    }

    @Override
    public List<Map<String, Object>> getMenuTree() {
        List<Menu> allMenus = menuMapper.selectAll();
        return buildMenuTree(allMenus, 0L);
    }

    @Override
    public List<Menu> getMenusByRoleId(Long roleId) {
        return menuMapper.selectByRoleId(roleId);
    }

    @Override
    public List<Menu> getUserMenus(Long userId) {
        return menuMapper.selectByUserId(userId);
    }

    @Override
    public boolean updateMenuStatus(Long id, Integer status) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setStatus(status);
        menu.setUpdateTime(LocalDateTime.now());
        
        return menuMapper.updateById(menu) > 0;
    }

    @Override
    public boolean updateMenuSort(Long id, Integer sort) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setSort(sort);
        menu.setUpdateTime(LocalDateTime.now());
        
        return menuMapper.updateById(menu) > 0;
    }

    /**
     * 构建菜单树结构
     */
    private List<Map<String, Object>> buildMenuTree(List<Menu> menus, Long parentId) {
        List<Map<String, Object>> tree = new ArrayList<>();
        
        for (Menu menu : menus) {
            if (parentId.equals(menu.getParentId())) {
                Map<String, Object> node = new HashMap<>();
                node.put("id", menu.getId());
                node.put("name", menu.getName());
                node.put("path", menu.getPath());
                node.put("component", menu.getComponent());
                node.put("icon", menu.getIcon());
                node.put("sort", menu.getSort());
                node.put("status", menu.getStatus());
                node.put("createTime", menu.getCreateTime());
                node.put("updateTime", menu.getUpdateTime());
                
                // 递归构建子菜单
                List<Map<String, Object>> children = buildMenuTree(menus, menu.getId());
                if (!children.isEmpty()) {
                    node.put("children", children);
                }
                
                tree.add(node);
            }
        }
        
        // 按排序字段排序
        tree.sort((a, b) -> {
            Integer sortA = (Integer) a.get("sort");
            Integer sortB = (Integer) b.get("sort");
            return Integer.compare(sortA != null ? sortA : 0, sortB != null ? sortB : 0);
        });
        
        return tree;
    }

    /**
     * 获取菜单路径
     */
    public List<Menu> getMenuPath(Long menuId) {
        List<Menu> path = new ArrayList<>();
        Menu current = menuMapper.selectById(menuId);
        
        while (current != null) {
            path.add(0, current);
            if (current.getParentId() != null && current.getParentId() > 0) {
                current = menuMapper.selectById(current.getParentId());
            } else {
                break;
            }
        }
        
        return path;
    }

    /**
     * 检查菜单名称是否存在
     */
    public boolean isMenuNameExists(String name, Long parentId, Long excludeId) {
        return menuMapper.selectByNameAndParentId(name, parentId, excludeId) != null;
    }

    /**
     * 获取菜单统计信息
     */
    public Map<String, Object> getMenuStats() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            int totalMenus = menuMapper.selectCount();
            int activeMenus = menuMapper.selectCountByStatus(1);
            int inactiveMenus = menuMapper.selectCountByStatus(0);
            
            stats.put("totalMenus", totalMenus);
            stats.put("activeMenus", activeMenus);
            stats.put("inactiveMenus", inactiveMenus);
            stats.put("menuTreeLevels", getMenuTreeLevels());
            
        } catch (Exception e) {
            stats.put("error", "获取菜单统计信息失败: " + e.getMessage());
        }
        
        return stats;
    }

    /**
     * 获取菜单树层级数
     */
    private int getMenuTreeLevels() {
        List<Menu> allMenus = menuMapper.selectAll();
        int maxLevel = 0;
        
        for (Menu menu : allMenus) {
            int level = calculateMenuLevel(menu.getId(), allMenus);
            maxLevel = Math.max(maxLevel, level);
        }
        
        return maxLevel;
    }

    /**
     * 计算菜单层级
     */
    private int calculateMenuLevel(Long menuId, List<Menu> allMenus) {
        int level = 1;
        Menu current = findMenuById(menuId, allMenus);
        
        while (current != null && current.getParentId() != null && current.getParentId() > 0) {
            level++;
            current = findMenuById(current.getParentId(), allMenus);
        }
        
        return level;
    }

    /**
     * 在菜单列表中查找指定ID的菜单
     */
    private Menu findMenuById(Long id, List<Menu> menus) {
        for (Menu menu : menus) {
            if (id.equals(menu.getId())) {
                return menu;
            }
        }
        return null;
    }
}
