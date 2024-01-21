package org.example.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * -
 *
 * @Description -
 * @Author -
 * @Date 2023/9/14 22:51
 * @Version 1.0
 */
@Configuration
@MapperScan(basePackages = "org.example.dal.two", sqlSessionFactoryRef = "twoSqlSessionFactory")
public class DruidSecondConfig {

    /**
     * 多数据源时，第一个数据源指定Primary
     */
    @Bean("dataSourceTwo")
    @ConfigurationProperties("spring.datasource.druid.two")
    public DataSource dataSourceTwo() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "twoSqlSessionFactory")
    public SqlSessionFactory twoSqlSessionFactory(@Qualifier("dataSourceTwo") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(resolveMapperLocations("classpath:org/example/dal/two/persistence/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "twoTransactionManager")
    public PlatformTransactionManager twoTransactionManager(@Qualifier("dataSourceTwo") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    private Resource[] resolveMapperLocations(String mapperLocation) throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        return resolver.getResources(mapperLocation);
    }
}
