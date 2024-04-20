package com.abc.system.web.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * IP工具包
 * <pre>
 * 1.获取本地IP (第一个IPv4非回环地址)
 * 2.获取本地主机名
 * 3.从HttpServletRequest中解析客户端IP
 * </pre>
 *
 * @Description IpUtils
 * @Author [author_name]
 * @Date 2077/7/27 23:17
 * @Version 1.0
 */
public class IpUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpUtils.class);
    private static final String UNKNOWN = "unknown";

    private IpUtils() {
    }

    public static String getHostIp() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();

            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address && !ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception var4) {
            LOGGER.error(">>>>>>>> get local host ip failed|exception: <<<<<<<<", var4);
        }

        return null;
    }

    public static String getHostname() {
        String hostname;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (final UnknownHostException ex) {
            hostname = UNKNOWN;
        }
        return hostname;
    }

    public static String getClientIp(HttpServletRequest request) {
        String ipAddr = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ipAddr) || UNKNOWN.equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getHeader("Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ipAddr) || UNKNOWN.equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ipAddr) || UNKNOWN.equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getHeader("HTTP_CLIENT_IP");
        }

        if (StringUtils.isEmpty(ipAddr) || UNKNOWN.equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (StringUtils.isEmpty(ipAddr) || UNKNOWN.equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddr) || "0:0:0:0:0:0:0:1".equals(ipAddr)) {
                try {
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    ipAddr = inetAddress.getHostAddress();
                } catch (UnknownHostException var3) {
                    LOGGER.error(">>>>>>>> get client ip failed|exception: <<<<<<<<", var3);
                }
            }
        }

        if (StringUtils.isEmpty(ipAddr) && ipAddr.length() > 15 && ipAddr.split(",").length > 0) {
            ipAddr = ipAddr.split(",")[0];
        }

        return ipAddr;
    }
}

