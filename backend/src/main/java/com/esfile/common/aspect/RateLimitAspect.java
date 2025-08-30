package com.esfile.common.aspect;
import com.esfile.common.annotation.RateLimit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
/**
 * 限流切面
 */
@Aspect
@Component
public class RateLimitAspect {
    @Before("@annotation(rateLimit)")
    public void before(JoinPoint joinPoint, RateLimit rateLimit) {
        // TODO: 限流逻辑
    }
}
