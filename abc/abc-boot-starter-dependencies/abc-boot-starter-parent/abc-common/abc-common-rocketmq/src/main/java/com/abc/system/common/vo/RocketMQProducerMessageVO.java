package com.abc.system.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * RocketMQProducerMessageVO[自定义消息结构]
 *
 * @Description RocketMQProducerMessageVO[自定义消息结构]
 * @Author -
 * @Date 2023/8/17 18:59
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RocketMQProducerMessageVO implements Serializable {
    /**
     * 填充Message#Keys
     */
    private Long id;
    /**
     * 填充Message#Topic（必填）
     */
    private String topic;
    /**
     * 填充Message#Tags
     */
    private String tags;
    /**
     * 填充Message#Body
     */
    private Object content;
    /**
     * 填充Message#DelayTimeLevel
     * <pre>
     * 关于DelayTimeLevel级别与时间的对应关系🤔️
     *   → 默认18级: [1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h]
     * </pre>
     */
    private int delayTimeLevel;

    /**
     * 消息顺序发送时使用到的key（会在消息发送时自动生成：checkMQMessageAndFillMsgKey#UUID）
     */
    private String msgKey;
}
