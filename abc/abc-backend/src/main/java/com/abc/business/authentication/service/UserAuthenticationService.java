/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.abc.business.authentication.service;

import com.abc.business.authentication.AuthenticationResult;
import com.abc.business.authentication.UserAccountLoginVO;
import com.abc.business.authentication.config.UserAccountProperties;
import com.abc.system.common.security.helper.Auth0JWTHelper;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Component;

import java.util.Base64;

/**
 * 配置文件中配置用户名与密码
 * <pre>
 * 1.单用户
 * 2.auth0.jwt校验
 * </pre>
 */
@Component
@EnableConfigurationProperties({UserAccountProperties.class})
@RequiredArgsConstructor
public final class UserAuthenticationService {

    private final UserAccountProperties userAccountProperties;

    /**
     * 用户验证
     * <pre>
     * 1.简单明文对比校验
     * </pre>
     *
     * @param userAccountLoginVO UserAccountLoginVO
     * @return AuthenticationResult about check success or failure
     */
    public AuthenticationResult checkUser(final UserAccountLoginVO userAccountLoginVO) {
        if (null == userAccountLoginVO
                || Strings.isNullOrEmpty(userAccountLoginVO.getUsername())
                || Strings.isNullOrEmpty(userAccountLoginVO.getPassword())) {
            return new AuthenticationResult(null, null, false);
        }
        final String username = userAccountLoginVO.getUsername();
        final String password = userAccountLoginVO.getPassword();
        // 处理配置中的所有用户信息
        // 1.密码Sha512加密
        if (userAccountProperties != null && CollectionUtils.isNotEmpty(userAccountProperties.getUsers())) {
            for (UserAccountProperties.UserAccountItem user : userAccountProperties.getUsers()) {
                if (username.equals(user.getUsername())) {
                    String _password = Base64.getEncoder()
                            .encodeToString(Sha512DigestUtils.sha(StringUtils.strip(user.getPassword())));
                    if (password.equals(_password)) {
                        return new AuthenticationResult(username, password, true);
                    }
                }
            }
        }
        return new AuthenticationResult(null, null, false);
    }

    /**
     * JWT生成
     *
     * @return JWT TOKEN (String)
     */
    public String getToken(final String username) {
        return Auth0JWTHelper.generateJWT(username);
    }

    /**
     * JWT校验
     *
     * @param token token String
     * @return true is valid, false is not valid
     */
    public boolean isValidToken(final String token) {
        return Auth0JWTHelper.validateJWT(token);
    }
}
