package com.abc.system.common.constant.session;

import java.time.Duration;

/**
 * 用户会话常量定义
 *
 * @Description 用户会话常量定义
 * @Author Trivis
 * @Date 2023/5/13 22:00
 * @Version 1.0
 */
public interface SessionConstants {

    /**
     * 会话默认超时时间30分钟
     */
    Duration DEFAULT_TIME_OUT_DURATION = Duration.ofMinutes(30L);

    /**
     * 客户端唯一标识
     */
    String EUID = "euid";
    /**
     * 用户显示名称
     */
    String USER_DISPLAY_NAME = "displayName";

    /**
     * 用户登录会话缓存前缀
     * 格式：USER_${euid}:会员信息字符串
     */
    String USER_ACCOUNT = "USER_%s";
}
