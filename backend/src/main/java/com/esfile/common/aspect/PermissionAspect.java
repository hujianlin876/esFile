package com.esfile.common.aspect;
import com.esfile.common.annotation.Permission;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
/**
 * 权限切面
 */
@Aspect
@Component
public class PermissionAspect {
    @Before("@annotation(permission)")
    public void before(JoinPoint joinPoint, Permission permission) {
        // TODO: 权限校验逻辑
    }
}
