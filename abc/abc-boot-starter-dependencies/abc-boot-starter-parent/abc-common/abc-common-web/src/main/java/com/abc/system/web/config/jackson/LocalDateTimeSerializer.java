package com.abc.system.web.config.jackson;

import com.abc.system.common.helper.SpringHelper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTimeSerializer
 *
 * @Description LocalDateTimeSerializer
 * @Author [author_name]
 * @Date 2077/5/19 11:20
 * @Version 1.0
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String format = SpringHelper.getApplicationContext().getEnvironment()
                .getProperty(JacksonConfig.CONFIG_PROPERTY_NANE);
        if (StringUtils.isEmpty(format)) format = JacksonConfig.DEFAULT_FORMAT;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        gen.writeString(value.format(formatter));
    }
}

