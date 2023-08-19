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
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.common.base.Strings;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 配置文件中配置用户名与密码
 * <pre>
 * 1.单用户
 * 2.auth0.jwt校验
 * </pre>
 */
@Component
@ConfigurationProperties(prefix = "auth")
@Setter
public final class UserAuthenticationService {
    
    private static final String JWT_TOKEN_ISSUER = "abc-ui";

    // com.auth0.jwt.algorithms.Algorithm
    private final Algorithm algorithm = Algorithm.HMAC256(RandomStringUtils.randomAlphanumeric(256));
    
    private final JWTVerifier verifier = JWT.require(algorithm).withIssuer(JWT_TOKEN_ISSUER).build();
    
    private String username;
    
    private String password;
    
    private int tokenExpiresAfterSeconds = 3600;
    
    /**
     * 用户验证
     * <pre>
     * 1.简单明文对比校验
     * </pre>
     *
     * @param userAccountLoginVO UserAccountLoginVO
     * @return check success or failure
     */
    public AuthenticationResult checkUser(final UserAccountLoginVO userAccountLoginVO) {
        if (null == userAccountLoginVO || Strings.isNullOrEmpty(userAccountLoginVO.getUsername()) || Strings.isNullOrEmpty(userAccountLoginVO.getPassword())) {
            return new AuthenticationResult(null, null, false);
        }
        if (username.equals(userAccountLoginVO.getUsername()) && password.equals(userAccountLoginVO.getPassword())) {
            return new AuthenticationResult(username, password, true);
        }
        return new AuthenticationResult(null, null, false);
    }
    
    /**
     * Get user authentication token.
     *
     * @return authentication token
     */
    public String getToken(final String username) {
        Map<String, Object> payload = new HashMap<>(1, 1);
        payload.put("username", username);
        Date expiresAt = new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(tokenExpiresAfterSeconds));
        return JWT.create()
                .withExpiresAt(expiresAt)
                .withIssuer(JWT_TOKEN_ISSUER)
                .withPayload(payload).sign(algorithm);
    }
    
    /**
     * Check if token is valid.
     *
     * @param token token
     * @return is valid
     */
    public boolean isValidToken(final String token) {
        try {
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            return false;
        }
        return true;
    }
}
