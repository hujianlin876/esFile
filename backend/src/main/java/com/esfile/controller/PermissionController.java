package com.esfile.controller;

import com.esfile.entity.mybatis.Permission;
import com.esfile.service.system.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

        /**
         * 查询所有权限
         */
        @GetMapping
        public List<Permission> listPermissions() {
            return permissionService.getAllPermissions();
        }

        /**
         * 新增权限
         */
        @PostMapping
        public boolean addPermission(@RequestBody Permission permission) {
            return permissionService.createPermission(permission);
        }

        /**
         * 删除权限
         */
        @DeleteMapping("/{id}")
        public boolean deletePermission(@PathVariable Long id) {
            return permissionService.deletePermission(id);
        }
}
