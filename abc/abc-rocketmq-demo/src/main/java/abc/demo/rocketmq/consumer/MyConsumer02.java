package abc.demo.rocketmq.consumer;

import com.abc.system.common.consumer.AbstractMQConsumer;
import com.abc.system.common.vo.RocketMQConsumerVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

import static org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
import static org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus.RECONSUME_LATER;

/**
 * MyConsumer02
 *
 * @Description MyConsumer02
 * @Author -
 * @Date 2077/8/31 18:38
 * @Version 1.0
 */
@Slf4j
@Component
public class MyConsumer02 extends AbstractMQConsumer {

    /**
     * PostConstruct注解修饰的方法会在bean初始化完成后调用。它的作用是在bean的所有依赖关系注入完成之后进行一些初始化操作。
     */
    @PostConstruct
    private void start() throws InterruptedException {
        for (int i = 10; i > 0; i--) {
            System.out.printf("[[[MyConsumer02启动等待中，剩余时间 %ds]]]%n", i);
            Thread.sleep(1000);
        }
        RocketMQConsumerVO mqConsumerVO = RocketMQConsumerVO.builder()
                .group("consumer-group-01-name")
                .topic("my-topic")
                .messageModel(MessageModel.CLUSTERING).build();
        super.registryConsumer(mqConsumerVO);
        log.info(">>>>>>>>>>>>|MyConsumer02消费者启动了\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80|<<<<<<<<<<<<");
    }

    @Override
    protected ConsumeConcurrentlyStatus onMessage(MessageExt messageExt) {
        String messageBody = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info(">>>>>>>>>>>>|MyConsumer02 get Message → {}", messageBody);
        // TIPS!!! Just for test about Message-Auto-Resend.
        // return CONSUME_SUCCESS;
        return RECONSUME_LATER;
    }
}
