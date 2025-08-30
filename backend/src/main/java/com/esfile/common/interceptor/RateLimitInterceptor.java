package com.esfile.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 限流拦截器
 * 基于Redis的令牌桶算法实现请求限流
 * 
 * @author esfile
 * @since 1.0.0
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 限流配置
    private static final int MAX_REQUESTS_PER_MINUTE = 100; // 每分钟最大请求数
    private static final int MAX_REQUESTS_PER_HOUR = 1000;  // 每小时最大请求数
    private static final String RATE_LIMIT_PREFIX = "rate_limit:";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = getClientIpAddress(request);
        String requestURI = request.getRequestURI();
        
        // 检查分钟级限流
        if (!checkRateLimit(clientIp, requestURI, "minute", MAX_REQUESTS_PER_MINUTE, 60)) {
            response.setStatus(429);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":429,\"message\":\"请求过于频繁，请稍后再试\"}");
            return false;
        }
        
        // 检查小时级限流
        if (!checkRateLimit(clientIp, requestURI, "hour", MAX_REQUESTS_PER_HOUR, 3600)) {
            response.setStatus(429);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":429,\"message\":\"请求过于频繁，请稍后再试\"}");
            return false;
        }
        
        return true;
    }

    /**
     * 检查限流
     */
    private boolean checkRateLimit(String clientIp, String requestURI, String timeUnit, int maxRequests, int seconds) {
        String key = RATE_LIMIT_PREFIX + clientIp + ":" + requestURI + ":" + timeUnit;
        
        // 获取当前计数
        Integer currentCount = (Integer) redisTemplate.opsForValue().get(key);
        
        if (currentCount == null) {
            // 第一次请求，设置计数为1，过期时间为指定秒数
            redisTemplate.opsForValue().set(key, 1, seconds, TimeUnit.SECONDS);
            return true;
        }
        
        if (currentCount >= maxRequests) {
            // 超过限制
            return false;
        }
        
        // 增加计数
        redisTemplate.opsForValue().increment(key);
        return true;
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
