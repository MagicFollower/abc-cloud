//import com.abc.system.common.security.util.AESUtils;
//import org.cryptonode.jncryptor.AES256JNCryptor;
//import org.cryptonode.jncryptor.CryptorException;
//import org.cryptonode.jncryptor.JNCryptor;
//import org.junit.jupiter.api.Test;
//import org.springframework.util.Base64Utils;
//
//import java.io.UnsupportedEncodingException;
//import java.nio.charset.StandardCharsets;
//import java.security.SecureRandom;
//
///**
// * AESUtilsTest
// *
// * @Description AESUtilsTest 详细介绍
// * @Author Trivis
// * @Date 2023/6/12 10:09
// * @Version 1.0
// */
//public class AESUtilsTest {
//
//    @Test
//    void test01() throws CryptorException {
//        System.out.println(AESUtils.withInitial("my_secret_key")
//                .encrypt("你好"));
//        System.out.println(AESUtils.withInitial("my_secret_key")
//                .decrypt("110D083C9C9C2578773B20AE55047C29"));
//
//        String data = "你好";
//        String key = "my_secret_key";
//        JNCryptor crypto = new AES256JNCryptor();
//        byte[] ciphertext = crypto.encryptData(data.getBytes(StandardCharsets.UTF_8), key.toCharArray());
//        String x1 = Base64Utils.encodeToString(ciphertext);
//        System.out.println("x1 = " + x1);
//
//        byte[] originData = crypto.decryptData(Base64Utils.decodeFromString(x1), key.toCharArray());
//        System.out.println(new String(originData, StandardCharsets.UTF_8));
//
//    }
//}
