package com.abc.business.fastlink.portal.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FilterConfig
 *
 * @Description FilterConfig
 * @Author Rake
 * @Date 2023/8/13 20:39
 * @Version 1.0
 */

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<AaaFilter> AaaFilter() {
        FilterRegistrationBean<AaaFilter> filter = new FilterRegistrationBean<>();
        filter.setName("AaaFilter");
        filter.setFilter(new AaaFilter());
        filter.setOrder(Integer.MAX_VALUE);
        return filter;
    }
}
