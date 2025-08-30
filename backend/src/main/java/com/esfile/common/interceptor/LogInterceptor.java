package com.esfile.common.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日志拦截器
 * 用于记录请求日志，包括请求时间、路径、方法等
 * 
 * @author esfile
 * @since 1.0.0
 */
@Component
public class LogInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String remoteAddr = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        
        logger.info("请求开始 - URI: {}, 方法: {}, IP: {}, 时间: {}, User-Agent: {}", 
            requestURI, method, remoteAddr, LocalDateTime.now().format(formatter), userAgent);
        
        // 记录请求开始时间
        request.setAttribute("startTime", System.currentTimeMillis());
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        int status = response.getStatus();
        Long startTime = (Long) request.getAttribute("startTime");
        long duration = startTime != null ? System.currentTimeMillis() - startTime : 0;
        
        if (ex != null) {
            logger.error("请求异常 - URI: {}, 方法: {}, 状态: {}, 耗时: {}ms, 异常: {}", 
                requestURI, method, status, duration, ex.getMessage());
        } else {
            logger.info("请求完成 - URI: {}, 方法: {}, 状态: {}, 耗时: {}ms", 
                requestURI, method, status, duration);
        }
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
