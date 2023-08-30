package com.abc.system.common.util;

import com.abc.system.common.vo.RocketMQProducerMessageVO;
import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * RocketMQUtils
 *
 * @Description RocketMQUtils
 * @Author -
 * @Date 2023/8/17 19:10
 * @Version 1.0
 */
public class RocketMQUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQUtils.class);

    private RocketMQUtils() {
    }

    /**
     * 将自定义Message转换为RocketMQ格式Message
     *
     * @param mqMessage RocketMQMessage
     * @return Message
     */
    public static Message convertToMessage(RocketMQProducerMessageVO mqMessage) {
        Message message = new Message();

        // RocketMQ可以开启消息自动去重，此时会使用到KEYS属性。如果没有开启（默认情况下）这个参数不会有任何作用。
        //   →使用该内置的属性（set/get），你可以在消费者端或生产者端进行手动控制数据的重复检测（可以使用单独的数据表记录已经发送的数据或其他方案）
        String id = mqMessage.getId() == null ? UUID.randomUUID().toString() : mqMessage.getId().toString();
        message.setKeys(id);

        message.setTopic(mqMessage.getTopic());
        message.setTags(mqMessage.getTags());

        // 填充自定义属性bizId（业务ID）
        message.putUserProperty("bizId", id);

        String body;
        if (mqMessage.getContent() instanceof String) {
            body = (String) mqMessage.getContent();
        } else {
            body = JSON.toJSONString(mqMessage.getContent());
        }
        message.setBody(body.getBytes(StandardCharsets.UTF_8));

        // 19级: [0s 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h]
        message.setDelayTimeLevel(mqMessage.getDelayTimeLevel());
        return message;
    }
}
