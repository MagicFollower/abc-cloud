package org.example.job;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
public class Job2001 implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        for (int i = 0; i < 3; i++) {
            System.out.println("[Job2001]→ 执行任务" + i + "," + LocalDateTime.now());
        }
        System.out.println();
    }
}
