//package com.abc.business.authentication.filter;
//
//
//import com.abc.business.authentication.AuthenticationResult;
//import com.abc.business.authentication.UserAccountLoginDTO;
//import com.abc.business.authentication.UserAccountLoginVO;
//import com.abc.business.authentication.service.UserAuthenticationService;
//import com.abc.system.common.constant.SystemRetCodeConstants;
//import com.abc.system.common.response.ResponseData;
//import com.abc.system.common.response.ResponseProcessor;
//import com.abc.system.web.util.ServletUtils;
//import com.alibaba.fastjson2.JSONObject;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.common.base.Strings;
//import lombok.RequiredArgsConstructor;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
///**
// * 登录身份认证过滤器
// * <pre>
// * 1.身份认证Filter
// * 2.JWT校验Filter
// * </pre>
// *
// * @Description AuthenticationFilter
// * @Author -
// * @Date 2023/8/18 22:39
// * @Version 1.0
// */
//@Component
//@WebFilter(urlPatterns = "/api/*")
//@Order(Integer.MAX_VALUE - 1000)
//@RequiredArgsConstructor
//public class AuthenticationFilter implements Filter {
//
//    private static final String LOGIN_URI = "/api/login";
//    private static final String ACCESS_TOKEN_HEADER_NAME = "Access-Token";
//
//    private static final ObjectMapper objectMapper = new ObjectMapper();
//    private final UserAuthenticationService userAuthenticationService;
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
//            throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
//        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
//        // 登录校验
//        if (LOGIN_URI.equals(httpRequest.getRequestURI())) {
//            handleLogin(httpRequest, httpResponse);
//        }
//        // JWT校验
//        String accessToken = httpRequest.getHeader(ACCESS_TOKEN_HEADER_NAME);
//        if (StringUtils.isBlank(accessToken) || !userAuthenticationService.isValidToken(accessToken)) {
//            respondWithUnauthorized(httpResponse);
//            return;
//        }
//        filterChain.doFilter(httpRequest, httpResponse);
//    }
//
//    /**
//     * 登录认证逻辑
//     * <pre>
//     * 3种响应值类型
//     * 1.认证成功 → username+accessToken
//     * 2.认证失败 → ResponseData
//     * 3.系统错误 → ResponseData
//     * </pre>
//     *
//     * @param httpRequest  HttpServletRequest
//     * @param httpResponse HttpServletResponse
//     */
//    private void handleLogin(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) {
//        final ResponseProcessor<UserAccountLoginDTO> rp = new ResponseProcessor<>();
//        try {
//            UserAccountLoginVO userAccountLoginVO = objectMapper.readValue(httpRequest.getReader(), UserAccountLoginVO.class);
//            AuthenticationResult authenticationResult = userAuthenticationService.checkUser(userAccountLoginVO);
//            if (!authenticationResult.isSuccess()) {
//                respondWithLoginFailed(httpResponse);
//                return;
//            }
//            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
//            httpResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
//            UserAccountLoginDTO userAccountLoginDTO = UserAccountLoginDTO.builder()
//                    .username(authenticationResult.getUsername())
//                    .accessToken(userAuthenticationService.getToken(authenticationResult.getUsername()))
//                    .build();
//            ServletUtils.withInitial(httpResponse)
//                    .renderString(JSONObject.toJSONString(rp.setData(userAccountLoginDTO)));
//        } catch (IOException e) {
//            respondWithSystemError(httpResponse);
//        }
//    }
//
//    /**
//     * 登录失败（登录页）
//     *
//     * @param httpResponse HttpServletResponse
//     */
//    private void respondWithLoginFailed(final HttpServletResponse httpResponse) {
//        final ResponseProcessor<String> rp = new ResponseProcessor<>();
//        ResponseData<String> stringResponseData = rp.setErrorMsg(SystemRetCodeConstants.LOGIN_USERNAME_PASSWORD_ERROR);
//        ServletUtils.withInitial(httpResponse).renderString(JSONObject.toJSONString(stringResponseData));
//    }
//
//    /**
//     * 未登录访问其他页面
//     *
//     * @param httpResponse HttpServletResponse
//     */
//    private void respondWithUnauthorized(final HttpServletResponse httpResponse) {
//        final ResponseProcessor<String> rp = new ResponseProcessor<>();
//        ResponseData<String> stringResponseData = rp.setErrorMsg(SystemRetCodeConstants.LOGIN_UNAUTHORIZED);
//        ServletUtils.withInitial(httpResponse).renderString(JSONObject.toJSONString(stringResponseData));
//    }
//
//    private void respondWithSystemError(final HttpServletResponse httpResponse) {
//        final ResponseProcessor<String> rp = new ResponseProcessor<>();
//        ResponseData<String> stringResponseData = rp.setErrorMsg(SystemRetCodeConstants.SYSTEM_ERROR);
//        ServletUtils.withInitial(httpResponse).renderString(JSONObject.toJSONString(stringResponseData));
//    }
//}
