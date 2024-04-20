package com.abc.system.common.autoconfig;

import com.abc.system.common.config.RocketMQConfigProperties;
import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.RocketMQException;
import com.abc.system.common.producer.RocketMQProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties({RocketMQConfigProperties.class})
@ConditionalOnClass({DefaultMQProducer.class, DefaultMQPushConsumer.class})
public class RocketMQAutoConfiguration {

    @Autowired
    private RocketMQConfigProperties rocketMQConfigProperties;

    public RocketMQAutoConfiguration() {
    }

    @Bean
    // 使用这种方式，IDEA的spring配置文件会缺少响应的提示，可以使用在代码中检测的方式或单独添加name关联的配置属性
    @ConditionalOnProperty(
            name = {"abc.rocketmq.producer.enable"},
            havingValue = "true",
            matchIfMissing = false
    )
    public RocketMQProducer rocketMQProducer() {
        log.info(">>>>>>>>|TUN ON|init RocketMQ producer|start|<<<<<<<<");
        if (StringUtils.isEmpty(this.rocketMQConfigProperties.getNameServers())) {
            log.error(">>>>>>>>|init RocketMQ nameServers is empty|<<<<<<<<");
            throw new RocketMQException(SystemRetCodeConstants.MQ_INIT_ERROR);
        } else {
            RocketMQProducer rocketMQProducer = new RocketMQProducer();
            rocketMQProducer.setProducerGroup(this.rocketMQConfigProperties.getProducerGroup());
            rocketMQProducer.setNamesrvAddr(this.rocketMQConfigProperties.getNameServers());
            try {
                rocketMQProducer.start();
                log.info(">>>>>>>>|Init RocketMQ producer|success|<<<<<<<<");
                return rocketMQProducer;
            } catch (MQClientException e) {
                log.error(">>>>>>>>|Init RocketMQ producer|failed|exception: {}|<<<<<<<<", e.getMessage(), e);
                throw new RocketMQException(SystemRetCodeConstants.MQ_INIT_ERROR, e);
            }
        }


//        RocketMQProducer rocketMQProducer = new RocketMQProducer();
//        if (this.rocketMQConfigProperties.getProducer().isEnable()) {
//            log.info(">>>>>>>>|TUN ON|init RocketMQ producer|start|<<<<<<<<");
//            if (StringUtils.isEmpty(this.rocketMQConfigProperties.getNameServers())) {
//                log.error(">>>>>>>>|init RocketMQ nameServers is empty|<<<<<<<<");
//                throw new RocketMQException(SystemRetCodeConstants.MQ_INIT_ERROR);
//            } else {
//                RocketMQProducer rocketMQProducer = new RocketMQProducer();
//                rocketMQProducer.setProducerGroup(this.rocketMQConfigProperties.getProducerGroup());
//                rocketMQProducer.setNamesrvAddr(this.rocketMQConfigProperties.getNameServers());
//                try {
//                    rocketMQProducer.start();
//                    log.info(">>>>>>>>|Init RocketMQ producer|success|<<<<<<<<");
//                    return rocketMQProducer;
//                } catch (MQClientException e) {
//                    log.error(">>>>>>>>|Init RocketMQ producer|failed|exception: {}|<<<<<<<<", e.getMessage(), e);
//                    throw new RocketMQException(SystemRetCodeConstants.MQ_INIT_ERROR, e);
//                }
//            }
//        } else {
//            // Caused by: org.springframework.beans.factory.BeanCreationException:
//            //   → Error creating bean with name 'RocketMQProducer': abc.rocketmq.producer.enable=false
//            throw new BeanCreationException("RocketMQProducer", "abc.rocketmq.producer.enable=false");
//        }
    }
}
