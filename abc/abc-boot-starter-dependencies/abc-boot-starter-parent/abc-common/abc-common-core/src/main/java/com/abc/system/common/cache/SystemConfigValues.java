package com.abc.system.common.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * 系统级缓存，独立于Spring自动配置，从Apollo配置加载全局配置
 *   → 使用位置（common-apollo-client）：com.abc.system.apollo.autoconfig.SystemValuesAutoConfiguration
 * </pre>
 * @Description 系统级缓存，独立于Spring自动配置，从Apollo配置加载全局配置（apollo.system.namespace）
 * @Author Trivis
 * @Date 2023/5/12 20:41
 * @Version 1.0
 */
@Slf4j
public final class SystemConfigValues {

    /**
     * 存储系统配置信息
     */
    private static final Map<String, String> SYSTEM_CONFIG_VALUES = new ConcurrentHashMap<>();

    private SystemConfigValues() {

    }

    /**
     * 获取指定KEY的值
     *
     * @param key KEY
     * @return 返回对应KEY的值
     */
    public static String get(String key) {
        return SYSTEM_CONFIG_VALUES.get(key);
    }

    /**
     * 获取指定KEY的值，如果对应KEY不存在，返回给定的默认值
     *
     * @param key          KEY
     * @param defaultValue 默认值
     * @return VALUE: String
     */
    public static String get(String key, String defaultValue) {
        // KEY存在，直接返回对应值
        if (StringUtils.isNoneEmpty(SYSTEM_CONFIG_VALUES.get(key))) {
            return SYSTEM_CONFIG_VALUES.get(key);
        }

        return defaultValue;
    }

    /**
     * 将配置信息放入缓存
     *
     * @param key   KEY
     * @param value VALUE
     */
    public static void put(String key, String value) {
        SYSTEM_CONFIG_VALUES.put(key, value);
    }

    /**
     * 将配置信息放入缓存
     *
     * @param maps MAP
     */
    public static void put(Map<String, String> maps) {
        SYSTEM_CONFIG_VALUES.putAll(maps);
    }

    /**
     * 删除指定KEY对应的值
     *
     * @param key KEY
     */
    public static void delete(String key) {
        SYSTEM_CONFIG_VALUES.remove(key);
    }
}
