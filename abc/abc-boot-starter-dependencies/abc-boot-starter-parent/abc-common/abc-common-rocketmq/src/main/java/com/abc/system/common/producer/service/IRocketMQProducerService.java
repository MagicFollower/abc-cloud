package com.abc.system.common.producer.service;

import com.abc.system.common.exception.RocketMQException;
import com.abc.system.common.vo.RocketMQProducerMessageVO;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;

import java.util.List;

/**
 * RocketMQ生产者接口定义
 * <pre>
 * 1.单条
 *   1.1 同步无序send
 *   1.2 同步有序sendOrdered
 *   1.3 异步无回调sendOneWay
 *   1.4 异步有回调sendAsync
 * 2.多条
 *   2.1 同步无序sendBatch
 * </pre>
 *
 * @Description RocketMQ生产者接口定义
 * @Author -
 * @Date 2023/8/17 18:57
 * @Version 1.0
 */
public interface IRocketMQProducerService {

    /**
     * 同步单条发送
     *
     * @param mqMessage RocketMQMessage
     * @return SendResult
     * @throws RocketMQException RocketMQException
     */
    SendResult send(RocketMQProducerMessageVO mqMessage) throws RocketMQException;

    /**
     * 同步单条发送（有序）
     *
     * @param mqMessage RocketMQMessage
     * @return SendResult
     * @throws RocketMQException RocketMQException
     */
    SendResult sendOrdered(RocketMQProducerMessageVO mqMessage) throws RocketMQException;

    /**
     * 异步单条发送
     *
     * @param mqMessage RocketMQMessage
     * @throws RocketMQException RocketMQException
     */
    void sendOneWay(RocketMQProducerMessageVO mqMessage) throws RocketMQException;

    /**
     * 异步单条发送（指定回调函数）
     *
     * @param mqMessage    RocketMQMessage
     * @param sendCallback SendCallback
     * @throws RocketMQException RocketMQException
     */
    void sendAsync(RocketMQProducerMessageVO mqMessage, SendCallback sendCallback) throws RocketMQException;

    /**
     * 同步批量发送
     *
     * @param mqMessageList List of RocketMQMessage
     * @return SendResult
     * @throws RocketMQException RocketMQException
     */
    SendResult sendBatch(List<RocketMQProducerMessageVO> mqMessageList) throws RocketMQException;
}
