package com.abc.system.web.util;

import com.abc.system.common.constant.session.SessionConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * SessionUtils 会话工具包（仅提供获取euid的方法）
 *
 * @Description SessionUtils 会话工具包（仅提供获取euid的方法）
 * @Author Trivis
 * @Date 2023/5/15 21:30
 * @Version 1.0
 */
@Slf4j
public class SessionUtils {

    private SessionUtils() {

    }

    /**
     * 从三个地方获取euid（euid可以解释为equipment_id或effective_uid, 表示登录后服务器给登录设备的tag标记）
     * 1.header
     * 2.cookie
     * 3.attribute
     *
     * @param request 请求体（HttpServletRequest）
     * @return euid
     */
    public static String getEuid(HttpServletRequest request) {
        // 从header中获取euid
        String euid = request.getHeader(SessionConstants.EUID);
        // 取得euid
        euid = StringUtils.isEmpty(euid) ? CookieUtils.getCookieValue(request, SessionConstants.EUID) : euid;
        // 无euid表示用户未登录
        if (StringUtils.isEmpty(euid)) {
            // 其次从request中获取euid
            euid = (String) request.getAttribute(SessionConstants.EUID);
            if (StringUtils.isEmpty(euid)) {
                log.warn(">>>>>>>> euid is empty|please login <<<<<<<<");
                return null;
            }
        }

        return euid;
    }
}
