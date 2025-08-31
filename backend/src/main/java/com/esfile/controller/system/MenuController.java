package com.esfile.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * 菜单控制器
 */
@RestController
@RequestMapping("/api/menu")
public class MenuController {
    @Autowired
    private com.esfile.service.system.MenuService menuService;

    /**
     * 查询所有菜单
     */
    @GetMapping
    public Object listMenus() {
        return "菜单管理功能待实现";
    }

    /**
     * 新增菜单
     */
    @PostMapping
    public String addMenu(@RequestBody com.esfile.entity.mybatis.Menu menu) {
        return "新增菜单功能待实现";
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{id}")
    public String deleteMenu(@PathVariable Long id) {
        return "删除菜单功能待实现";
    }
}
