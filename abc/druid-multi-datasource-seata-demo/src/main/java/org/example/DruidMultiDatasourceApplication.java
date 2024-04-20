package org.example;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * -
 *
 * @Description -
 * @Author -
 * @Date 2077/9/14 22:28
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan({"org.example.dal"})
@org.mybatis.spring.annotation.MapperScan({"org.example.dal"})
public class DruidMultiDatasourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DruidMultiDatasourceApplication.class, args);
    }
}
