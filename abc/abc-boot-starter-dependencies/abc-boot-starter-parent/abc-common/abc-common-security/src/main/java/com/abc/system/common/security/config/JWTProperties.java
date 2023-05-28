package com.abc.system.common.security.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWTProperties
 *
 * @Description JWTProperties 详细介绍
 * @Author Trivis
 * @Date 2023/5/27 23:52
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "abc.jwt")
public class JWTProperties {

    /**
     * JWT发布者
     */
    private String issuer = StringUtils.repeat("*", 32);

    /**
     * JWT过期时间（默认30分钟）
     */
    private long expiration = 30L;

    /**
     * JWT内容加密密钥（AES）
     */
    private String encryptionSecret = "_ABC" + StringUtils.repeat("*", 32);

    /**
     * JWT签名密钥
     */
    private String signatureSecret = StringUtils.repeat("*", 32) + "_ABC";

}
