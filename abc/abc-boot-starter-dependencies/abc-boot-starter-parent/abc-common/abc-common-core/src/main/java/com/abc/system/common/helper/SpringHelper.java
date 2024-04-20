package com.abc.system.common.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * SpringHelper
 *
 * @Description SpringHelper Spring帮助器，区别于Utils，Helper工具会存在于SpringIOC容器
 * @Author [author_name]
 * @Date 2077/5/10 20:10
 * @Version 1.0
 */
@SuppressWarnings({"unchecked"})
public class SpringHelper implements ApplicationContextAware {
    private static ApplicationContext APPLICATION_CONTEXT;

    public SpringHelper() {
    }

    public static <T> T getBean(String beanName) {
        return APPLICATION_CONTEXT.containsBean(beanName) ? (T) APPLICATION_CONTEXT.getBean(beanName) : null;
    }

    public static <T> T getBean(Class<T> className) {
        return APPLICATION_CONTEXT.getBean(className);
    }

    public static <T> Map<String, T> getBeansByType(Class<T> baseType) {
        return APPLICATION_CONTEXT.getBeansOfType(baseType);
    }

    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
    }
}
