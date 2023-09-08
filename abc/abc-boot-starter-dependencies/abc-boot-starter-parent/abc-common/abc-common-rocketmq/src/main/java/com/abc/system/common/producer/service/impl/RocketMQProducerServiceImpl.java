package com.abc.system.common.producer.service.impl;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.RocketMQException;
import com.abc.system.common.producer.RocketMQProducer;
import com.abc.system.common.producer.service.IRocketMQProducerService;
import com.abc.system.common.util.RocketMQUtils;
import com.abc.system.common.vo.RocketMQProducerMessageVO;
import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * RocketMQ生产者接口实现（自动注入 → heilan.rocketmq.producer.enable）
 * <pre>
 * 🔍示例一
 * {@code
 *    @GetMapping("/demo01")
 *     public String demo01() {
 *         String currentTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now());
 *         RocketMQProducerMessageVO rocketMQProducerMessageVO = RocketMQProducerMessageVO.builder()
 *                 .topic("my-topic")
 *                 .content("hello,world → " + currentTime).build();
 *         SendResult sendResult = rocketMQProducerService.sendOrdered(rocketMQProducerMessageVO);
 *
 *         return JSONObject.toJSONString(sendResult, JSONWriter.Feature.PrettyFormat);
 *     }
 * }
 *
 * 🔍示例二：设置消息延迟发送（适用于主子表更新时，主表更新后延迟N秒后发送子表同步消息，子表消息在延迟后可正常获得主表同步后的数据，2/3[5s/10s]即可）
 * {@code
 *     @RestController
 *     @RequiredArgsConstructor
 *     public class DemoController {
 *
 *         private final IRocketMQProducerService rocketMQProducerService;
 *
 *         @GetMapping("/demo01")
 *         public String demo01() {
 *             String currentTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now());
 *             RocketMQProducerMessageVO rocketMQProducerMessageVO = RocketMQProducerMessageVO.builder()
 *                     .topic("my-topic")
 *                     .delayTimeLevel(2)
 *                     .content("111 → hello,world → " + currentTime).build();
 *             SendResult sendResult = rocketMQProducerService.sendOrdered(rocketMQProducerMessageVO);
 *
 *             RocketMQProducerMessageVO rocketMQProducerMessageVO1 = RocketMQProducerMessageVO.builder()
 *                     .topic("my-topic")
 *                     .delayTimeLevel(0)
 *                     .content("222 → hello,world → " + currentTime).build();
 *             SendResult sendResult1 = rocketMQProducerService.sendOrdered(rocketMQProducerMessageVO1);
 *
 *             return "200";
 *         }
 *
 *     }
 * }
 * </pre>
 *
 * @Description RocketMQ生产者接口实现（自动注入 → heilan.rocketmq.producer.enable）
 * @Author -
 * @Date 2023/8/17 19:42
 * @Version 1.0
 */
