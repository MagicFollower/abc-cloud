package com.abc.system.web.config.jackson;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * JacksonConfig
 *
 * @Description JacksonConfig
 * @Author [author_name]
 * @Date 2077/5/19 11:23
 * @Version 1.0
 */
@Configuration
public class JacksonConfig {

    /**
     * LocalDateTime序列化器、反序列化器缺省时间格式;
     */
    final static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * <pre>
     * spring.jackson.date-format参数, 使用该参数格式化LocalDateTime;
     * 1.spring.jackson.date-format参数目前只对Date生效（springboot:2.7.11），这是此处配置的目的；
     * 2.⚠️注意：如果你没有配置spring.jackson.date-format，LocalDateTime会按照上方默认格式`DEFAULT_FORMAT`序列化，
     *          但Date仍然是原生序列化格式，这是约定。
     * spring:
     *   jackson:
     *     time-zone: GMT+8
     *     date-format: yyyy-MM-dd HH:mm:ss.SSS
     * </pre>
     */
    final static String CONFIG_PROPERTY_NANE = "spring.jackson.date-format";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer());
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer());
            builder.serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
            builder.deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));
            builder.serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME));
            builder.deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME));
        };
    }
}
