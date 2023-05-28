package com.abc.system.common.security.helper;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.jwt.JWTException;
import com.abc.system.common.helper.SpringHelper;
import com.abc.system.common.security.config.JWTProperties;
import com.abc.system.common.security.util.AESUtils;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.gson.io.GsonSerializer;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 * JWT生成与解析工具
 *
 * @Description <pre>
 * JWT生成与解析工具(jsonwebtoken:jjwt-impl)
 * 1.{@code String generateJWT(String content)}
 *   {@code String generateJWT(String content, String currentSystemName)}
 * 2.{@code boolean validateJWT(String jwt)}
 *   {@code boolean validateJWT(String jwt, String currentSystemName)}
 * 3.{@code String parseUserInfo(String jwt))}
 *   {@code String parseUserInfo(String jwt, String currentSystemName))}
 * </pre>
 * @Author Trivis
 * @Date 2023/5/28 8:30
 * @Version 1.0
 */
@Slf4j
public class JWTHelper {

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
            Claims claims = parseJWT(jwt, currentSystemName);
            if (!isJWTExpired(claims)) {
                return parseJWT(jwt, currentSystemName).get("user").toString();
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
     * @return {@code io.jsonwebtoken.Claims}
     */
    private static Claims parseJWT(String jwt, String currentSystemName) {
        JWTProperties jwtProperties = SpringHelper.getBean(JWTProperties.class);
        try {
            return Jwts.parserBuilder()
                    .requireIssuer(jwtProperties.getIssuer())
                    .requireAudience(currentSystemName)
                    .setSigningKey(generateKey())
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
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
    private static boolean isJWTExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
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
        return getJwtBuilder(content, issuer, audience, ttlDuration)
                .serializeToJsonWith(new GsonSerializer<>(new Gson()))
                .compact();
    }

    /**
     * 获得JwtBuilder
     *
     * @param content     自定义payload（user: content）
     * @param issuer      JWT发布者
     * @param audience    JWT受众
     * @param ttlDuration JWT存活时间范围（Duration）
     * @return {@code io.jsonwebtoken.JwtBuilder}
     */
    private static JwtBuilder getJwtBuilder(String content, String issuer, String audience, Duration ttlDuration) {
        if (Objects.isNull(ttlDuration)) {
            ttlDuration = Duration.of(30L, ChronoUnit.MINUTES);
        }
        // 初始化jwt发布时间与过期时间
        long iat = System.currentTimeMillis();
        long exp = iat + ttlDuration.toMillis();
        Date start = new Date(iat);
        Date end = new Date(exp);

        // 添加自定义payload
        // user: content
        HashMap<String, Object> claimsMap = new HashMap<>(1);
        claimsMap.put("user", content);

        try {
            return Jwts.builder()
                    .setIssuer(issuer)
                    .setIssuedAt(start)
                    .setExpiration(end)
                    // 指定接受者（解析时可使用requireAudience进行验证）
                    .setAudience(audience)
                    // 添加payload，前方sub/iss/iat/exp/aud都将添加到payload中，请合理区分setClaims和addClaims的差异。
                    .addClaims(claimsMap)
                    // 指定签名秘钥
                    .signWith(generateKey());
        } catch (Exception e) {
            log.error(">>>>>>>>|JWT生成异常|e:{}|<<<<<<<<", e.getMessage(), e);
            throw new JWTException(SystemRetCodeConstants.JWT_GENERATE_ERROR);
        }
    }

    /**
     * 生成JJWT签名用密钥
     *
     * @return {@code javax.crypto.SecretKey}
     */
    private static SecretKey generateKey() {
        // signature签名秘钥
        // 密钥位数必须大于256位，一个字符按照8位算，至少32个字符。
        JWTProperties jwtProperties = SpringHelper.getBean(JWTProperties.class);
        byte[] jwtKeyBytes = jwtProperties.getSignatureSecret().getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(jwtKeyBytes, 0, jwtKeyBytes.length, "HmacSHA512");
    }
}
