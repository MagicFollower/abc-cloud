package com.abc.system.web.config.jackson;

import com.abc.system.common.helper.SpringHelper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTimeDeserializer
 *
 * @Description LocalDateTimeDeserializer
 * @Author [author_name]
 * @Date 2077/5/19 11:21
 * @Version 1.0
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext dc) throws IOException {
        String str = p.getText().trim();
        if (StringUtils.isEmpty(str)) return null;
        String format = SpringHelper.getApplicationContext().getEnvironment()
                .getProperty(JacksonConfig.CONFIG_PROPERTY_NANE);
        if (StringUtils.isEmpty(format)) format = JacksonConfig.DEFAULT_FORMAT;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(str, formatter);
    }
}
