package com.esfile.common.annotation;
import java.lang.annotation.*;
/**
 * 权限注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {
    String value();
}
