package com.abc.system.web.util;

import lombok.NonNull;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

/**
 * CookieUtils Cookie工具类
 *
 * @Description <pre> CookieUtils Cookie工具类
 *     1.cookie的uri和domain有什么差别？
 *       在HTTP协议中，Cookie是一种用于在客户端和服务器之间传递信息的机制。在设置Cookie时，可以通过设置URI和Domain来限定Cookie的使用范围：
 *       → URI（Path）：表示Cookie仅在指定的URI路径下才会被发送到服务器。例如，如果设置了URI为“/login”，则只有在访问“/login”路径下的页面时，浏览器才会将该Cookie发送给服务器。
 *       → Domain：表示Cookie仅在指定的域名下才会被发送到服务器。例如，如果设置了Domain为“.example.com”，则该Cookie会在所有以“example.com”结尾的域名下都会被发送到服务器。
 *       因此，URI和Domain的主要区别在于它们限定Cookie的范围的方式不同。URI是基于路径的限制方式，而Domain则是基于域名的限制方式。
 *     2.cookie.setHttpOnly(true);有什么作用？
 *       → 在Java Servlet中，通过调用`cookie.setHttpOnly(true)`方法可以设置Cookie的“HttpOnly”属性。设置了“HttpOnly”属性的Cookie只能通过HTTP或HTTPS协议访问，而无法通过JavaScript等客户端脚本来访问。这样可以防止一些常见的Web攻击，如跨站脚本攻击（XSS）和跨站请求伪造（CSRF）攻击，提高了Cookie的安全性。
 *       → 需要注意的是，设置了“HttpOnly”属性的Cookie只能在支持该属性的浏览器中使用。如果使用了不支持“HttpOnly”属性的浏览器，该Cookie仍然可以被JavaScript等客户端脚本访问，从而导致安全风险。因此，在使用`cookie.setHttpOnly(true)`设置Cookie的“HttpOnly”属性时，需要确保应用程序的目标浏览器支持该属性。
 *     3.cookie.setSecure(true);有什么作用？
 *       → 在Java Servlet中，通过调用`cookie.setSecure(true)`方法可以设置Cookie的“安全”属性。设置了“安全”属性的Cookie只会在HTTPS安全连接中发送到服务器，不会在普通HTTP连接中发送。这样可以确保Cookie中包含的敏感信息不会在不安全的网络中传输，提高了Cookie的安全性。
 *       → 需要注意的是，设置了“安全”属性的Cookie只能在HTTPS连接中使用，如果在普通HTTP连接中访问该Cookie，浏览器将无法发送该Cookie到服务器。因此，在使用`cookie.setSecure(true)`设置Cookie的“安全”属性时，需要确保应用程序同时支持HTTPS连接。
 *     4.cookie有几个核心属性？
 *        Cookie有四个核心属性，分别是：
 *        → 1. Name：Cookie的名称，用于标识Cookie。
 *        → 2. Value：Cookie的值，存储在客户端和服务器之间传递的信息。
 *        → 3. Domain：Cookie的域名，表示该Cookie所属的域名。
 *        → 4. Path：Cookie的路径，表示该Cookie所属的路径。只有在访问该路径下的页面时，浏览器才会将该Cookie发送给服务器。
 *        此外，Cookie还有一些可选属性，包括：
 *        → 1. Max-Age：Cookie的最大存活时间，表示该Cookie可以在客户端保持有效的时间长度。
 *        → 2. Secure：表示该Cookie只能在HTTPS安全连接中发送到服务器。
 *        → 3. HttpOnly：表示该Cookie只能通过HTTP或HTTPS协议访问，而无法通过JavaScript等客户端脚本来访问。
 * </pre>
 * @Author Trivis
 * @Date 2023/5/15 21:06
 * @Version 1.0
 */
public class CookieUtils {

    private CookieUtils() {

    }

    // #########################################################
    // 获取Cookie
    // #########################################################