public class RocketMQProducerServiceImpl implements IRocketMQProducerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQProducerServiceImpl.class);

    @Autowired
    private RocketMQProducer rocketMQProducer;

    public RocketMQProducerServiceImpl() {
    }

    public SendResult send(RocketMQProducerMessageVO message) throws RocketMQException {
        LOGGER.info(">>>>>>>>|send mq message|start|sync|topic:{}|tags:{}|messageKey:{}|<<<<<<<<",
                message.getTopic(), message.getTags(), message.getMsgKey());
        this.checkMQMessageAndFillMsgKey(message);
        Message sendMsg;

        try {
            sendMsg = RocketMQUtils.convertToMessage(message);
            SendResult sendResult = this.rocketMQProducer.send(sendMsg);
            LOGGER.info(">>>>>>>>|send mq message|end|sync|topic:{}|tag:{}|messageKey:{}|messageId:{}|status:{}|<<<<<<<<",
                    message.getTopic(),
                    message.getTags(),
                    message.getMsgKey(),
                    sendResult.getMsgId(),
                    sendResult.getSendStatus().name());
            return sendResult;
        } catch (InterruptedException | RemotingException | MQBrokerException | MQClientException e) {
            LOGGER.error(">>>>>>>>|send message failed|sync|exception:{}|<<<<<<<<", e.getMessage(), e);
            throw new RocketMQException(">>>>>>>>|send sync message failed|<<<<<<<<");
        }
    }

    public SendResult sendOrdered(RocketMQProducerMessageVO message) throws RocketMQException {
        LOGGER.info(">>>>>>>>|send orderly mq message|sync|topic:{}|tag:{}|messageKey:{}|start|<<<<<<<<",
                message.getTopic(), message.getTags(), message.getMsgKey());
        this.checkMQMessageAndFillMsgKey(message);
        Message sendMsg;

        try {
            sendMsg = RocketMQUtils.convertToMessage(message);
            // 该代码片段使用了自定义的MessageQueueSelector实现，根据消息的msgKey来选择消息被发送到的消息队列。
            // 这样可以确保具有相同msgKey的消息被发送到同一个消息队列，实现顺序消息的发送和消费。
            // 🔍send#3参数版本，参数三中的属性会作为参数二中select方法的第三个参数
            // 🔍😄当然，你可以使用官方提供的SelectMessageQueueByHash（基于参数三HashCode的消息队列选择器），也可以自己手动控制（如下）。
            SendResult sendResult = this.rocketMQProducer.send(sendMsg, new MessageQueueSelector() {
                public MessageQueue select(List<MessageQueue> mqs, Message message, Object msgKey) {
                    int code;
                    if (msgKey instanceof String) {
                        code = DigestUtils.md5Hex((String) msgKey).hashCode();
                        if (code < 0) {
                            code = Math.abs(code);
                        }
                    } else {
                        // 统一控制RocketMQProducerMessageVO#msgKey为String
                        LOGGER.error(">>>>>>>>|send orderly message failed|msgKey type only support String|<<<<<<<<");
                        throw new RocketMQException(SystemRetCodeConstants.MQ_MESSAGE_FORMAT_ERROR);
                    }
                    int index = code % mqs.size();
                    return mqs.get(index);
                }
            }, message.getMsgKey());
            LOGGER.info(">>>>>>>>|send orderly mq message|sync|topic:{}|tag:{}|messageKey:{}|messageId:{}|status:{}|end|<<<<<<<<",
                    message.getTopic(),
                    message.getTags(),
                    message.getMsgKey(),
                    sendResult.getMsgId(),
                    sendResult.getSendStatus().name());
            return sendResult;
        } catch (InterruptedException | RemotingException | MQBrokerException | MQClientException var5) {
            LOGGER.error(">>>>>>>>|send orderly message failed|sync|exception:|<<<<<<<<", var5);
            throw new RocketMQException(SystemRetCodeConstants.MQ_MESSAGE_SEND_ERROR);
        }
    }

    public void sendOneWay(RocketMQProducerMessageVO message) throws RocketMQException {
        LOGGER.info(">>>>>>>>|send mq message|start|oneWay|topic:{}|tag:{}|messageKey:{}|<<<<<<<<",
                message.getTopic(), message.getTags(), message.getMsgKey());
        this.checkMQMessageAndFillMsgKey(message);
        Message sendMsg;

        try {
            sendMsg = RocketMQUtils.convertToMessage(message);
            this.rocketMQProducer.sendOneway(sendMsg);
            LOGGER.info(">>>>>>>>|send message|success|oneWay|topic:{}|tag:{}|messageKey:{}|<<<<<<<<",
                    message.getTopic(), message.getTags(), message.getMsgKey());
        } catch (InterruptedException | RemotingException | MQClientException e) {
            LOGGER.error(">>>>>>>>|send message failed|exception:{}|<<<<<<<<", e.getMessage(), e);
            throw new RocketMQException(SystemRetCodeConstants.MQ_MESSAGE_SEND_ERROR);
        }
    }

    public void sendAsync(RocketMQProducerMessageVO message, SendCallback sendCallback) throws RocketMQException {
        LOGGER.info(">>>>>>>>|send mq message|start|async|topic:{}|tag:{}|messageKey:{}|<<<<<<<<",
                message.getTopic(), message.getTags(), message.getMsgKey());
        this.checkMQMessageAndFillMsgKey(message);
        Message sendMsg;

        try {
            sendMsg = RocketMQUtils.convertToMessage(message);
            this.rocketMQProducer.send(sendMsg, sendCallback);
            LOGGER.info(">>>>>>>>|send mq message|success|async|topic:{}|tag:{}|messageKey:{}|<<<<<<<<",
                    message.getTopic(), message.getTags(), message.getMsgKey());
        } catch (InterruptedException | RemotingException | MQClientException e) {
            LOGGER.error(">>>>>>>>|send message failed|async|exception:{}|<<<<<<<<", e.getMessage(), e);
            throw new RocketMQException(SystemRetCodeConstants.MQ_MESSAGE_SEND_ERROR);
        }
    }

    public SendResult sendBatch(List<RocketMQProducerMessageVO> messages) throws RocketMQException {
        if (CollectionUtils.isEmpty(messages)) {
            LOGGER.error(">>>>>>>>|send batch mq message|failed|message empty|<<<<<<<<");
            throw new RocketMQException(SystemRetCodeConstants.MQ_MESSAGE_LIST_EMPTY);
        } else {
            RocketMQProducerMessageVO mqMessage = messages.get(0);
            LOGGER.info(">>>>>>>>|send batch mq message|start|sync|topic:{}|tag:{}|messageKey:{}|<<<<<<<<",
                    mqMessage.getTopic(), mqMessage.getTags(), mqMessage.getMsgKey());
            this.checkMQMessageAndFillMsgKey(mqMessage);
            List<Message> messageList = new ArrayList<>();
            messages.forEach((rocketMQProducerMessageVO)
                    -> messageList.add(RocketMQUtils.convertToMessage(rocketMQProducerMessageVO)));

            try {
                SendResult sendResult = this.rocketMQProducer.send(messageList);
                LOGGER.info(">>>>>>>>|send batch mq message|end|sync|topic:{}|tags:{}|sendResult:{}|<<<<<<<<",
                        mqMessage.getTopic(), mqMessage.getTags(), JSON.toJSON(sendResult));
                return sendResult;
            } catch (InterruptedException | RemotingException | MQBrokerException | MQClientException e) {
                LOGGER.error(">>>>>>>>|send batch message failed|sync|exception:{}|<<<<<<<<", e.getMessage(), e);
                throw new RocketMQException(SystemRetCodeConstants.MQ_MESSAGE_SEND_ERROR);
            }
        }
    }

    private void checkMQMessageAndFillMsgKey(RocketMQProducerMessageVO message) {
        if (null == message) {
            LOGGER.error(">>>>>>>>|message is empty(null)|<<<<<<<<");
            throw new RocketMQException(SystemRetCodeConstants.MQ_MESSAGE_EMPTY);
        } else if (checkMQParam(message.getTopic())) {
            LOGGER.error(">>>>>>>>|message-topic is empty|<<<<<<<<");
            throw new RocketMQException(SystemRetCodeConstants.MQ_MESSAGE_LOST_TOPIC);
        } else {
            if (StringUtils.isEmpty(message.getMsgKey())) {
                String uuid = UUID.randomUUID().toString().replace("-", "");
                message.setMsgKey(uuid);
            }
        }
    }

    private boolean checkMQParam(String param) {
        return StringUtils.isEmpty(param);
    }
}
