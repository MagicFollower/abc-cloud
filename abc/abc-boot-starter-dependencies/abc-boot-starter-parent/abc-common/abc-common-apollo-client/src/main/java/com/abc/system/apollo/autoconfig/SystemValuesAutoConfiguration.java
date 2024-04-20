package com.abc.system.apollo.autoconfig;

import com.abc.system.apollo.config.SystemConfigValuesProperties;
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
 * <pre>
 * 🤗建议每一个配置项值都使用双引号字符串表示
 * 1.自动监听Apollo中属性变动
 * 2.Apollo中属性配置为null时，将自动被解析为空字符串，null使用双引号括起来时，才会被正常解析为普通字符串
 * 3.Apollo中属性被删除时，程序会监听到新值为null，这里会根据新值为null来在全局缓存中移除对应key
 * </pre>
 *
 * @Description 系统级缓存自定义配置自动配置类
 * @Author [author_name]
 * @Date 2077/5/12 20:37
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
            // 将改变后的值放入缓存/移除
            if (configChange.getNewValue() == null) {
                SystemConfigValues.delete(key);
            } else {
                SystemConfigValues.put(key, configChange.getNewValue());
            }
            log.info(">>>>>>>> \uD83E\uDD20Apollo Refresh Event Start|namespaces:{}|" +
                            "changeType:{}|key:{}|old-value:{}|new-value:{} <<<<<<<<",
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
