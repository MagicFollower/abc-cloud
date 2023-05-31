package com.abc.system.apollo.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 系统级缓存自定义配置
 *
 * @Description
 * <pre>
 * 系统级缓存自定义配置
 *   1.apollo.system.namespaces
 *   2.多个namespace使用逗号分隔
 * </pre>
 * @Author Trivis
 * @Date 2023/5/12 20:36
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "apollo.system")
public class SystemConfigValuesProperties {

    /**
     * 需要加载到系统缓存的Apollo对应的namespace，多个namespaces之间使用逗号分隔
     */
    private String namespaces;

    public List<String> getNamespaces() {
        if (StringUtils.isEmpty(namespaces)) {
            return Collections.emptyList();
        }
        return Arrays.asList(namespaces.split(","));
    }

    public void setNamespaces(String namespaces) {
        this.namespaces = namespaces;
    }
}
