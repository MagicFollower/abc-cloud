package com.abc.business.web.config;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;

/**
 * -
 *
 * @Description -
 * @Author -
 * @Date 2023/9/12 21:48
 * @Version 1.0
 */
@Slf4j
public class MyDruidDataSource extends DruidDataSource {
    private static final long serialVersionUID = 1377390212442554779L;
    private static final String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJnI09eW+aUcxZZBHue4GR3DIJ3ulFlWVxYhGJ1Qe5Yax9tox9dBenFybR3dxv1rXR+6h4H2mqupPUdkKGSlLFMCAwEAAQ==";

    @Override
    public void setPassword(String password) {
        try {
            password = ConfigTools.decrypt(publicKey, username);
        } catch (Exception e) {
            log.error(">>>>>>>>|DruidConnectionProvider|ERROR|exception:{}|<<<<<<<<", e.getMessage(), e);
        }
        super.setPassword(password);
    }
}
