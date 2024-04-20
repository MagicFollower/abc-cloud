package com.abc.system.common.dubbo.filter;

import com.abc.system.common.dubbo.constant.RpcConstants;
import com.abc.system.common.dubbo.util.RpcLogTraceHelper;
import com.abc.system.common.helper.SpringHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

/**
 * RPC调用链路跟踪过滤器
 *
 * @Description RPC调用链路跟踪过滤器（调用前在MDC中填充traceId，调用后在MDC中移除traceId）
 * @Author [author_name]
 * @Date 2077/5/13 21:26
 * @Version 1.0
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER}, order = -50000)
public class ProviderDubboTraceIdFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        log.debug(">>>>>>>>>>> remote dubbo trace Filter <<<<<<<<<<<");

        // 从RPC上下文中取得traceId
        //     ➡️请使用RpcContext.getServiceContext()替换RpcContext.getContext()
        final RpcServiceContext rpcServiceContext = RpcContext.getServiceContext();
        String traceId = rpcServiceContext.getAttachment(RpcConstants.RPC_TRACE_ID);
        // traceId为空则重新生成，并重新设置到RPC上下文中
        if (StringUtils.isEmpty(traceId)) {
            traceId = SpringHelper.getBean(RpcLogTraceHelper.class).getTraceId();
            rpcServiceContext.setAttachment(RpcConstants.RPC_TRACE_ID, traceId);
        }

        // slf4j 中设置了日志打印格式用作日志链路追踪
        //     ➡️建议在启动类中指定: System.setProperty("dubbo.application.logger","slf4j");
        MDC.put(RpcConstants.RPC_TRACE_ID, traceId);

        try {
            return invoker.invoke(invocation);
        } finally {
            MDC.remove(RpcConstants.RPC_TRACE_ID);
        }
    }
}
