package com.esfile.controller.auth;

import com.esfile.common.vo.ResponseResult;
import com.esfile.entity.dto.LoginDto;
import com.esfile.entity.dto.RegisterDto;
import com.esfile.service.auth.AuthService;
import com.esfile.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 提供用户登录、注册等认证相关API接口
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseResult<Map<String, Object>> login(@RequestBody LoginDto loginDto) {
        try {
            String token = authService.login(loginDto);
            
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("tokenType", "Bearer");
            result.put("expiresIn", 86400000); // 24小时
            
            return ResponseResult.success(result, "登录成功");
        } catch (Exception e) {
            return ResponseResult.fail("登录失败: " + e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseResult<String> register(@RequestBody RegisterDto registerDto) {
        try {
            String result = authService.register(registerDto);
            return ResponseResult.success(result, "注册成功");
        } catch (Exception e) {
            return ResponseResult.fail("注册失败: " + e.getMessage());
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public ResponseResult<String> logout() {
        try {
            // TODO: 实现登出逻辑，如清除Token等
            return ResponseResult.success("success", "登出成功");
        } catch (Exception e) {
            return ResponseResult.fail("登出失败: " + e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    public ResponseResult<Map<String, Object>> getCurrentUser(HttpServletRequest request) {
        try {
            // 从Authorization header中获取token
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseResult.fail("Token格式错误");
            }
            
            String token = authHeader.substring(7);
            boolean isValid = jwtUtil.validateToken(token);
            
            if (!isValid) {
                return ResponseResult.fail("Token无效或已过期");
            }
            
            // 从token中获取用户信息
            String userId = jwtUtil.getUserIdFromToken(token);
            String username = jwtUtil.getUsernameFromToken(token);
            
            // 获取用户详细信息
            Map<String, Object> userInfo = authService.getCurrentUserInfo(userId);
            
            return ResponseResult.success(userInfo, "获取用户信息成功");
        } catch (Exception e) {
            return ResponseResult.fail("获取用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refresh")
    public ResponseResult<Map<String, Object>> refreshToken(HttpServletRequest request) {
        try {
            // 从Authorization header中获取token
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseResult.fail("Token格式错误");
            }
            
            String token = authHeader.substring(7);
            boolean isValid = jwtUtil.validateToken(token);
            
            if (!isValid) {
                return ResponseResult.fail("Token无效或已过期");
            }
            
            // 生成新的token
            String username = jwtUtil.getUsernameFromToken(token);
            String newToken = jwtUtil.generateToken("2", username); // 使用用户ID 2
            
            Map<String, Object> result = new HashMap<>();
            result.put("token", newToken);
            result.put("tokenType", "Bearer");
            result.put("expiresIn", 86400000);
            
            return ResponseResult.success(result, "Token刷新成功");
        } catch (Exception e) {
            return ResponseResult.fail("Token刷新失败: " + e.getMessage());
        }
    }

    /**
     * 检查Token有效性
     */
    @GetMapping("/verify")
    public ResponseResult<Boolean> verifyToken(HttpServletRequest request) {
        try {
            // 从Authorization header中获取token
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseResult.fail("Token格式错误");
            }
            
            String token = authHeader.substring(7);
            boolean isValid = jwtUtil.validateToken(token);
            
            if (isValid) {
                return ResponseResult.success(true, "Token有效");
            } else {
                return ResponseResult.fail("Token无效或已过期");
            }
        } catch (Exception e) {
            return ResponseResult.fail("Token验证失败: " + e.getMessage());
        }
    }
}
