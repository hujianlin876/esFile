package com.esfile.common.aspect;
import com.esfile.common.annotation.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
/**
 * 日志切面
 */
@Aspect
@Component
public class LogAspect {
    @Before("@annotation(log)")
    public void before(JoinPoint joinPoint, Log log) {
        // TODO: 记录操作日志
    }
}
