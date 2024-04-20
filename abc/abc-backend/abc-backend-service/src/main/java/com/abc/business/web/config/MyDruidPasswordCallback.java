package com.abc.business.web.config;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.util.DruidPasswordCallback;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * 自定义Druid密码解密回调类
 * <pre>
 * 1.推荐阅读1：http://raye.wang/springboot-druid-zi-ding-yi-jia-mi-shu-ju-ku-mi-ma/
 * </pre>
 *
 * @Description -
 * @Author -
 * @Date 2077/9/14 9:00
 * @Version 1.0
 */
@Slf4j
public class MyDruidPasswordCallback extends DruidPasswordCallback {

    @Override
    public void setProperties(Properties properties) {
        String password = properties.getProperty("password");
        String publicKey = properties.getProperty("config.decrypt.key");
        String decrypt = "";
        try {
            decrypt = ConfigTools.decrypt(publicKey, password);
        } catch (Exception e) {
            log.error(">>>>>>>>|MyDruidPasswordCallback|decrypt password error|exception:", e);
        }
        super.setPassword(decrypt.toCharArray());
        super.setProperties(properties);
    }
}
