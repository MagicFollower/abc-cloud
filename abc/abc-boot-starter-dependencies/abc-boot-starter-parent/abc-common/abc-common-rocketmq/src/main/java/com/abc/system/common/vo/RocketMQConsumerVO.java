package com.abc.system.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.rocketmq.remoting.protocol.heartbeat.MessageModel;

/**
 * RocketMQConsumerVO
 *
 * @Description RocketMQConsumerVO
 * @Author -
 * @Date 2023/8/17 20:59
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RocketMQConsumerVO {
    private String group;
    private String topic;
    private String tags;

    /**
     * 最大重新消费次数[默认0，不允许重新消费]
     * <pre>
     * 1.在RocketMQ消费者中，setMaxReconsumeTimes方法用于设置消息重新消费的最大次数。
     *   → 当消费者无法正常处理一条消息时，RocketMQ允许重新消费该消息一定次数，以便处理失败的情况。
     * 2.具体地说，当消息被消费者接收后，如果消费过程中发生了异常或处理失败，消费者可以选择将该消息标记为可重试，并将其重新投递到队列中以便重复消费。
     *   → setMaxReconsumeTimes方法用于设置允许的最大重试次数。
     * 3.当重试次数达到最大重试次数时，消息将不再重新投递，而是根据消费者的配置进行处理。
     *   → 这可能包括将消息发送到一个特定的死信队列(Dead Letter Queue)用于进一步分析异常情况或人工干预处理。
     * </pre>
     */
    private int maxReconsumeTimes;

    /**
     * 消息模式（广播模式、集群模式[默认]）
     * <pre>
     * 1.MessageModel.BROADCASTING
     * 2.MessageModel.CLUSTERING
     * </pre>
     */
    private MessageModel messageModel;
}