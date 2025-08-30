package com.esfile.common.annotation;
import java.lang.annotation.*;
/**
 * 限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    int value() default 1;
}
