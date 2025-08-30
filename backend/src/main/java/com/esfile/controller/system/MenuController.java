package com.esfile.controller.system;
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
        return menuService.getAllMenus();
    }

    /**
     * 新增菜单
     */
    @PostMapping
    public boolean addMenu(@RequestBody com.esfile.entity.mybatis.Menu menu) {
        return menuService.createMenu(menu);
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{id}")
    public boolean deleteMenu(@PathVariable Long id) {
        return menuService.deleteMenu(id);
    }
}
