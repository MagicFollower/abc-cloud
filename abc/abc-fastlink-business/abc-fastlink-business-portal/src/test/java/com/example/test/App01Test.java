package com.example.test;

import com.abc.business.fastlink.portal.bootstrap.FastlinkPortalApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

/**
 * App01Test
 *
 * @Description App01Test
 * @Author Rake
 * @Date 2023/7/14 20:17
 * @Version 1.0
 */
@SpringBootTest(classes = {FastlinkPortalApplication.class})
public class App01Test {

    @Autowired
    private Environment environment;

    @Test
    public void test01() {
        String property = environment.getProperty("abc.excel");
        System.out.println("property = " + property);
    }

    @Test
    public void test02() {
        throw new RuntimeException(this.getClass().getName() + ": error");
    }
}
