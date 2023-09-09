package com.abc.system.common.consumer;

import com.abc.system.common.config.RocketMQConfigProperties;
import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.RocketMQException;
import com.abc.system.common.vo.RocketMQConsumerVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * AbstractOrderlyMQConsumer
 *
 * @Description AbstractOrderlyMQConsumer
 * @Author -
 * @Date 2023/8/17 21:13
 * @Version 1.0
 */
public abstract class AbstractOrderlyMQConsumer implements MessageListenerOrderly {
    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    protected RocketMQConsumerVO rocketMQConsumerVO;
    @Autowired
    private RocketMQConfigProperties rocketMQConfigProperties;

    /**
     * 子类重新onMessage消费方法（模板设计模式）
     *
     * @param messageExt MessageExt
     * @return ConsumeOrderlyStatus
     */
    protected abstract ConsumeOrderlyStatus onMessage(MessageExt messageExt);

    /**
     * 子类调用registryConsumer传入RocketMQConsumerVO
     *
     * @param rocketMQConsumerVO RocketMQConsumerVO
     */
    protected void registryConsumer(RocketMQConsumerVO rocketMQConsumerVO) {
        if (null == rocketMQConsumerVO) {
            this.LOGGER.error(">>>>>>>>|rocket MQ Consumer VO empty|<<<<<<<<");
            throw new RocketMQException(SystemRetCodeConstants.MQ_MESSAGE_EMPTY.getCode(),
                    "rocket MQ Consumer VO empty");
        } else if (checkMQParam(rocketMQConsumerVO.getTopic())) {
            this.LOGGER.error(">>>>>>>>|rocket MQ Consumer VO topic is empty|<<<<<<<<");
            throw new RocketMQException(SystemRetCodeConstants.MQ_MESSAGE_LOST_TOPIC.getCode(),
                    "rocket MQ Consumer VO topic is empty");
        } else if (checkMQParam(rocketMQConsumerVO.getGroup())) {
            this.LOGGER.error(">>>>>>>>|rocket MQ Consumer VO group is empty|<<<<<<<<");
            throw new RocketMQException(SystemRetCodeConstants.MQ_MESSAGE_LOST_GROUP.getCode(),
                    "rocket MQ Consumer VO group is empty");
        } else {
            this.rocketMQConsumerVO = rocketMQConsumerVO;
            MessageModel messageModel = rocketMQConsumerVO.getMessageModel();
            if (!StringUtils.isEmpty(messageModel.name())
                    && Arrays.asList(MessageModel.CLUSTERING.name(), MessageModel.BROADCASTING.name())
                    .contains(messageModel.name())) {
                this.initConsumer();
            } else {
                this.LOGGER.error(">>>>>>>>|Consumption mode only supports broadcast mode and cluster mode|<<<<<<<<");
                throw new IllegalArgumentException("Consumption mode only supports broadcast mode and cluster mode");
            }
        }
    }

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messageExtList,
                                               ConsumeOrderlyContext consumeOrderlyContext) {
        this.LOGGER.info(">>>>>>>>|receive mq orderly message |message size:{}|<<<<<<<<", messageExtList.size());
        for (MessageExt messageExt : messageExtList) {
            ConsumeOrderlyStatus consumeOrderlyStatus = onMessage(messageExt);
            if (consumeOrderlyStatus.equals(ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT)) {
                this.LOGGER.info(">>>>>>>>|receive mq orderly message|process abort|" +
                        "current queue will be stopped for some time!|" +
                        "\uD83E\uDD74error-keys:[{}]|<<<<<<<<", messageExt.getKeys());
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        }
        this.LOGGER.info(">>>>>>>>|receive mq orderly message|process end|success|<<<<<<<<");
        return ConsumeOrderlyStatus.SUCCESS;
    }

    private boolean checkMQParam(String param) {
        return StringUtils.isEmpty(param);
    }

    private void initConsumer() {
        this.LOGGER.info(">>>>>>>>|init rocket mq orderly consumer|start|<<<<<<<<");
        if (StringUtils.isEmpty(this.rocketMQConfigProperties.getNameServers())) {
            throw new RocketMQException(">>>>>>>>|rocket mq name sever is empty|<<<<<<<<");
        } else {
            try {
                RocketMQConsumer consumer = new RocketMQConsumer();
                consumer.setNamesrvAddr(this.rocketMQConfigProperties.getNameServers());
                consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
                consumer.setConsumerGroup(this.rocketMQConsumerVO.getGroup());
                int maxReconsumeTimes = this.rocketMQConsumerVO.getMaxReconsumeTimes();
                if (maxReconsumeTimes > 0 && maxReconsumeTimes <= 16) {
                    consumer.setMaxReconsumeTimes(maxReconsumeTimes);
                }

                String tags = this.rocketMQConsumerVO.getTags();
                if (StringUtils.isEmpty(tags)) {
                    tags = "*";
                }

                consumer.subscribe(this.rocketMQConsumerVO.getTopic(), tags);
                //consumer.registerMessageListener(this::consumeMessage);
                consumer.registerMessageListener(this);
                consumer.start();
                this.LOGGER.info(">>>>>>>>|init orderly mq consumer rocket mq consumer|success|<<<<<<<<");
            } catch (MQClientException e) {
                this.LOGGER.error(">>>>>>>>|init orderly rocket mq consumer failed|exception:|<<<<<<<<", e);
                throw new RocketMQException(SystemRetCodeConstants.MQ_INIT_ERROR, e);
            }
        }
    }
}