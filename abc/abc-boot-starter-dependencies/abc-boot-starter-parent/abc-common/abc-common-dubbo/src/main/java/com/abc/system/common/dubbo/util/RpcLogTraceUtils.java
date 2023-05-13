package com.abc.system.common.dubbo.util;

import java.util.UUID;

/**
 * RPC服务traceId生成
 *
 * @Description RPC服务traceId生成
 * @Author Trivis
 * @Date 2023/5/13 21:33
 * @Version 1.0
 */
public class RpcLogTraceUtils {

    private RpcLogTraceUtils() {

    }

    /**
     * 生成traceId
     *
     * @return traceId
     */
    public static String getTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}