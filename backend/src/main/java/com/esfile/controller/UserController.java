package com.esfile.controller;

import com.esfile.entity.mybatis.User;
import com.esfile.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

        /**
         * 查询所有用户
         */
        @GetMapping
        public List<User> listUsers() {
            return userService.getAllUsers();
        }

        /**
         * 新增用户
         */
        @PostMapping
        public User addUser(@RequestBody User user) {
            userService.addUser(user);
            return user;
        }

        /**
         * 删除用户
         */
        @DeleteMapping("/{id}")
        public boolean deleteUser(@PathVariable Long id) {
            return userService.deleteUser(id);
        }
}
