package com.abc.system.common.security.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

/**
 * <pre>
 * AES工具包（自定义实现）
 * {@code
 *         System.out.println(AESUtils.withInitial("my_secret_key")
 *                 .encrypt("你好"));
 *         System.out.println(AESUtils.withInitial("my_secret_key")
 *                 .decrypt("A0082734A9C2180CD2F9DE0A4EAD6EE0"));
 * }
 *
 * 推荐使用`jncryptor`
 * {@code
 *      <dependency>
 *          <groupId>org.cryptonode.jncryptor</groupId>
 *          <artifactId>jncryptor</artifactId>
 *          <version>1.2.0</version>
 *      </dependency>
 *
 *      String data = "你好";
 *      String key = "my_secret_key";
 *      JNCryptor crypto = new AES256JNCryptor();
 *      byte[] ciphertext = crypto.encryptData(data.getBytes(StandardCharsets.UTF_8), key.toCharArray());
 *      String x1 = Base64Utils.encodeToString(ciphertext);
 *      System.out.println("x1 = " + x1);
 *
 *      byte[] originData = crypto.decryptData(Base64Utils.decodeFromString(x1), key.toCharArray());
 *      System.out.println(new String(originData, StandardCharsets.UTF_8));
 * }
 *
 * ⚠️注意1：为什么Java中的AES加密和Mysql中AES加密的输出结果不同？
 * {@code
 *      Java 和 MySQL 中的 `AES_ENCRYPT()` 函数虽然使用相同的加密算法，但是它们的实现方式可能有所不同，因此输出结果也不一定相同。
 *      其中一个可能的原因是，Java 和 MySQL 使用的填充模式（padding mode）不同。
 *        → 在 AES 加密中，填充模式用于将数据块扩展到加密算法所需的长度。
 *          → Java 默认使用的是 PKCS5Padding 或 PKCS7Padding 填充模式，
 *          → 而 MySQL 使用的是 ZeroPadding 填充模式。
 *          → 这意味着，如果你在 Java 中加密一个数据块，然后使用 MySQL 进行解密，或者反过来，你可能会得到不同的结果，因为填充模式不同。
 *        → 另一个可能的原因是，Java 和 MySQL 使用的是不同的加密模式（encryption mode）。
 *          → 在 AES 加密中，加密模式用于指定如何将数据块链接在一起。
 *            → Java 默认使用的是 CBC（Cipher Block Chaining）模式；
 *            → 而 MySQL 使用的是 ECB（Electronic Codebook）模式。这也可能导致输出结果不同。
 *      因此，如果你需要在 Java 和 MySQL 中使用相同的加密算法并获得相同的输出结果，需要确保它们使用相同的填充模式和加密模式。你可以在 Java 中使用 `Cipher` 类来指定填充模式和加密模式，并在 MySQL 中使用 `AES_ENCRYPT()` 函数的选项参数来指定填充模式和加密模式。
 * }
 *
 * ⚠️注意2：在 MySQL 中，AES 加密和解密时使用 PKCS7Padding 是不支持的。
 *   → MySQL 使用的是一种名为“zero-padding”的填充模式，这是一种简单的填充模式，只是在明文的末尾添加零字节，以确保每个加密块都是相同的大小。
 *
 * ⚠️注意3：关于PKCS5Padding？
 *   ❓PKCS5Padding是什么？PKCS是什么意思？是什么单词的缩写？
 *     → PKCS5Padding 是一种填充方式，用于在加密时将数据填充到指定的块大小。PKCS 是 Public Key Cryptography Standards 的缩写，是一组密码学标准。PKCS#5 是其中的一个标准，定义了一种填充方式，可以在加密时将数据填充到指定的块大小。
 *     → 具体来说，PKCS5Padding 使用的是一种称为 PKCS#5 的算法，它将数据分成固定大小的块，然后在块的末尾填充一些字节，使得块的大小达到指定的长度。填充字节的值等于需要填充的字节数，比如需要填充 3 个字节，则填充 0x03。
 *     → 在这个例子中，使用了 AES/CBC/PKCS5Padding 加密方式，其中 PKCS5Padding 表示使用 PKCS#5 填充方式。块大小为 16 个字节（128 位），因此如果原始数据长度不是 16 的倍数，则会进行填充。填充后的数据长度总是 16 的倍数。
 *   ❓PKCS#5和PKCS#7有什么差别？
 *     → PKCS#5 和 PKCS#7 都是密码学标准，它们的主要区别在于填充方式的设计。
 *     → PKCS#5 定义了一种填充方式，称为 PKCS#5 Padding，它适用于块大小为 8 个字节的加密算法，比如 DES。PKCS#5 Padding 的填充方式与 PKCS#7 Padding 基本相同，只是在填充的时候需要满足一些额外的限制。因此，PKCS#5 Padding 可以看作是 PKCS#7 Padding 的一个子集。
 *     → PKCS#7 定义了一种通用的填充方式，可以适用于任意块大小的加密算法，比如 AES、TripleDES 等。PKCS#7 Padding 的填充方式与 PKCS#5 Padding 基本相同，只是在填充的时候没有额外的限制。因此，PKCS#7 Padding 更加通用，可以适用于更多的加密算法。
 *     → 在实际应用中，PKCS#7 Padding 更加常用，因为它可以适用于任意块大小的加密算法。而 PKCS#5 Padding 则主要用于一些旧的加密算法，比如 DES。
 * </pre>
 *
 * @Description AES工具包
 * @Author Trivis
 * @Date 2023/5/28 8:38
 * @Version 1.0
 */
