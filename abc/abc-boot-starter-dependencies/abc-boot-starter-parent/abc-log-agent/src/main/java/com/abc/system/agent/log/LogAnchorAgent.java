package com.abc.system.agent.log;

import com.abc.system.common.log.annotation.LogAnchor;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 日志锚点JavaAgent
 *
 * @Description LogAnchorAgent 日志锚点JavaAgent
 * @Author Trivis
 * @Date 2023/5/21 14:29
 * @Version 1.0
 */
public class LogAnchorAgent {

    private final static String PACKAGE_PREFIX = "com.abc.business.";

    public static void premain(String arguments, Instrumentation instrumentation) {
        new AgentBuilder.Default()
                .type(ElementMatchers.nameStartsWith(PACKAGE_PREFIX))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) -> builder
                        .method(ElementMatchers.isAnnotatedWith(LogAnchor.class))
                        .intercept(Advice.to(LogTagInterceptor.class)))
                .installOn(instrumentation);
    }

    public static class LogTagInterceptor {
        @Advice.OnMethodEnter
        public static void enter(@Advice.Origin Method method, @Advice.AllArguments Object[] args) {
            final Logger LOGGER = LoggerFactory.getLogger(method.getDeclaringClass());
            LOGGER.info(">>>>>>>>>>>|enter|method:{}|<<<<<<<<<<<", method.getName());
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                String parameterName = parameters[i].getName();
                Object parameterValue = args[i];
                if (parameterValue instanceof HttpServletRequest || parameterValue instanceof HttpServletResponse) {
                    // LOGGER.info(">>>>>>>>>>>|HttpServletRequest/HttpServletResponse|<<<<<<<<<<<");
                    continue;
                }
                LOGGER.info(">>>>>>>>>>>|param-{}|{}:⤵\n{}", i + 1, parameterName,
                        JSONObject.toJSONString(parameterValue, JSONWriter.Feature.PrettyFormat));
            }
        }

        @Advice.OnMethodExit(onThrowable = Exception.class)
        public static void exit(@Advice.Origin Method method,
                                @Advice.Thrown Exception e) {
            String methodName = method.getName();
            final Logger LOGGER = LoggerFactory.getLogger(method.getDeclaringClass());
            if (e != null) {
                LOGGER.error(">>>>>>>>>>>|exception|method:{},errorMessage:{}|<<<<<<<<<<<", methodName, e.getMessage(), e);
            } else {
                LOGGER.info(">>>>>>>>>>>|success|method:{}|<<<<<<<<<<<", methodName);
            }
        }
    }
}
