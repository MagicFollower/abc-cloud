package com.abc.system;

import com.abc.common.core.utils.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

/**
 * 系统模块
 *
 * @author abc
 */
//@SpringBootApplication
//@MapperScan("com.abc.**.mapper")
public class AbcSystemApplication {
    public static void main(String[] args) {
//        SpringApplication.run(AbcSystemApplication.class, args);
//        System.out.println("系统模块启动成功");

        System.out.println(StringUtils.isMatch("/abc/aaa/bbb", "/ABC/aaa/bbb"));
        System.out.println(StringUtils.isMatch("/abc/*/bbb", "/abc/aaa/bbb/cccc"));
        System.out.println(StringUtils.isMatch("/abc/*/bbb", "/abc/aaa/bbb"));
        System.out.println(StringUtils.isMatch("/abc/*/bbb", "/abc/aaa/aaa/bbb"));
        System.out.println(StringUtils.isMatch("/abc/**/bbb", "/abc/aaa/aaa/bbb"));
        System.out.println(StringUtils.matches("/abc/aaa/bbb/bbb", List.of("/abc/*/bbb","/abc/**/bbb")));


        System.out.println(StringUtils.padding(123, 7));
        System.out.println(StringUtils.padding(123, 2));
        System.out.println(StringUtils.padding("hello", 7, '?'));
        System.out.println(StringUtils.padding("hello", 2, '?'));

    }
}