public class AESUtils {
    private static final Logger log = LoggerFactory.getLogger(AESUtils.class);
    private final String encryptSecret;

    private AESUtils(String encryptSecret) {
        this.encryptSecret = encryptSecret;
    }

    public static AESUtils withInitial(String encryptSecret) {
        return new AESUtils(encryptSecret);
    }

    public static AESUtils withInitial(Supplier<String> supplier) {
        return new AESUtils(requireNonNull(supplier, "supplier").get());
    }

    public String encrypt(String content) {
        Key key = this.getKey(encryptSecret);
        byte[] result = null;

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(ENCRYPT_MODE, key);
            result = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.info(">>>>>>>>|aes encrypted error:{}|<<<<<<<<", e.getMessage(), e);
        }

        StringBuilder sb = new StringBuilder();

        if (result != null) {
            for (byte b : result) {
                String hex = Integer.toHexString(b & 255);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }

                sb.append(hex.toUpperCase());
            }
        }

        return sb.toString();
    }

    public String decrypt(String content) {
        if (content.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[content.length() / 2];

            for (int i = 0; i < content.length() / 2; ++i) {
                int high = Integer.parseInt(content.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(content.substring(i * 2 + 1, i * 2 + 2), 16);
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
            // 创建SHA-256哈希实例
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // 计算哈希值
            byte[] hash = digest.digest(secret.getBytes(StandardCharsets.UTF_8));
            // 创建SecretKeySpec对象
            return new SecretKeySpec(hash, "AES");
        } catch (NoSuchAlgorithmException e) {
            log.error(">>>>>>>>|get key exception:{}|<<<<<<<<", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


//    private Key getKey(String secret) {
//        try {
//            KeyGenerator generator = KeyGenerator.getInstance("AES");
//            // PRNG是伪随机数发生器（Pseudo-Random Number Generator）的缩写。
//            /*
//                在SHA1PRNG和NativePRNG之间选择更好的安全随机数生成器取决于您的具体需求和环境。以下是一些比较：
//                  → 性能： NativePRNG通常比SHA1PRNG更快，因为它是使用本地操作系统的随机数生成器实现的。但是，如果您的应用程序需要在不同的操作系统之间移植，那么SHA1PRNG可能更可靠，因为它是Java平台独立的。
//                  → 安全性： SHA1PRNG和NativePRNG都被认为是安全的随机数生成器。但是，SHA1PRNG使用SHA-1算法，而SHA-1已经被认为是不够安全，因为它容易受到碰撞攻击。因此，如果您需要更高的安全性，那么NativePRNG可能更好，因为它可以使用更强大的算法，如SHA-256或SHA-512。
//                  → 可用性： SHA1PRNG是Java平台默认提供的随机数生成器，因此它应该在所有Java环境中都可用。但是，NativePRNG可能只在某些操作系统上可用，因此您需要检查您的操作系统是否支持它。
//                综上所述，如果您需要高性能和可移植性，那么SHA1PRNG可能更适合您。如果您需要更高的安全性和更强大的算法支持，并且您的应用程序只在特定的操作系统上运行，那么NativePRNG可能更好。
//             */
//            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
//            random.setSeed(secret.getBytes(StandardCharsets.UTF_8));
//            generator.init(128, random);
//            return generator.generateKey();
//        } catch (Exception e) {
//            log.error(">>>>>>>>|get key other exception:{}|<<<<<<<<", e.getMessage(), e);
//            throw new RuntimeException("decrypt byte stream is null");
//        }
//    }
}
