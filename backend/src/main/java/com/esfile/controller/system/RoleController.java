package com.esfile.controller.system;
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
        return roleService.getAllRoles();
    }

    /**
     * 新增角色
     */
    @PostMapping
    public boolean addRole(@RequestBody com.esfile.entity.mybatis.Role role) {
        return roleService.createRole(role);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public boolean deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }
}
