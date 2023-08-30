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
     * <pre>
     * 1.这在处理事务消息时非常重要，因为它可以确保具有相同生产者组的生产者实例在发送消息或检查事务状态时具有相同的行为。
     * 2.在非事务消息的场景中，producerGroup的唯一性对于每个进程来说并不重要，但仍然需要保证唯一以避免潜在的冲突。
     * 🔍为什么事务消息需要确保多个生产者位于同一个生产者组中？
     * 1.分布式事务一致性：分布式事务消息涉及到【两阶段提交】，即预提交消息和最终提交消息。当生产者发送预提交消息后，需要在一定时间内检查事务状态以决定是否提交或回滚事务。将具有相同角色的生产者实例放入同一个生产者组可以确保它们在发送消息和检查事务状态时具有相同的行为，从而保证事务的一致性。
     * 2.事务消息负载均衡：当事务消息的发送量较大时，同一生产者组中的生产者实例可以共同分担发送任务，从而提高整体的处理能力
     * 3.事务消息高可用性：将多个生产者放入同一个生产者组可以实现负载均衡和故障转移。当一个生产者实例不可用时，其他具有相同producerGroup的生产者实例可以接管其工作，从而确保事务消息的持续发送和处理。
     * </pre>
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
