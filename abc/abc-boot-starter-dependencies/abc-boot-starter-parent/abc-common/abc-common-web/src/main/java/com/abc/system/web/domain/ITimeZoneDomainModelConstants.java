package com.abc.system.web.domain;

import java.util.Arrays;
import java.util.List;

/**
 * 基础领域模型实体常量
 *
 * @Description 基础领域模型实体常量
 * @Author Trivis
 * @Date 2023/5/26 12:37
 * @Version 1.0
 */
interface ITimeZoneDomainModelConstants {

    String TZ_HEADER = "tz";
    String TZ_DEFAULT = "GMT+8";

    String TZ_PREFIX_LIMIT = "GMT";
    List<String> TZ_POSTFIX_LIMIT = Arrays.asList("-12", "-11", "-10",
            "-9", "-8", "-7", "-6", "-5", "-4", "-3", "-2", "-1",
            "-0", "+0",
            "+1", "+2", "+3", "+4", "+5", "+6", "+7", "+8", "+9", "+10", "+11", "+12", "+13", "+14");

}
