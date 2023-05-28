package com.abc.system.common.security.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

/**
 * AES工具包
 *
 * @Description AES工具包
 * @Author Trivis
 * @Date 2023/5/28 8:38
 * @Version 1.0
 */
public class AESUtils {
    private static final Logger log = LoggerFactory.getLogger(AESUtils.class);
    private final String content;

    public AESUtils(String content) {
        this.content = content;
    }

    public String encrypt(String encryptSecret) {
        Key key = this.getKey(encryptSecret);
        byte[] result = null;

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(ENCRYPT_MODE, key);
            result = cipher.doFinal(this.content.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.info(">>>>>>>>|aes encrypted error:{}|<<<<<<<<", e.getMessage(), e);
        }

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < result.length; ++i) {
            String hex = Integer.toHexString(result[i] & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            sb.append(hex.toUpperCase());
        }

        return sb.toString();
    }

    public String decrypt(String encryptSecret) {
        if (this.content.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[this.content.length() / 2];

            for (int i = 0; i < this.content.length() / 2; ++i) {
                int high = Integer.parseInt(this.content.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(this.content.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte) (high * 16 + low);
            }

            Key key = this.getKey(encryptSecret);
            byte[] decrypt = null;

            try {
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(DECRYPT_MODE, key);
                decrypt = cipher.doFinal(result);
            } catch (Exception e) {
                log.info(">>>>>>>>|aes decrypt error:{}|<<<<<<<<", e.getMessage(), e);
            }
            if (decrypt == null) {
                throw new RuntimeException("decrypt byte stream is null");
            }
            return new String(decrypt, StandardCharsets.UTF_8);
        }
    }

    private Key getKey(String secret) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(secret.getBytes(StandardCharsets.UTF_8));
            generator.init(128, random);
            return generator.generateKey();
        } catch (Exception e) {
            log.error(">>>>>>>>|get key other exception:{}|<<<<<<<<", e.getMessage(), e);
            throw new RuntimeException("decrypt byte stream is null");
        }
    }
}