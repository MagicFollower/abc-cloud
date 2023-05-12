package com.abc.system.apollo.autoconfig;

import com.abc.system.apollo.config.SystemConfigValuesProperties;
import com.abc.system.common.cache.SystemConfigValues;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 系统级缓存自定义配置自动配置类
 *
 * @Description 系统级缓存自定义配置自动配置类
 * @Author Trivis
 * @Date 2023/5/12 20:37
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(SystemConfigValuesProperties.class)
@ConditionalOnClass(ConfigService.class)
public class SystemValuesAutoConfiguration {

    private final SystemConfigValuesProperties systemConfigValuesProperties;

    /**
     * 自动监听Apollo配置信息变化事件
     */
    @PostConstruct
    public void loadApolloConfig() {
        // 为每个namespaces注册change监听事件
        systemConfigValuesProperties.getNamespaces().forEach(namespaces -> {
            // 取得对应的namespaces配置实例
            Config config = ConfigService.getConfig(namespaces);
            // 初化加载配置信息至内存
            initConfig(config, namespaces);
            // 注册change事件
            addReferenceListener(config);
        });
    }

    /**
     * 自动监听Apollo配置信息变化事件
     *
     * @param config Apollo Config
     */
    private void addReferenceListener(Config config) {
        // 为每个namespace注册变更监听事件
        config.addChangeListener(changeEvent -> changeEvent.changedKeys().forEach(key -> {
            ConfigChange configChange = changeEvent.getChange(key);
            // 将改变后的值放入缓存
            SystemConfigValues.put(key, configChange.getNewValue());

            log.info(">>>>>>>>>>> refresh Event start|namespaces:{}|" +
                            "changeType:{}|key:{}|old-value:{}|new-value:{} <<<<<<<<<<<",
                    changeEvent.getNamespace(), configChange.getChangeType(), key,
                    configChange.getOldValue(), configChange.getNewValue());
        }));
    }

    /**
     * 初始化加载配置信息
     *
     * @param config     配置信息
     * @param namespaces 命名空间
     */
    private void initConfig(Config config, String namespaces) {
        // 处理对应namespaces中每个KEY
        config.getPropertyNames().forEach(key -> {
            // 取得配置值
            String value = config.getProperty(key, "");
            // 将Apollo配置放入缓存
            SystemConfigValues.put(key, value);

            log.info(">>>>>>>>>>> init system config start|sourceType:{}|namespaces:{}|key:{}|value:{} <<<<<<<<<<<",
                    config.getSourceType().getDescription(), namespaces, key, value);
        });
    }
}
