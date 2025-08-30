package com.esfile.controller.auth;
import com.esfile.entity.dto.LoginDto;
import com.esfile.entity.dto.RegisterDto;
import com.esfile.common.vo.ResponseResult;
import org.springframework.web.bind.annotation.*;
/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private com.esfile.service.auth.AuthService authService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public ResponseResult<String> login(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);
        if (token != null && !token.isEmpty()) {
            return new ResponseResult<>(200, "登录成功", token);
        }
        return new ResponseResult<>(401, "用户名或密码错误", null);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public ResponseResult<String> register(@RequestBody RegisterDto registerDto) {
        String result = authService.register(registerDto);
        if (result != null && !result.isEmpty()) {
            return new ResponseResult<>(200, "注册成功", result);
        }
        return new ResponseResult<>(400, "注册信息不完整", null);
    }
}
