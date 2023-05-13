package com.abc.system.common.dubbo.constant;

/**
 * RPC常量定义
 *
 * @Description RPC常量定义
 * @Author Trivis
 * @Date 2023/5/13 21:22
 * @Version 1.0
 */
public class RpcConstants {

    /**
     * RPC上下文传递的traceId
     */
    public static final String RPC_TRACE_ID = "_traceId";
    /**
     * RPC上下文传递的Elastic Apm traceId
     */
    public static final String ELASTIC_APM_TRACE_ID = "_apmTraceId";

    private RpcConstants() {

    }
}
