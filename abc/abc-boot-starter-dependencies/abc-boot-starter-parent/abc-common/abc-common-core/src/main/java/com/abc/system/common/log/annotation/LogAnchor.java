package com.abc.system.common.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * LogAnchor 日志锚点
 *
 * @Description LogAnchor 日志锚点
 * @Author Trivis
 * @Date 2023/5/21 14:22
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface LogAnchor {

}