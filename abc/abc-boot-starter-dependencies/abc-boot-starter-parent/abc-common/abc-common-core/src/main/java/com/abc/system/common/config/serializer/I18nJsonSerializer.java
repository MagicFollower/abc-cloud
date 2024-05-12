package com.abc.system.common.config.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * I18n国际化Json序列化器
 * <pre>
 * 1.为什么实现了MessageSourceAware就可以自动获取MessageSource？
 * 实现了MessageSourceAware接口的类可以自动获取MessageSource是因为Spring框架的依赖注入机制。当
 * 一个类实现了MessageSourceAware接口时，它就表明自己需要获取一个MessageSource对象的引用。
 * Spring容器在创建这个类的实例时，会自动识别这个需求，并将容器中配置的MessageSource注入到该实例中。
 *
 * 2.关于JsonSerializer？
 * JsonSerializer 是一个接口，属于 Jackson 库，这个库是一个流行的 Java JSON 处理库。JsonSerializer 负责将 Java 对象序列化为 JSON 格式的字符串。
 * 例如我想将对象中的所有属性在序列化为字符串时都转为string，你可以自定义一个序列化器然后在注解中使用它：
 * ↓
 * {@code
 * public class User {
 *     private String name;
 *     private int age;
 *
 *     // getters and setters
 * }
 * ↓
 * public class IntegerAsStringSerializer extends JsonSerializer<Integer> {
 *     @Override
 *     public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 *         gen.writeString(String.valueOf(value));
 *     }
 * }
 * ↓
 * public class User {
 *     private String name;
 *     @JsonSerialize(using = IntegerAsStringSerializer.class)
 *     private int age;
 *
 *     // getters and setters
 * }
 * ↓
 * ObjectMapper mapper = new ObjectMapper();
 * User user = new User("John Doe", 30);
 * String json = mapper.writeValueAsString(user);
 * System.out.println(json);
 * ↓
 * {"name":"John Doe","age":"30"}
 * }
 * </pre>
 *
 * @Description I18nJsonSerializer
 * @Author [author_name]
 * @Date 2077/7/30 18:39
 * @Version 1.0
 */
@Component
public class I18nJsonSerializer extends JsonSerializer<String> implements MessageSourceAware {

    private MessageSource messageSource;

    @Override
    public void serialize(String message, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        try {
            String newMessage = messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
            if (StringUtils.isNotEmpty(newMessage)) {
                jsonGenerator.writeString(newMessage);
            } else {
                jsonGenerator.writeString(message);
            }
        } catch (NoSuchMessageException e) {
            jsonGenerator.writeString(message);
        }
    }

    @Override
    public void setMessageSource(@NonNull MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
