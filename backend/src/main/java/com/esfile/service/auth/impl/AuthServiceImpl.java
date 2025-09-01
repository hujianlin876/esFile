package com.esfile.service.auth.impl;

import com.esfile.entity.dto.LoginDto;
import com.esfile.entity.dto.RegisterDto;
import com.esfile.entity.mybatis.User;
import com.esfile.mapper.UserMapper;
import com.esfile.service.auth.AuthService;
import com.esfile.util.PasswordUtil;
import com.esfile.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务实现类
 * 处理用户登录、注册等认证相关功能
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String login(LoginDto loginDto) {
        if (loginDto == null || loginDto.getUsername() == null || loginDto.getPassword() == null) {
            throw new RuntimeException("用户名和密码不能为空");
        }

        // 根据用户名查询用户
        User user = userMapper.selectByUsername(loginDto.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证密码
        if (!PasswordUtil.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 检查用户状态
        if (!Integer.valueOf(1).equals(user.getStatus())) {
            throw new RuntimeException("用户已被禁用");
        }

        // 更新登录信息
        updateLoginInfo(user.getId());

        // TODO: 生成JWT Token
        String token = generateJwtToken(user);
        
        return token;
    }

    @Override
    @Transactional
    public String register(RegisterDto registerDto) {
        if (registerDto == null || registerDto.getUsername() == null || 
            registerDto.getPassword() == null || registerDto.getEmail() == null) {
            throw new RuntimeException("用户名、密码和邮箱不能为空");
        }

        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(registerDto.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        User userByEmail = userMapper.selectByEmail(registerDto.getEmail());
        if (userByEmail != null) {
            throw new RuntimeException("邮箱已被使用");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(PasswordUtil.encrypt(registerDto.getPassword()));
        user.setEmail(registerDto.getEmail());
        user.setNickname(registerDto.getUsername());
        user.setStatus(1); // 1-激活状态
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setLoginFailCount(0);

        // 插入用户
        userMapper.insert(user);
        
        return "success";
    }

    /**
     * 更新登录信息
     */
    private void updateLoginInfo(Long userId) {
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setLastLoginTime(LocalDateTime.now());
        updateUser.setLoginFailCount(0);
        updateUser.setUpdateTime(LocalDateTime.now());
        
        userMapper.updateById(updateUser);
    }

    /**
     * 生成JWT Token
     */
    private String generateJwtToken(User user) {
        // 使用JWT工具类生成真正的Token
        return jwtUtil.generateToken(user.getId().toString(), user.getUsername());
    }

    @Override
    public Map<String, Object> getCurrentUserInfo(String userId) {
        try {
            Long id = Long.parseLong(userId);
            User user = userMapper.selectById(id);
            
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }
            
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("nickname", user.getNickname());
            userInfo.put("email", user.getEmail());
            userInfo.put("phone", user.getPhone());
            userInfo.put("avatar", user.getAvatar());
            userInfo.put("gender", user.getGender());
            userInfo.put("status", user.getStatus());
            userInfo.put("deptId", user.getDeptId());
            userInfo.put("createTime", user.getCreateTime());
            userInfo.put("updateTime", user.getUpdateTime());
            
            // 添加角色和权限信息（暂时使用默认值）
            userInfo.put("roles", new String[]{"USER"});
            userInfo.put("permissions", new String[]{"file:read", "file:upload"});
            
            return userInfo;
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息失败: " + e.getMessage());
        }
    }
}
