package com.esfile.common.interceptor;

import com.esfile.common.util.JwtUtil;
import com.esfile.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证拦截器
 * 用于验证JWT token的有效性
 * 
 * @author esfile
 * @since 1.0.0
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的Authorization
        String authHeader = request.getHeader("Authorization");
        
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"缺少认证token\"}");
            return false;
        }

        // 提取token
        String token = authHeader.substring(7);
        
        try {
            // 验证token
            if (!jwtUtil.validateToken(token)) {
                response.setStatus(401);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":401,\"message\":\"token无效或已过期\"}");
                return false;
            }

            // 获取用户ID并设置到请求属性中
            String userId = jwtUtil.getUserIdFromToken(token);
            request.setAttribute("userId", userId);
            
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"token验证失败\"}");
            return false;
        }
    }
}