    /**
     * 获取指定属性Cookie值
     *
     * @param request 请求体
     * @param name    属性名
     * @return cookie值
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // #########################################################
    // 保存Cookie
    // #########################################################

    /**
     * 向响应中写入会话cookie
     * 1.默认：会话cookie
     * 2.默认：isHttpOnly
     * 3.默认：uri=/
     *
     * @param response 响应对象（HttpServletResponse）
     * @param key      Cookie的key(String)
     * @param value    Cookie的value(String)
     */
    public static void setSessionCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        // 设置了secure后，cookie将只会在https请求中发送
        // cookie.setSecure(true);
        cookie.setHttpOnly(true);
        setCookie(response, cookie);
    }

    /**
     * 向响应中写入会话cookie
     * 1.默认：会话cookie
     * 2.默认：isHttpOnly
     *
     * @param response   响应对象（HttpServletResponse）
     * @param key        Cookie的key(String)
     * @param value      Cookie的value(String)
     * @param isHttpOnly isHttpOnly
     * @param domain     Cookie的domain域(String)
     * @param uri        uri
     */
    public static void setSessionCookie(HttpServletResponse response,
                                        String key, String value,
                                        boolean isHttpOnly, String domain, String uri) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(isHttpOnly);
        if (StringUtils.isNotEmpty(domain)) {
            cookie.setDomain(domain);
        }
        if (StringUtils.isNotEmpty(uri)) {
            cookie.setPath(uri);
        }
        setCookie(response, cookie);
    }

    /**
     * 向响应中写入cookie
     * 1.默认：isHttpOnly
     * 2.默认：uri=/
     *
     * @param response       响应对象（HttpServletResponse）
     * @param key            Cookie的key(String)
     * @param value          Cookie的value(String)
     * @param maxAgeDuration Duration
     */
    public static void setCookie(HttpServletResponse response, String key, String value, Duration maxAgeDuration) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        // 设置了secure后，cookie将只会在https请求中发送
        // cookie.setSecure(true);
        cookie.setHttpOnly(true);
        if (maxAgeDuration != null) {
            cookie.setMaxAge((int) maxAgeDuration.getSeconds());
        }
        setCookie(response, cookie);
    }

    /**
     * 向响应中写入cookie
     * 1.默认：isHttpOnly
     *
     * @param response       响应对象（HttpServletResponse）
     * @param key            Cookie的key(String)
     * @param value          Cookie的value(String)
     * @param maxAgeDuration Duration
     * @param isHttpOnly     isHttpOnly
     * @param domain         Cookie的domain域(String)
     * @param uri            uri
     */
    public static void setCookie(HttpServletResponse response,
                                 String key,
                                 String value,
                                 Duration maxAgeDuration,
                                 boolean isHttpOnly, String domain, String uri) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(isHttpOnly);
        if (StringUtils.isNotEmpty(domain)) {
            cookie.setDomain(domain);
        }
        if (StringUtils.isNotEmpty(uri)) {
            cookie.setPath(uri);
        }
        if (maxAgeDuration != null) {
            cookie.setMaxAge((int) maxAgeDuration.getSeconds());
        }
        setCookie(response, cookie);
    }

    /**
     * 向客户端保存Cookie
     *
     * @param response 响应对象（HttpServletResponse）
     * @param cookie   Cookie实体
     */
    public static void setCookie(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
    }

    // #########################################################
    // 清除Cookie
    // #########################################################

    /**
     * 清除Cookie
     * <pre>
     *     清除指定 KEY的Cookie
     * </pre>
     *
     * @param response 响应
     * @param key      Cookie对应的key
     */
    public static void cleanCookie(HttpServletResponse response, String key) {
        Cookie cookie = new Cookie(key, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 清除Cookie
     * <pre>
     *     清除指定 KEY的Cookie
     * </pre>
     *
     * @param response 响应
     * @param key      Cookie对应的key
     * @param uri      cookie保存的根路径
     */
    public static void cleanCookie(HttpServletResponse response, String key, String uri) {
        Cookie cookie = new Cookie(key, "");
        cookie.setMaxAge(0);
        if (StringUtils.isNotEmpty(uri)) {
            cookie.setPath(uri);
        } else {
            cookie.setPath("/");
        }
        response.addCookie(cookie);
    }

    /**
     * 清除Cookie
     * <pre>
     *     清除指定 KEY的Cookie
     * </pre>
     *
     * @param response 响应
     * @param key      Cookie对应的key
     * @param domain   Cookie的domain域
     * @param uri      cookie保存的根路径
     */
    public static void cleanCookie(HttpServletResponse response, String key, String domain, String uri) {
        Cookie cookie = new Cookie(key, "");
        cookie.setMaxAge(0);
        if (StringUtils.isNotEmpty(uri)) {
            cookie.setPath(uri);
        } else {
            cookie.setPath("/");
        }
        if (StringUtils.isNotEmpty(domain)) {
            cookie.setDomain(domain);
        }
        response.addCookie(cookie);
    }

    // #########################################################
    // 生成Cookie
    // #########################################################

    /**
     * 生成Cookie，根据key、value、maxAgeDuration
     *
     * @param key            cookie名称
     * @param value          cookie值
     * @param maxAgeDuration Duration
     * @return Cookie
     */
    public static Cookie generateCookie(String key, String value, Duration maxAgeDuration) {
        return generateCookie(key, value, maxAgeDuration, null, null);
    }

    /**
     * 生成Cookie，根据key、value、maxAgeDuration、domain
     *
     * @param key            cookie名称
     * @param value          cookie值
     * @param maxAgeDuration Duration
     * @param domain         cookie根路径
     * @return Cookie
     */
    public static Cookie generateCookieWithDomain(String key, String value, Duration maxAgeDuration, String domain) {
        return generateCookie(key, value, maxAgeDuration, domain, null);
    }

    /**
     * 生成Cookie，根据key、value、maxAgeDuration、uri
     *
     * @param key            cookie名称
     * @param value          cookie值
     * @param maxAgeDuration Duration
     * @param uri            cookie根路径
     * @return Cookie
     */
    public static Cookie generateCookieWithUri(String key, String value, Duration maxAgeDuration, String uri) {
        return generateCookie(key, value, maxAgeDuration, null, uri);
    }

    /**
     * 生成Cookie，根据key、value、maxAgeDuration、domain、uri
     *
     * @param key            cookie名称
     * @param value          cookie值
     * @param maxAgeDuration Duration
     * @param domain         cookie根路径
     * @param uri            cookie根路径
     * @return Cookie
     */
    public static Cookie generateCookie(@NonNull String key,
                                        @NonNull String value,
                                        Duration maxAgeDuration, String domain, String uri) {
        Cookie cookie = new Cookie(key, value);
        if (StringUtils.isNotEmpty(uri)) {
            enrichCookie(cookie, uri, maxAgeDuration);
        } else {
            enrichCookie(cookie, "/", maxAgeDuration);
        }
        if (StringUtils.isNotEmpty(domain)) {
            cookie.setDomain(domain);
        }
        return cookie;
    }

    /**
     * 设置Cookie路径与有效期(uri为null时，忽略uri配置)
     * <pre>
     *   → Cookie有四个最常用的参数key、value、maxAge、uri/path
     *   → 前两者可以在创建Cookie中指定{@code Cookie cookie = new Cookie(key, value);}，后两者将通过该enrichCookie方法进行填充
     * </pre>
     *
     * @param cookie         待处理的Cookie对象
     * @param uri            cookie保存的根路径
     * @param maxAgeDuration Duration
     */
    public static void enrichCookie(Cookie cookie, String uri, Duration maxAgeDuration) {
        if (StringUtils.isNotEmpty(uri)) {
            cookie.setPath(uri);
        }
        cookie.setMaxAge((int) maxAgeDuration.getSeconds());
    }
}