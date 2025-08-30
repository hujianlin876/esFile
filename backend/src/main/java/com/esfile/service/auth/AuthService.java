package com.esfile.service.auth;
import com.esfile.entity.dto.LoginDto;
import com.esfile.entity.dto.RegisterDto;
/**
 * 认证服务接口
 */
public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}
