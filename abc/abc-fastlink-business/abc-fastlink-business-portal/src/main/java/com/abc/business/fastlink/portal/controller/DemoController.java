package com.abc.business.fastlink.portal.controller;

import com.abc.system.apollo.autoconfig.SystemConfigValues;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试用Controller
 *
 * @Description 测试用Controller
 * @Author Rake
 * @Date 2023/8/4 19:11
 * @Version 1.0
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/01")
    public void demo01() {
        System.out.println(SystemConfigValues.get("author.name"));
        System.out.println(SystemConfigValues.get("author.age"));
        System.out.println(SystemConfigValues.get("author.email"));
        System.out.println(SystemConfigValues.get("author.memo"));
        System.out.println(StringUtils.repeat("=", 50));
    }
}
