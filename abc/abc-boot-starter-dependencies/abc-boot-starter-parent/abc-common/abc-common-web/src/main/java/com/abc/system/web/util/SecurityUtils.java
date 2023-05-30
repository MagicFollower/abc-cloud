package com.abc.system.web.util;

import com.abc.system.common.constant.security.SecurityConstants;
import com.abc.system.common.constant.session.SessionConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * SecuritySessionUtils 会话工具包（仅提供获取euid的方法）
 *
 * @Description SecuritySessionUtils 会话工具包（仅提供获取euid的方法）
 * @Author Trivis
 * @Date 2023/5/15 21:30
 * @Version 1.0
 */
@Slf4j
public class SecurityUtils {

    private SecurityUtils() {

    }

    /**
     * 从请求中获取euid（euid可以解释为equipment_id或effective_uid, 表示登录后服务器给登录设备的tag标记）
     * 1.header
     * 2.cookie
     *
     * @param request 请求体（HttpServletRequest）
     * @return euid
     */
    public static String getEuid(HttpServletRequest request) {
        String euid = request.getHeader(SessionConstants.EUID);
        return StringUtils.isEmpty(euid) ? CookieUtils.getCookieValue(request, SessionConstants.EUID) : euid;
    }

    /**
     * 生成新EUID
     *
     * @return 新EUID字符串
     */
    public static String newEuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 从请求中提取传入的token信息
     * 1.header
     * 2.cookie
     *
     * @param request 请求体（HttpServletRequest）
     * @return TOKEN字符串
     */
    public static String getToken(HttpServletRequest request) {
        String accessToken = request.getHeader(SecurityConstants.ACCESS_TOKEN);
        return StringUtils.isEmpty(accessToken) ? CookieUtils.getCookieValue(request, SecurityConstants.ACCESS_TOKEN)
                : accessToken;
    }

    /**
     * 清除cookie中的token信息（token校验失败时使用）
     *
     * @param response 响应体（HttpServletResponse）
     */
    public static void cleanToken(HttpServletResponse response) {
        CookieUtils.cleanCookie(response, SecurityConstants.ACCESS_TOKEN, "/", null);
    }
}
