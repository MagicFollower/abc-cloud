package com.abc.system.common.consumer;

import com.abc.system.common.config.RocketMQConfigProperties;
import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.RocketMQException;
import com.abc.system.common.vo.RocketMQConsumerVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.List;

/**
 * AbstractMQConsumer
 * <pre>
 * 使用方式⤵️
 * {@code
 *     @Slf4j
 *     @Component
 *     public class MyConsumer02 extends AbstractMQConsumer {
 *
 *         // PostConstruct注解修饰的方法会在bean初始化完成后调用。它的作用是在bean的所有依赖关系注入完成之后进行一些初始化操作。
 *         @PostConstruct
 *         private void start() {
 *             RocketMQConsumerVO mqConsumerVO = RocketMQConsumerVO.builder()
 *                     .group("consumer-group-01-name")
 *                     .topic("my-topic")
 *                     .messageModel(MessageModel.CLUSTERING).build();
 *             super.registryConsumer(mqConsumerVO);
 *             log.info(">>>>>>>>>>>>|MyConsumer02消费者启动了\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80|<<<<<<<<<<<<");
 *         }
 *
 *         @Override
 *         protected ConsumeConcurrentlyStatus onMessage(MessageExt messageExt) {
 *             String messageBody = new String(messageExt.getBody(), StandardCharsets.UTF_8);
 *             log.info(">>>>>>>>>>>>|MyConsumer02 get Message → {}", messageBody);
 *             return CONSUME_SUCCESS;
 *         }
 *     }
 *     }
 * </pre>
 *
 * @Description AbstractMQConsumer
 * @Author -
 * @Date 2023/8/17 20:56
 * @Version 1.0
 */
public abstract class AbstractMQConsumer implements MessageListenerConcurrently, ApplicationContextAware {
    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    protected RocketMQConsumerVO rocketMQConsumerVO;

    private ApplicationContext context;

    /**
     * 子类重写onMessage消费方法
     * <pre>
     * 1.MessageExt是RocketMQ中的消息扩展类，用于表示从消息队列中消费到的消息。
     *   → 它是对RocketMQ原生消息(Message)的进一步封装，提供了更多的属性和方法来处理和获取消息的相关信息。
     * 2.你依然可以从中直接获取其父类Message中的body消息体(byte[])
     * </pre>
     *
     * @param messageExt MessageExt
     * @return ConsumeConcurrentlyStatus[CONSUME_SUCCESS、RECONSUME_LATER]
     */
    protected abstract ConsumeConcurrentlyStatus onMessage(MessageExt messageExt);

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

    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList,
                                                    ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        this.LOGGER.info(">>>>>>>>|receive mq message|message size:{}|<<<<<<<<", messageExtList.size());
        for (MessageExt messageExt : messageExtList) {
            ConsumeConcurrentlyStatus consumeConcurrentlyStatus = onMessage(messageExt);
            if(consumeConcurrentlyStatus.equals(ConsumeConcurrentlyStatus.RECONSUME_LATER)){
                this.LOGGER.info(">>>>>>>>|receive mq message|process abort|\uD83E\uDD74error-keys:[{}]|<<<<<<<<", messageExt.getKeys());
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        this.LOGGER.info(">>>>>>>>|receive mq message|process end|success|<<<<<<<<");
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    private boolean checkMQParam(String param) {
        return StringUtils.isEmpty(param);
    }

    private void initConsumer() {
        this.LOGGER.info(">>>>>>>>|init rocket mq consumer|start|<<<<<<<<");
        // 获取RocketMQ配置属性，使用到配置中的NameServers
        RocketMQConfigProperties rocketMQConfigProperties = context.getBean(RocketMQConfigProperties.class);
        if (StringUtils.isEmpty(rocketMQConfigProperties.getNameServers())) {
            throw new RocketMQException(">>>>>>>>|rocket mq name sever is empty|<<<<<<<<");
        } else {
            try {
                RocketMQConsumer consumer = new RocketMQConsumer();
                consumer.setNamesrvAddr(rocketMQConfigProperties.getNameServers());
                consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
                consumer.setConsumerGroup(this.rocketMQConsumerVO.getGroup());
                int maxReconsumeTimes = this.rocketMQConsumerVO.getMaxReconsumeTimes();
                if (maxReconsumeTimes > 0 && maxReconsumeTimes <= 16) {
                    // default=-1 => In concurrent mode, -1 means 16; In orderly mode, -1 means Integer.MAX_VALUE.
                    // 错误重试间隔：
                    // 1.🔍RocketMQ中支持延迟消息，延迟消息分为19个级别： [0s 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h]
                    // 2.🔍错误重试间隔分为16个级别，从10s开始：[10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h]
                    consumer.setMaxReconsumeTimes(maxReconsumeTimes);
                }

                String tags = this.rocketMQConsumerVO.getTags();
                if (StringUtils.isEmpty(tags)) {
                    tags = "*";
                }

                consumer.subscribe(this.rocketMQConsumerVO.getTopic(), tags);
                // consumer.registerMessageListener(this::consumeMessage);
                consumer.registerMessageListener(this);
                consumer.start();
                this.LOGGER.info(">>>>>>>>|init rocket mq consumer|success|<<<<<<<<");
            } catch (MQClientException e) {
                this.LOGGER.error(">>>>>>>>|init rocket mq consumer failed|exception:{}|<<<<<<<<", e.getMessage(), e);
                throw new RocketMQException(SystemRetCodeConstants.MQ_INIT_ERROR, e);
            }
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
