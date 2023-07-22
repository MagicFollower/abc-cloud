package com.abc.business.fastlink.portal.confif;

/**
 * FilterConfig
 *
 * @Description FilterConfig
 * @Author Rake
 * @Date 2023/7/22 16:29
 * @Version 1.0
 */
import com.abc.business.fastlink.portal.filter.AaaFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
