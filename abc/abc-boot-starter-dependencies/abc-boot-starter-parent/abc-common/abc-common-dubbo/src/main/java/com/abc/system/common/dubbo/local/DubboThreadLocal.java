package com.abc.system.common.dubbo.local;

/**
 * RPC-EUID本地线程缓存类
 *
 * @Description RPC-EUID本地线程缓存类
 * @Author Trivis
 * @Date 2023/5/13 21:46
 * @Version 1.0
 */
@Deprecated
public class DubboThreadLocal {

    /**
     * 保存当前线程上下文信息
     */
    private static final ThreadLocal<String> CACHE = new ThreadLocal<>();

    /**
     * 获取当前线程euid
     *
     * @return euid
     */
    public static String getEuid() {
        return CACHE.get();
    }

    /**
     * 保存当前线程euid
     *
     * @param euid euid
     */
    public static void setEuid(String euid) {
        CACHE.set(euid);
    }

    public static void clear() {
        CACHE.remove();
    }
}
