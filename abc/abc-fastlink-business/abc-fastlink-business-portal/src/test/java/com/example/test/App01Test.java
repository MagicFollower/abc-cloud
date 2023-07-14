package com.example.test;

import net.bytebuddy.agent.ByteBuddyAgent;
import org.junit.jupiter.api.Test;

/**
 * App01Test
 *
 * @Description App01Test
 * @Author Rake
 * @Date 2023/7/14 20:17
 * @Version 1.0
 */
public class App01Test {

    static {
        ByteBuddyAgent.install();
    }

    @Test
    public void test01() {
        System.out.println("App01Test.test01");
    }

    @Test
    public void test02() {
        throw new RuntimeException(this.getClass().getName() + ": error");
    }
}
