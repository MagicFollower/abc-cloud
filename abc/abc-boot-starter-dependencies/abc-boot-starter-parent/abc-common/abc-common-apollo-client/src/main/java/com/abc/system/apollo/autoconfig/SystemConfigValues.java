package com.abc.system.apollo.autoconfig;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * 系统级缓存，独立于Spring自动配置，从Apollo配置加载全局配置
 *   → 使用位置（common-apollo-client）：com.abc.system.apollo.autoconfig.SystemValuesAutoConfiguration
 * </pre>
 * @Description
 * <pre>
 * 系统级缓存，独立于Spring自动配置，从Apollo配置加载全局配置（apollo.system.namespace）
 *   → 示例：{@code com.abc.business.fastlink.portal.controller.order.OrderController#testSystemValues}
 * {@code
 *     @LogAnchor
 *     @PostMapping("/testSystemValues")
 *     public void testSystemValues() {
 *         String s = SystemConfigValues.get("not_need_login.urls");
 *         List<String> notNeedLoginUrls = Arrays.stream(s.split(",")).collect(Collectors.toList());
 *         System.out.println("notNeedLoginUrls = " + notNeedLoginUrls);
 *     }
 * }
 * </pre>
 * @Author Trivis
 * @Date 2023/05/21 14:18
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

    // ############################################################################
    // #####分布式微服务环境下，不推荐本地级别的缓存，写入操作会使得不同服务内出现数据不一致的情况
    // #####  1.建议使用分布式缓存（redis）
    // #####  2.如果使用本地缓存，请不要开放缓存数据的写能力
    // #####    2.1 此处对外关闭了写、删除API
    // #####    2.2 put仅在SystemValuesAutoConfiguration使用/生效
    // #####    2.3 未来版本计划移除移除putAll+delete，或者废弃本地缓存使用分布式缓存
    // #####  时间：2023年5月31日22:17:46
    // ############################################################################

    /**
     * 将配置信息放入缓存
     *
     * @param key   KEY
     * @param value VALUE
     */
    static void put(String key, String value) {
        SYSTEM_CONFIG_VALUES.put(key, value);
    }

    /**
     * 将配置信息放入缓存
     *
     * @param maps MAP
     */
    static void put(Map<String, String> maps) {
        SYSTEM_CONFIG_VALUES.putAll(maps);
    }

    /**
     * 删除指定KEY对应的值
     *
     * @param key KEY
     */
    static void delete(String key) {
        SYSTEM_CONFIG_VALUES.remove(key);
    }
}
