package com.abc.system.common.config.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;

import java.io.IOException;

/**
 * I18nJsonSerializer
 *
 * @Description I18nJsonSerializer
 * @Author Rake
 * @Date 2023/7/30 18:39
 * @Version 1.0
 */
@Configuration
public class I18nJsonSerializer extends JsonSerializer<String> implements MessageSourceAware {

    private MessageSource messageSource;

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String s_;
        try {
            s_ = messageSource.getMessage(s, null, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            jsonGenerator.writeString(s);
            return;
        }
        if (StringUtils.isEmpty(s_)) {
            jsonGenerator.writeString(s);
            return;
        }
        jsonGenerator.writeString(s_);
    }

    @Override
    public void setMessageSource(@NonNull MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
