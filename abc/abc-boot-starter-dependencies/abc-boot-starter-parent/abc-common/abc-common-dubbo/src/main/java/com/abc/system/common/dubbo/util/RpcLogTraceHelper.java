package com.abc.system.common.dubbo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * RPC服务traceId生成
 *
 * @Description RPC服务traceId生成
 * @Author [author_name]
 * @Date 2077/5/13 21:33
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class RpcLogTraceHelper {

    private final Environment environment;

    /**
     * <pre>
     * 生成traceId
     *   → 主机名#服务名(spring.application.name)#UUID
     * </pre>
     * @return traceId
     */
    public String getTraceId() {
        String hostname = "unknown";
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignore) {}
        String appName = environment.getProperty("spring.application.name");
        return hostname + "#" + appName + "#" + UUID.randomUUID().toString().replace("-", "");
    }
}
