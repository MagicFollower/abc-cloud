package com.abc.system.apollo.autoconfig;

import com.abc.system.apollo.context.EnvironmentChangeEvent;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * Apollo原生配置自动配置类
 *
 * @Description 自动刷新Apollo配置信息，监听apollo中{apollo.bootstrap.namespaces}配置的namespaces
 * @Author [author_name]
 * @Date 2077/5/12 20:25
 * @Version 1.0
 */
@Slf4j
@Configuration
@ConditionalOnClass(ConfigService.class)  // {@code com.ctrip.framework.apollo.ConfigService }
@ConditionalOnProperty(prefix = ApolloConfigRefreshAutoConfiguration.APOLLO_CONFIG_PREFIX, name = "enabled", havingValue = "true")
public class ApolloConfigRefreshAutoConfiguration implements ApplicationContextAware {

    /**
     * apollo配置匹配前缀
     */
    public static final String APOLLO_CONFIG_PREFIX = "apollo.bootstrap";

    /**
     * spring上下文
     */
    private ApplicationContext applicationContext;

    /**
     * 指定加载的namespaces
     */
    @Value("${apollo.bootstrap.namespaces}")
    private String[] namespaces;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 自动监听Apollo配置信息变化事件，当发生变化时更新到应用
     */
    @PostConstruct
    public void addRefreshListener() {
        Arrays.stream(namespaces).forEach(namespaces -> {
            // 取得对应的namespaces配置实例
            Config config = ConfigService.getConfig(namespaces);
            // 为每个namespace注册变更监听事件
            config.addChangeListener(changeEvent -> {
                changeEvent.changedKeys().forEach(key -> log.info(">>>>>>>>>>> refresh Event start|" +
                                "namespaces:{}|changeType:{}|key:{}|old-value:{}|new-value:{} <<<<<<<<<<<",
                        changeEvent.getNamespace(), changeEvent.getChange(key).getChangeType(), key,
                        changeEvent.getChange(key).getOldValue(), changeEvent.getChange(key).getNewValue()));
                // 将变更应用到上下文中
                this.applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
            });
        });
    }
}
