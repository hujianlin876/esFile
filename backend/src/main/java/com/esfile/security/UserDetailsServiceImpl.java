package com.esfile.security;

import com.esfile.entity.mybatis.User;
import com.esfile.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户详情服务实现类
 * 实现Spring Security的UserDetailsService接口
 * 
 * @author esfile
 * @since 1.0.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // 根据用户名查找用户
            User user = userService.getUserByUsername(username);
            
            if (user == null) {
                throw new UsernameNotFoundException("用户不存在: " + username);
            }

            // 检查用户状态
            if (!"1".equals(user.getStatus())) {
                throw new UsernameNotFoundException("用户账户已被禁用: " + username);
            }

            // 获取用户角色和权限
            List<String> authorities = new ArrayList<>();
            
            // 获取用户角色（通过UserService方法）
            List<Map<String, Object>> userRoles = userService.getUserRoles(user.getId());
            if (userRoles != null && !userRoles.isEmpty()) {
                authorities.addAll(userRoles.stream()
                    .map(role -> "ROLE_" + role.get("code"))
                    .collect(Collectors.toList()));
            }
            
            // 暂时不添加权限，因为User实体中没有直接的权限字段
            // 可以通过角色来获取权限，这里简化处理

            // 转换为Spring Security的权限格式
            List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

            // 创建UserDetails对象
            return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(grantedAuthorities)
                .accountExpired(false)  // 账户未过期
                .accountLocked(user.isLocked())   // 根据锁定状态判断
                .credentialsExpired(false) // 凭据未过期
                .disabled(!"1".equals(user.getStatus())) // 根据状态判断是否启用
                .build();

        } catch (Exception e) {
            throw new UsernameNotFoundException("加载用户信息失败: " + username, e);
        }
    }
}
