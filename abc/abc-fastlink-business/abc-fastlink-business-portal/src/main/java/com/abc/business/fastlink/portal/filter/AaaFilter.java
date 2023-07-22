package com.abc.business.fastlink.portal.filter;

import com.abc.system.common.dubbo.constant.RpcConstants;
import com.abc.system.common.dubbo.util.RpcLogTraceHelper;
import com.abc.system.common.helper.SpringHelper;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AaaFilter
 *
 * @Description AaaFilter
 * @Author Rake
 * @Date 2023/7/22 16:27
 * @Version 1.0
 */
@Slf4j
@WebFilter
@Order(Integer.MAX_VALUE)
public class AaaFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.warn(">>>>>>>>>>> \uD83D\uDE03ENTER|AaaFilter <<<<<<<<<<<");
        log.warn(">>>>>>>>>>> \uD83D\uDE03生成traceId,并放入Dubbo上下文|AaaFilter <<<<<<<<<<<");
        final String traceId = SpringHelper.getBean(RpcLogTraceHelper.class).getTraceId();
        RpcContext.getServiceContext().setAttachment(RpcConstants.RPC_TRACE_ID, traceId);

        // 过滤器放行
        filterChain.doFilter(request, response);
    }

}
