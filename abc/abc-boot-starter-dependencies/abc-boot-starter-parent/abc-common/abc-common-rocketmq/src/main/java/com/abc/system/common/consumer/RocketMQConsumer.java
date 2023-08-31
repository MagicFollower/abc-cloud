package com.abc.system.common.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;

/**
 * RocketMQConsumer
 *
 * @Description RocketMQConsumer
 * @Author -
 * @Date 2023/8/17 21:06
 * @Version 1.0
 */
public class RocketMQConsumer extends DefaultMQPushConsumer {
    public RocketMQConsumer() {
        // 设置批处理数量（默认1，此处设置用于提示可以修改相关参数）
        //   → 默认=1，在实现MessageListenerConcurrently接口处理消息时，可以更简单的控制错误重试（见AbstractOrderlyMQConsumer#consumeMessage）
        this.setConsumeMessageBatchMaxSize(1);
    }
}
