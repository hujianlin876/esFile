package com.esfile.controller.auth;
import org.springframework.web.bind.annotation.*;
/**
 * 登录控制器
 */
@RestController
@RequestMapping("/api/login")
public class LoginController {
    @PostMapping
    public String login() {
        // TODO: 登录逻辑
        return "success";
    }
}
