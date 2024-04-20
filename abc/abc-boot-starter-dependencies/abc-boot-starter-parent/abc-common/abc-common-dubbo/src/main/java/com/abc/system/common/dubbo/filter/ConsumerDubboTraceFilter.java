package com.abc.system.common.dubbo.filter;

import com.abc.system.common.constant.session.SessionConstants;
import com.abc.system.common.dubbo.constant.RpcConstants;
import com.abc.system.common.dubbo.local.DubboThreadLocal;
import com.abc.system.common.dubbo.local.DubboTraceThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

/**
 * Dubbo RPC Consumer过滤器
 *
 * @Description Dubbo RPC Consumer过滤器, 实现euid、traceId上下文传递，防止多次调用RPC时上下文参数丢失
 * @Author [author_name]
 * @Date 2077/5/13 21:42
 * @Version 1.0
 */
@Slf4j
@Activate(group = {CommonConstants.CONSUMER}, order = -51000)
public class ConsumerDubboTraceFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        processEuid();
        processTraceId();
        return invoker.invoke(invocation);
    }

    /**
     * 处理traceId
     */
    private void processTraceId() {
        final RpcServiceContext rpcContext = RpcContext.getServiceContext();
        // 从RPC上下文中取得traceId
        String traceId = rpcContext.getAttachment(RpcConstants.RPC_TRACE_ID);

        /* 解决DubboContext中属性异常丢失问题，不再使用 */
        //if (StringUtils.isNotEmpty(traceId)) {
        //    // 将traceId并保存至当前线程
        //    DubboTraceThreadLocal.setTraceId(traceId);
        //    log.debug(">>>>>>>>|dubbo Consumer Context Filter|get rpc context|traceId:{}|<<<<<<<<", traceId);
        //} else {
        //    // 从当前线程获取traceId，存入rpcContext
        //    traceId = DubboTraceThreadLocal.getTraceId();
        //    rpcContext.setAttachment(RpcConstants.RPC_TRACE_ID, traceId);
        //    log.debug(">>>>>>>>|dubbo Consumer Context Filter|get traceId from thread local|" +
        //            "traceId:{}|<<<<<<<<", traceId);
        //}

        // slf4j 中设置了日志打印格式用作日志链路追踪
        //     ➡️建议在启动类中指定: System.setProperty("dubbo.application.logger","slf4j");
        MDC.put(RpcConstants.RPC_TRACE_ID, traceId);
    }

    /**
     * 处理euid
     */
    private void processEuid() {
        //RpcServiceContext rpcContext = RpcContext.getServiceContext();
        //String euid = rpcContext.getAttachment(SessionConstants.EUID);
        //if (StringUtils.isNotEmpty(euid)) {
        //    // 从RpcContext中获取euid并保存
        //    DubboThreadLocal.setEuid(euid);
        //    log.debug(">>>>>>>>|Dubbo Consumer Context Filter|get rpc context|euid:{}|<<<<<<<<", euid);
        //} else {
        //    // 从当前线程获取euid，存入rpcContext
        //    euid = DubboThreadLocal.getEuid();
        //    rpcContext.setAttachment(SessionConstants.EUID, euid);
        //    log.debug(">>>>>>>>|Dubbo Consumer Context Filter|get euid from thread local|euid:{}|<<<<<<<<", euid);
        //}
    }
}
