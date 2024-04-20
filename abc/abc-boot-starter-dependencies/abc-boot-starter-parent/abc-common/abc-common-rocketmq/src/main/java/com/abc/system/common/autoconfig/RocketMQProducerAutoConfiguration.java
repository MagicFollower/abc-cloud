package com.abc.system.common.autoconfig;

import com.abc.system.common.producer.RocketMQProducer;
import com.abc.system.common.producer.service.IRocketMQProducerService;
import com.abc.system.common.producer.service.impl.RocketMQProducerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RocketMQProducerAutoConfiguration
 * <pre>
 * 1.在RocketMQProducer注入后启动当前类的自动配置行为（RocketMQProducer由ConditionalOnProperty+abc.rocketmq.producer.enable控制）
 * </pre>
 *
 * @Description RocketMQProducerAutoConfiguration
 * @Author -
 * @Date 2077/8/17 19:44
 * @Version 1.0
 */
@Slf4j
@Configuration
@AutoConfigureAfter({RocketMQProducer.class})
public class RocketMQProducerAutoConfiguration {
    public RocketMQProducerAutoConfiguration() {
    }

    @Bean
    public IRocketMQProducerService rocketMQProducerService() {
        log.info(">>>>>>>>|Inject RocketMQProducerServiceImpl to IOC|<<<<<<<<");
        return new RocketMQProducerServiceImpl();
    }
}
