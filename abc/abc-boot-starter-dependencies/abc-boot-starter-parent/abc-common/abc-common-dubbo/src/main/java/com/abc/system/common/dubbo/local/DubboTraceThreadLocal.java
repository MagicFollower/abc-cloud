package com.abc.system.common.dubbo.local;

/**
 * RPC-TRACE本地线程缓存类
 *
 * @Description RPC-TRACE本地线程缓存类
 * @Author Trivis
 * @Date 2023/5/13 21:48
 * @Version 1.0
 */
@Deprecated
public class DubboTraceThreadLocal {

    /**
     * 保存当前线程上下文信息
     */
    private static final ThreadLocal<String> TRACE_CACHE = new ThreadLocal<>();

    /**
     * 获取当前线程traceId
     *
     * @return 当前线程traceId
     */
    public static String getTraceId() {
        return TRACE_CACHE.get();
    }

    /**
     * 保存traceId在当前线程
     *
     * @param traceId traceId
     */
    public static void setTraceId(String traceId) {
        TRACE_CACHE.set(traceId);
    }

    /**
     * 清空当前线程数据
     */
    public void clear() {
        TRACE_CACHE.remove();
    }
}
