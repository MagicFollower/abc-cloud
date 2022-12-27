package com.abc.auth.controller;


import com.abc.auth.form.LoginBody;
import com.abc.auth.form.RegisterBody;
import com.abc.auth.service.SysLoginService;
import com.abc.common.core.domain.R;
import com.abc.common.core.utils.JwtUtils;
import com.abc.common.core.utils.StringUtils;
import com.abc.common.security.auth.AuthUtil;
import com.abc.common.security.service.TokenService;
import com.abc.common.security.utils.SecurityUtils;
import com.abc.system.api.model.LoginUser;
import com.alibaba.nacos.shaded.com.google.protobuf.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * token 控制
 */
@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    private final SysLoginService sysLoginService;

    @PostMapping("login")
    public R<?> login(@RequestBody LoginBody form) throws ServiceException {
        // 用户登录
        LoginUser userInfo = sysLoginService.login(form.getUsername(), form.getPassword());
        // 获取登录token
        return R.ok(tokenService.createToken(userInfo));
    }

    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            String username = JwtUtils.getUserName(token);
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志
            sysLoginService.logout(username);
        }
        return R.ok();
    }

    @PostMapping("refresh")
    public R<?> refresh(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return R.ok();
        }
        return R.ok();
    }

    @PostMapping("register")
    public R<?> register(@RequestBody RegisterBody registerBody) throws ServiceException {
        // 用户注册
        sysLoginService.register(registerBody.getUsername(), registerBody.getPassword());
        return R.ok();
    }
}
