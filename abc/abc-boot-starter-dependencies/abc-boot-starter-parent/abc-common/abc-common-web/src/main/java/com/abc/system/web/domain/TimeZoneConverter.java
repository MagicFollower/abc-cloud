package com.abc.system.web.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 多时区转换器
 *
 * @Description <pre>
 * 多时区转换器
 * 1.提供世界级时区统一转换
 *   -> 请将三者统一为UTC时区：{@code @JsonFormat}、jdbcUrl#serverTimezone/connectionTimeZone、MySQL#time_zone
 *
 * 💡示例：
 * {@code
 *     @PostMapping("/demo02")
 *     public String demo02(HttpServletRequest request, @RequestBody User user) {
 *         TimeZoneConverter.tzConverterDown(request, user, User.class);
 *
 *         Example example = new Example(User.class);
 *         Example.Criteria criteria = example.createCriteria();
 *         criteria.andLessThanOrEqualTo(User.Fields.id, 400);
 *         List<User> users = userMapper.selectByExample(example);
 *
 *         TimeZoneConverter.tzConverterUp(request, users, User.class);
 *
 *         return JSONObject.toJSONString(users, JSONWriter.Feature.PrettyFormat);
 *     }
 * }
 * </pre>
 * @Author Trivis
 * @Date 2023/5/26 8:37
 * @Version 1.0
 */
public class TimeZoneConverter {

    static String TZ_HEADER = "tz";
    static String TZ_DEFAULT = "GMT+8";

    static String TZ_PREFIX_LIMIT = "GMT";
    static List<String> TZ_POSTFIX_LIMIT = Arrays.asList("-12", "-11", "-10",
            "-9", "-8", "-7", "-6", "-5", "-4", "-3", "-2", "-1",
            "-0", "+0",
            "+1", "+2", "+3", "+4", "+5", "+6", "+7", "+8", "+9", "+10", "+11", "+12", "+13", "+14");

    /**
     * <pre>
     * 时区填充检测(仅支持大写GMT前缀)
     *   -> 从Headers中获取tz属性
     *   -> 在时区的标准表示法中，GMT（格林威治标准时间）后面的数字表示时区偏移量，以分钟为单位。
     *      -> 通常情况下，时区偏移量的范围是从GMT-12到GMT+14之间，这是因为地球上最西边的地区与最东边的地区之间的时差最大为26小时。
     * </pre>
     *
     * @return 时区字符串（GMT-12~GMT+14）
     */
    private static String fetchTZ(HttpServletRequest request) {
        String userLocalTimeZone = request.getHeader(TZ_HEADER);
        if (StringUtils.isEmpty(userLocalTimeZone)) return TZ_DEFAULT;
        if ((userLocalTimeZone.length() == 6 || userLocalTimeZone.length() == 5)
                && userLocalTimeZone.startsWith(TZ_PREFIX_LIMIT)) {
            if (TZ_POSTFIX_LIMIT.contains(userLocalTimeZone.substring(userLocalTimeZone.indexOf("GMT") + 3))) {
                return userLocalTimeZone;
            }
        }
        return TZ_DEFAULT;
    }

    public static <T> void tzConverterDown(HttpServletRequest request, T instance, Class<T> clazz) {
        String userLocalTimeZone = fetchTZ(request);
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() == ZonedDateTime.class) {
                field.setAccessible(true);
                try {
                    ZonedDateTime x = (ZonedDateTime) (field.get(instance));
                    if (x != null) {
                        field.set(instance, x.withZoneSameLocal(ZoneId.of(userLocalTimeZone)));

                    }
                } catch (IllegalAccessException ignored) {
                }
            }
        }
    }

    public static <T> void tzConverterUp(HttpServletRequest request, T instance, Class<T> clazz) {
        String userLocalTimeZone = fetchTZ(request);
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() == ZonedDateTime.class) {
                field.setAccessible(true);
                try {
                    ZonedDateTime x = (ZonedDateTime) (field.get(instance));
                    if (x != null) {
                        field.set(instance, x.withZoneSameInstant(ZoneId.of(userLocalTimeZone)));
                    }
                } catch (IllegalAccessException ignored) {
                }
            }
        }
    }

    public static <T> void tzConverterDown(HttpServletRequest request, List<T> instanceList, Class<T> clazz) {
        instanceList.forEach(instance -> tzConverterDown(request, instance, clazz));
    }

    public static <T> void tzConverterUp(HttpServletRequest request, List<T> instanceList, Class<T> clazz) {
        instanceList.forEach(instance -> tzConverterUp(request, instance, clazz));
    }

}
