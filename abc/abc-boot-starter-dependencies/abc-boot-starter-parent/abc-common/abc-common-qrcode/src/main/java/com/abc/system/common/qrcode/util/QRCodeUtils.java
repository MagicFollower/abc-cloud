package com.abc.system.common.qrcode.util;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.business.BizException;
import com.abc.system.common.qrcode.constant.ImageTypeEnum;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 二维码生成工具类
 *
 * @Description 二维码生成工具类
 * @Author Trivis
 * @Date 2023/5/12 18:50
 * @Version 1.0
 */
@Slf4j
public class QRCodeUtils {

    private QRCodeUtils() {

    }

    /**
     * 生成二维码
     * <pre>
     *     直接向输出流中写入二维码
     * </pre>
     *
     * @param content 二维码内容
     * @param width   二维码宽度
     * @param height  二维码高度
     * @param stream  输出流
     * @throws BizException BizException
     */
    public static void createQRCode(String content, int width, int height, OutputStream stream) throws BizException {
        Map<EncodeHintType, Object> hints = getHintTypeObjectMap();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content,
                    BarcodeFormat.QR_CODE, width, height, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, ImageTypeEnum.JPEG.getCode(), stream);
        } catch (Exception e) {
            log.error(">>>>>>>>>>> create QR code failed|exception: <<<<<<<<<<<", e);
            throw new BizException(SystemRetCodeConstants.SYSTEM_BUSINESS.getMessage(), "二维码生成失败，请稍候重试");
        } finally {
            if (Objects.nonNull(bitMatrix)) {
                bitMatrix.clear();
            }
            if (Objects.nonNull(stream)) {
                try {
                    stream.close();
                } catch (IOException e) {
                    log.error(">>>>>>>>>>>create QR code|close stream failed|exception: <<<<<<<<<<<", e);
                }
            }
        }
    }

    /**
     * 将生成的二维码转换为前端页面展示的base64（包含prefix）
     *
     * @param content 二维码内容
     * @param width   二维码宽度
     * @param height  二维码高度
     * @return 前端src直接使用的Base64字符串
     * @throws BizException 校验异常
     */
    public static String createQRCode(String content, int width, int height) throws BizException {
        Map<EncodeHintType, Object> hints = getHintTypeObjectMap();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content,
                    BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix, new MatrixToImageConfig());
            return getImageBase64Str(image, ImageTypeEnum.JPEG.getCode());
        } catch (Exception e) {
            log.error(">>>>>>>>>>> create QR code convert base64 encoder failed|exception: <<<<<<<<<<<", e);
            throw new BizException(SystemRetCodeConstants.SYSTEM_BUSINESS.getMessage(), "二维码生成失败，请稍候重试");
        } finally {
            if (Objects.nonNull(bitMatrix)) {
                bitMatrix.clear();
            }
        }
    }

    private static Map<EncodeHintType, Object> getHintTypeObjectMap() {
        Map<EncodeHintType, Object> hints = new HashMap<>(3);
        // 白边的宽度: 0~4
        hints.put(EncodeHintType.MARGIN, 0);
        // 字符集编码: UTF-8
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
        // 容错级别: H(L、M、Q、H)，容错级别越高，二维码密度越小
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        return hints;
    }

    /**
     * 将图片缓冲BufferedImage(java.awt.image)转换为前端src直接使用的Base64字符串
     *
     * @param image BufferedImage(java.awt.image)
     * @return 前端src直接使用的Base64字符串
     * @throws IOException IOException
     */
    public static String getImageBase64Str(BufferedImage image, String imageType) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            String imageTypeLower = imageType.toLowerCase();
            ImageIO.write(image, imageTypeLower, byteArrayOutputStream);
            // Base64编码
            String imageBase64 = Base64Utils.encodeToString(byteArrayOutputStream.toByteArray());
            // 移除 \r\n
            // Base64编码后的数据可能包含\r或\n。这是因为在编码过程中，有些编码器会在一定数量的字符后插入换行符以便于显示或传输。但是，这些换行符不影响数据的正确性。
            imageBase64 = imageBase64.replaceAll("\n", "").replaceAll("\r", "");
            // 拼接前端src直接使用的Base64数据
            return "data:image/" + imageTypeLower + ";base64," + imageBase64;
        }
    }
}
