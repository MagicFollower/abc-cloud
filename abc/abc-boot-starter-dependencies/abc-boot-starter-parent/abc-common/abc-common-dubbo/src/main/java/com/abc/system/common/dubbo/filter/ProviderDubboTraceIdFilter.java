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
 * <pre>
 *     1.日志打印warn级别仅供测试使用
 * </pre>
 *
 * @Description RPC调用链路跟踪过滤器（调用前在MDC中填充traceId，调用后在MDC中移除traceId）
 * @Author Trivis
 * @Date 2023/5/13 21:26
 * @Version 1.0
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER}, order = -50000)
public class ProviderDubboTraceIdFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        log.warn(">>>>>>>>>>> \uD83D\uDE80ENTER|ProviderDubboTraceIdFilter <<<<<<<<<<<");

        // 从RPC上下文中取得traceId
        //     ➡️请使用RpcContext.getServiceContext()替换RpcContext.getContext()
        String traceId = RpcContext.getServiceContext().getAttachment(RpcConstants.RPC_TRACE_ID);
        // traceId为空则重新生成，并重新设置到RPC上下文中
        if (StringUtils.isEmpty(traceId)) {
            // 🤔️这种情况表示消费者没有正常在Dubbo上下文中填充TraceID, 即使这样，之后的调用链仍然需要进行追踪
            log.warn(">>>>>>>>>>> \uD83D\uDE80重新生成traceId,并放入Dubbo上下文|ProviderDubboTraceIdFilter <<<<<<<<<<<");
            traceId = SpringHelper.getBean(RpcLogTraceHelper.class).getTraceId();
            RpcContext.getServiceContext().setAttachment(RpcConstants.RPC_TRACE_ID, traceId);
        }

        // slf4j 中设置了日志打印格式用作日志链路追踪
        //     ➡️建议在启动类中指定: System.setProperty("dubbo.application.logger","slf4j");
        MDC.put(RpcConstants.RPC_TRACE_ID, traceId);
        log.warn(">>>>>>>>>>> \uD83D\uDE80remote dubbo trace Filter|traceId={} <<<<<<<<<<<", traceId);

        try {
            return invoker.invoke(invocation);
        } finally {
            MDC.remove(RpcConstants.RPC_TRACE_ID);
        }
    }
}
