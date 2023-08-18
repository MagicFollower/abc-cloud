package com.abc.system.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RocketMQ配置实体类
 * <p>  → Warning：ignoreUnknownFields=false，将不再允许abc.rocketmq配置下存在未知属性！</p>
 *
 * <pre>
 * 配置示例
 * {@code
 *     # RocketMQ
 *     abc:
 *       rocketmq:
 *         producer:
 *           enable: true
 *         nameServers: 192.168.204.102:9876;192.168.204.103:9876
 *         producerGroup: "my-producer-group"
 * }
 * </pre>
 */
@ConfigurationProperties(
        prefix = "abc.rocketmq",
        ignoreUnknownFields = false
)
@Data
public class RocketMQConfigProperties {
    /**
     * 名称服务器，多个服务器使用分号分隔（例如: 192.168.204.102:9876;192.168.204.103:9876）
     */
    private String nameServers;
    /**
     * 生产者组
     */
    private String producerGroup;
    private RocketMQProducerConfig producer = new RocketMQProducerConfig();


    @Data
    public static final class RocketMQProducerConfig {
        /**
         * 是否开启Producer自动注入
         */
        private boolean enable = false;
    }
}
