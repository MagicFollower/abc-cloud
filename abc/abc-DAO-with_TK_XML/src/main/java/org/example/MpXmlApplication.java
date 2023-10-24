package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = {
        "org.example.dal.persistence"
})
public class MpXmlApplication {
    public static void main(String[] args) {
        SpringApplication.run(MpXmlApplication.class, args);
    }
}