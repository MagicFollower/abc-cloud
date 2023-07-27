package com.abc.system.common.security.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA非对称加密工具包，提供功能如下
 * <pre>
 * 1.公钥加密，私钥解密
 * 2.私钥签名，公钥认证
 * 3.公钥加密解密，私钥加密解密
 * </pre>
 *
 * @Description RSAUtils
 * @Author Rake
 * @Date 2023/7/27 22:44
 * @Version 1.0
 */
public class RSAUtils {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtils.class);
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    private static final int KEY_SIZE = 1024;
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;
    private static final Map<String, Key> SEC_KEY_MAP = new HashMap<>(2);

    private RSAUtils() {
    }

    /**
     * 重新生成秘密对，生成后使用getPublicKey+getPrivateKey可获取到对应的公钥、私钥
     *
     * @throws Exception Exception
     */
    private static void genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(KEY_SIZE, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        SEC_KEY_MAP.put(PUBLIC_KEY, publicKey);
        SEC_KEY_MAP.put(PRIVATE_KEY, privateKey);
    }

    /**
     * 获取公钥
     *
     * @return String
     * @throws Exception Exception
     */
    public static String getPublicKey() throws Exception {
        Key key = getKey(PUBLIC_KEY);
        return new String(Base64.getEncoder().encode(key.getEncoded()), StandardCharsets.UTF_8);
    }

    /**
     * 获取私钥
     *
     * @return String
     * @throws Exception Exception
     */
    public static String getPrivateKey() throws Exception {
        Key key = getKey(PRIVATE_KEY);
        return new String(Base64.getEncoder().encode(key.getEncoded()), StandardCharsets.UTF_8);
    }


    /**
     * 公钥加密
     * <pre>
     * 【关于cipherAlgorithm的取值🤔️】
     * 除了RSA之外，还可以指定以下非对称加密算法：
     *   1.RSA
     *   2.DSA（Digital Signature Algorithm）：一种常用的数字签名算法，用于生成和验证数字签名。
     *   3.ECDSA（Elliptic Curve Digital Signature Algorithm）：基于椭圆曲线密码学的数字签名算法，与DSA相似，但使用椭圆曲线来提供相同的安全性但更短的密钥长度。
     *   4.Diffie-Hellman（DH）：用于密钥交换的算法，允许双方在不安全的通信渠道上协商共享密钥。
     *   5.ECDH（Elliptic Curve Diffie-Hellman）：基于椭圆曲线密码学的密钥交换算法，与Diffie-Hellman类似，但使用椭圆曲线来提供相同的安全性但更短的密钥长度。
     * 非对称加密算法的选择应该基于安全性和性能需求，以及所支持的平台和库的支持情况。为了确保正确选择，建议参考相关文档和标准，以及咨询安全专家的建议。
     * </pre>
     *
     * @param str             待加密字符串
     * @param publicKey       公钥
     * @param cipherAlgorithm 非对称加密算法
     * @return String
     * @throws Exception Exception
     */
    public static String encrypt(String str, String publicKey, String cipherAlgorithm) throws Exception {
        publicKey = keyTransformation(publicKey, "-----BEGIN PUBLIC KEY-----", "-----END PUBLIC KEY-----");
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(keyBytes));
        Cipher cipher;
        if (StringUtils.isNotEmpty(cipherAlgorithm)) {
            cipher = Cipher.getInstance(cipherAlgorithm);
        } else {
            cipher = Cipher.getInstance(KEY_ALGORITHM);
        }

        cipher.init(1, pubKey);
        return new String(Base64.getEncoder().encode(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8))),
                StandardCharsets.UTF_8);
    }

    /**
     * 私钥解密
     *
     * @param str             待解密字符串
     * @param privateKey      私钥
     * @param cipherAlgorithm cipherAlgorithm
     * @return String
     * @throws Exception Exception
     */
    public static String decrypt(String str, String privateKey, String cipherAlgorithm) throws Exception {
        privateKey = keyTransformation(privateKey, "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----");
        byte[] inputByte = Base64.getDecoder().decode(str);
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        Cipher cipher;
        if (StringUtils.isNotEmpty(cipherAlgorithm)) {
            cipher = Cipher.getInstance(cipherAlgorithm);
        } else {
            cipher = Cipher.getInstance(KEY_ALGORITHM);
        }

        cipher.init(2, priKey);
        return new String(cipher.doFinal(inputByte));
    }

    /**
     * 生成指定数据的数字签名
     *
     * @param data       待签名数据
     * @param privateKey 私钥
     * @return 签名（sign）
     * @throws Exception Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        privateKey = keyTransformation(privateKey, "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----");
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return new String(Base64.getEncoder().encode(signature.sign()), StandardCharsets.UTF_8);
    }

    /**
     * 验证数字签名
     *
     * @param data      待验证数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 文件是否验证通过
     * @throws Exception Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        publicKey = keyTransformation(publicKey, "-----BEGIN PUBLIC KEY-----", "-----END PUBLIC KEY-----");
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.getDecoder().decode(sign));
    }

    /* =============================================================== */
    /* ==========额外提供单独的公钥加密解密、私钥加密解密的公开通用方法========== */
    /* =============================================================== */

    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey, String cipherAlgorithm) throws Exception {
        privateKey = keyTransformation(privateKey, "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----");
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        return encryptAndDecrypt(encryptedData, keyFactory, priKey, 2, MAX_DECRYPT_BLOCK, cipherAlgorithm);
    }

    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey, String cipherAlgorithm) throws Exception {
        publicKey = keyTransformation(publicKey, "-----BEGIN PUBLIC KEY-----", "-----END PUBLIC KEY-----");
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key pubKey = keyFactory.generatePublic(x509KeySpec);
        return encryptAndDecrypt(encryptedData, keyFactory, pubKey, 2, MAX_DECRYPT_BLOCK, cipherAlgorithm);
    }

    public static byte[] encryptByPublicKey(byte[] data, String publicKey, String cipherAlgorithm) throws Exception {
        publicKey = keyTransformation(publicKey, "-----BEGIN PUBLIC KEY-----", "-----END PUBLIC KEY-----");
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key pubKey = keyFactory.generatePublic(x509KeySpec);
        return encryptAndDecrypt(data, keyFactory, pubKey, 1, MAX_ENCRYPT_BLOCK, cipherAlgorithm);
    }

    public static byte[] encryptByPrivateKey(byte[] data, String privateKey, String cipherAlgorithm) throws Exception {
        privateKey = keyTransformation(privateKey, "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----");
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        return encryptAndDecrypt(data, keyFactory, priKey, 1, MAX_ENCRYPT_BLOCK, cipherAlgorithm);
    }

    /* =============================================================== */
    /* ==========下方为内部使用私有方法========== */
    /* =============================================================== */

    private static String keyTransformation(String privateKey, String begin, String end) {
        return privateKey.replace(begin, "").replace(end, "").replace("\r\n", "").replace("\r", "").replace("\n", "");
    }

    private static Key getKey(String keyName) throws Exception {
        Key key = SEC_KEY_MAP.get(keyName);
        if (null == key) {
            try {
                genKeyPair();
                key = SEC_KEY_MAP.get(keyName);
            } catch (Exception var3) {
                LOGGER.error(">>>>>>>> Generate RSA public/private key failed|key:{}|exception: <<<<<<<<", keyName, var3);
                throw new Exception("Generate RSA public/private key failed|message:" + var3.getMessage());
            }
        }

        return key;
    }

    private static byte[] encryptAndDecrypt(byte[] data, KeyFactory keyFactory, Key key, int cipherMode, int maxBlock, String cipherAlgorithm) throws Exception {
        Cipher cipher;
        if (StringUtils.isNotEmpty(cipherAlgorithm)) {
            cipher = Cipher.getInstance(cipherAlgorithm);
        } else {
            cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        }

        cipher.init(cipherMode, key);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;

        for (int i = 0; inputLen - offSet > 0; offSet = i * maxBlock) {
            byte[] cache;
            if (inputLen - offSet > maxBlock) {
                cache = cipher.doFinal(data, offSet, maxBlock);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }

            out.write(cache, 0, cache.length);
            ++i;
        }

        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }
}

