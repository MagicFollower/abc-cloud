package com.abc.system.common.security.helper;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.jwt.JWTException;
import com.abc.system.common.helper.SpringHelper;
import com.abc.system.common.security.config.JWTProperties;
import com.abc.system.common.security.util.AESUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

/**
 * JWTHelper1
 *
 * @Description <pre>
 * JWT生成与解析工具(auth0:jav-jwt)
 * 1.{@code String generateJWT(String content)}
 *   {@code String generateJWT(String content, String currentSystemName)}
 * 2.{@code boolean validateJWT(String jwt)}
 *   {@code boolean validateJWT(String jwt, String currentSystemName)}
 * 3.{@code String parseUserInfo(String jwt))}
 *   {@code String parseUserInfo(String jwt, String currentSystemName))}
 * </pre>
 * @Author Trivis
 * @Date 2023/5/28 11:26
 * @Version 1.0
 */
@Slf4j
public class JWTHelper1 {

    /**
     * 获取JWT字符串
     *
     * @param content           JWT自定义payload
     * @param currentSystemName 当前系统名，作为Audience
     * @return JWT字符串
     */
    public static String generateJWT(String content, String currentSystemName) {
        JWTProperties jwtProperties = SpringHelper.getBean(JWTProperties.class);
        String encryptionSecret = jwtProperties.getEncryptionSecret();
        String encryptedContent = new AESUtils(content).encrypt(encryptionSecret);
        String issuer = jwtProperties.getIssuer();
        return generateJWT(encryptedContent, issuer, currentSystemName);
    }

    public static String generateJWT(String content) {
        return generateJWT(content, null);
    }

    /**
     * 校验JWT字符串
     *
     * @param jwt               JWT字符串
     * @param currentSystemName 当前系统名，作为Audience
     * @return 校验是否通过（true是，false否）
     */
    public static boolean validateJWT(String jwt, String currentSystemName) {
        boolean valid;
        try {
            valid = isJWTExpired(parseJWT(jwt, currentSystemName));
        } catch (Exception e) {
            valid = false;
            log.error(">>>>>>>>|JWT校验失败|e:{}|<<<<<<<<", e.getMessage(), e);
        }
        return valid;
    }

    public static boolean validateJWT(String jwt) {
        return validateJWT(jwt, null);
    }

    /**
     * 解析JWT字符串中自定义payload（key="user"）
     *
     * @param jwt               JWT字符串
     * @param currentSystemName 当前系统名称（作为Audience）
     * @return 用户信息
     */
    public static String parseUserInfo(String jwt, String currentSystemName) {
        try {
            Map<String, Claim> stringClaimMap = parseJWT(jwt, currentSystemName);
            if (!isJWTExpired(stringClaimMap)) {
                return parseJWT(jwt, currentSystemName).get("user").asString();
            } else {
                throw new RuntimeException("JWT已过期");
            }
        } catch (Exception e) {
            log.error(">>>>>>>>|JWT解析用户信息失败|e:{}|<<<<<<<<", e.getMessage(), e);
            throw new JWTException(SystemRetCodeConstants.JWT_PARSE_ERROR);
        }
    }

    public static String parseUserInfo(String jwt) {
        return parseUserInfo(jwt, null);
    }

    /*🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎🍎*/

    /**
     * 解析JWT
     *
     * @param jwt               JWT
     * @param currentSystemName Audience
     * @return {@code Map<String, Claim>、com.auth0.jwt.interfaces.Claim} → iss/aud/iat/exp/自定义key
     */
    private static Map<String, Claim> parseJWT(String jwt, String currentSystemName) {
        try {
            JWTProperties jwtProperties = SpringHelper.getBean(JWTProperties.class);
            Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSignatureSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(jwtProperties.getIssuer())
                    .withAudience(currentSystemName)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(jwt);
            return decodedJWT.getClaims();
        } catch (Exception e) {
            log.error(">>>>>>>>|JWT解析异常|e:{}|<<<<<<<<", e.getMessage(), e);
            throw new JWTException(SystemRetCodeConstants.JWT_PARSE_ERROR);
        }
    }

    /**
     * 判断JWT是否过期，在校验JWT通过获取Claims后，需要根据Claims检测JWT是否过期
     *
     * @param claims {@code io.jsonwebtoken.Claims}
     * @return 是否过期（true是，false否）
     */
    private static boolean isJWTExpired(Map<String, Claim> claims) {
        Instant exp = Instant.ofEpochSecond(claims.get("exp").asLong());
        return exp.isBefore(Instant.now());
    }

    /**
     * 生成JWT字符串
     *
     * @param content  自定义payload（user: content）
     * @param issuer   JWT发布者
     * @param audience JWT受众
     * @return JWT字符串
     */
    private static String generateJWT(String content, String issuer, String audience) {
        JWTProperties jwtProperties = SpringHelper.getBean(JWTProperties.class);
        Duration duration = Duration.of(jwtProperties.getExpiration(), ChronoUnit.MINUTES);
        return generateJWT(content, issuer, audience, duration);
    }

    /**
     * 生成JWT字符串
     *
     * @param content     自定义payload（user: content）
     * @param issuer      JWT发布者
     * @param audience    JWT受众
     * @param ttlDuration JWT存活时间范围（Duration）
     * @return JWT字符串
     */
    private static String generateJWT(String content, String issuer, String audience, Duration ttlDuration) {
        try {
            JWTProperties jwtProperties = SpringHelper.getBean(JWTProperties.class);
            Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSignatureSecret());
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            Date expirationTime = new Date(nowMillis + ttlDuration.toMillis());
            return JWT.create()
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .withIssuedAt(now)
                    .withExpiresAt(expirationTime)
                    .withClaim("user", content)
                    .sign(algorithm);
        } catch (Exception e) {
            log.error(">>>>>>>>|JWT生成异常|e:{}|<<<<<<<<", e.getMessage(), e);
            throw new JWTException(SystemRetCodeConstants.JWT_GENERATE_ERROR);
        }
    }
}
