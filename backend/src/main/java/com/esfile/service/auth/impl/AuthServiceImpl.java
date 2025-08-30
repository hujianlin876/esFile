package com.esfile.service.auth.impl;
import com.esfile.entity.dto.LoginDto;
import com.esfile.entity.dto.RegisterDto;
import com.esfile.service.auth.AuthService;
import org.springframework.stereotype.Service;
/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public String login(LoginDto loginDto) {
        // TODO: 登录实现
        @org.springframework.beans.factory.annotation.Autowired
        private com.esfile.mapper.UserMapper userMapper;
        if (loginDto == null || loginDto.getUsername() == null || loginDto.getPassword() == null) return null;
        com.esfile.entity.mybatis.User user = userMapper.selectByUsername(loginDto.getUsername());
        if (user == null) return null;
        if (!com.esfile.util.PasswordUtil.matches(loginDto.getPassword(), user.getPassword())) return null;
        // TODO: 生成 JWT token
        return "jwt-token";
    }
    @Override
    public String register(RegisterDto registerDto) {
        // TODO: 注册实现
        if (registerDto == null || registerDto.getUsername() == null || registerDto.getPassword() == null || registerDto.getEmail() == null) return null;
        com.esfile.entity.mybatis.User user = new com.esfile.entity.mybatis.User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(com.esfile.util.PasswordUtil.encode(registerDto.getPassword()));
        user.setEmail(registerDto.getEmail());
        user.setStatus("1");
        userMapper.insert(user);
        return "success";
    }
}
