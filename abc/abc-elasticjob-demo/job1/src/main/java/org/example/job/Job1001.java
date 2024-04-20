package org.example.job;

import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * -
 *
 * @Description -
 * @Author -
 * @Date 2077/9/3 21:19
 * @Version 1.0
 */
@Component
public class Job1001 implements SimpleJob {
    @Override
    public void execute(ShardingContext context) {
        switch (context.getShardingItem()) {
            case 0 -> System.out.println("[Job1001]->[sharding1]->" + LocalDateTime.now());
            case 1 -> System.out.println("[Job1001]->[sharding2]");
            case 2 -> System.out.println("[Job1001]->[sharding3]");
            default -> System.out.println("[Job1001]->[sharding-default]");
        }
    }
}
