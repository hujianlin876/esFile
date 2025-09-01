package com.esfile.service.auth;
import com.esfile.entity.dto.LoginDto;
import com.esfile.entity.dto.RegisterDto;
import java.util.Map;

/**
 * 认证服务接口
 */
public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
    Map<String, Object> getCurrentUserInfo(String userId);
}
