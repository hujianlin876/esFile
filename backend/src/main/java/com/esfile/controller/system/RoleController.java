package com.esfile.controller.system;

import com.esfile.service.system.RoleService;
import com.esfile.entity.mybatis.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * 角色控制器
 */
@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    private com.esfile.service.system.RoleService roleService;

    /**
     * 查询所有角色
     */
    @GetMapping
    public Object listRoles() {
        return "角色管理功能待实现";
    }

    /**
     * 新增角色
     */
    @PostMapping
    public String addRole(@RequestBody Role role) {
        return "新增角色功能待实现";
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public String deleteRole(@PathVariable Long id) {
        return "删除角色功能待实现";
    }
}
