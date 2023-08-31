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

/**
 * MyConsumer01
 *
 * @Description MyConsumer01
 * @Author -
 * @Date 2023/8/31 18:38
 * @Version 1.0
 */
@Slf4j
@Component
public class MyConsumer01 extends AbstractMQConsumer {

    @PostConstruct
    private void start() throws InterruptedException {
        Thread.sleep(10 * 1000);
        RocketMQConsumerVO mqConsumerVO = RocketMQConsumerVO.builder()
                .group("consumer-group-01-name")
                .topic("my-topic")
                .messageModel(MessageModel.CLUSTERING).build();
        super.registryConsumer(mqConsumerVO);
        log.info(">>>>>>>>|MyConsumer01消费者启动了\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80|<<<<<<<<");
    }

    @Override
    protected ConsumeConcurrentlyStatus onMessage(MessageExt messageExt) {
        String messageBody = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info(">>>>>>>>|MyConsumer01 get Message → {}", messageBody);
        return CONSUME_SUCCESS;
    }
}
