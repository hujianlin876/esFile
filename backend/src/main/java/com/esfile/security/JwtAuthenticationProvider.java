package com.esfile.security;

import com.esfile.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * JWT认证提供者
 * 用于处理JWT token的认证逻辑
 * 
 * @author esfile
 * @since 1.0.0
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // 加载用户详情
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        if (userDetails == null) {
            throw new BadCredentialsException("用户不存在");
        }

        // 验证密码
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }

        // 检查用户是否启用
        if (!userDetails.isEnabled()) {
            throw new BadCredentialsException("用户账户已被禁用");
        }

        // 检查账户是否未过期
        if (!userDetails.isAccountNonExpired()) {
            throw new BadCredentialsException("用户账户已过期");
        }

        // 检查账户是否未锁定
        if (!userDetails.isAccountNonLocked()) {
            throw new BadCredentialsException("用户账户已被锁定");
        }

        // 检查凭据是否未过期
        if (!userDetails.isCredentialsNonExpired()) {
            throw new BadCredentialsException("用户凭据已过期");
        }

        // 创建认证token
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails, password, userDetails.getAuthorities());
        
        authToken.setDetails(authentication.getDetails());
        
        return authToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
