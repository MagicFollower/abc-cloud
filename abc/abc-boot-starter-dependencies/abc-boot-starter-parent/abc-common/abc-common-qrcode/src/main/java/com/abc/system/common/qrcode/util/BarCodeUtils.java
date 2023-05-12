package com.abc.system.common.qrcode.util;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.BizException;
import com.abc.system.common.qrcode.constant.ImageTypeEnum;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * 条形码生成工具类
 *
 * @Description 条形码生成工具类
 * @Author Trivis
 * @Date 2023/5/12 18:41
 * @Version 1.0
 */
@Slf4j
public class BarCodeUtils {

    private BarCodeUtils() {

    }

    /**
     * 生成条形码
     * <pre>
     *     直接向输出流中写入二维码
     * </pre>
     *
     * @param content 条形码内容
     * @param width   图片宽度
     * @param height  图片高度
     * @param stream  输出流
     * @throws BizException BizException
     */
    public static void createBarCode(String content, int width, int height, OutputStream stream) throws BizException {
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, width, height, null);
            MatrixToImageWriter.writeToStream(matrix, ImageTypeEnum.JPEG.getCode(), stream);
        } catch (Exception e) {
            log.error(">>>>>>>>>>> create bar code failed|exception: <<<<<<<<<<<", e);
            throw new BizException(SystemRetCodeConstants.SYSTEM_BUSINESS.getMessage(), "条形码生成失败，请稍候重试");
        } finally {
            if (Objects.nonNull(matrix)) {
                matrix.clear();
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
     * 根据content生成条形码
     *
     * @param content 条形码内容
     * @param width   图片宽度
     * @param height  图片高度
     * @return 前端src直接使用的Base64字符串
     * @throws BizException BizException
     */
    public static String createBarCodeString(String content, int width, int height) throws BizException {
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, width, height, null);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix, new MatrixToImageConfig());
            return QRCodeUtils.getImageBase64Str(image, ImageTypeEnum.JPEG.getCode());
        } catch (Exception e) {
            log.error(">>>>>>>>>>> create bar code failed|exception: <<<<<<<<<<<", e);
            throw new BizException(SystemRetCodeConstants.SYSTEM_BUSINESS.getMessage(), "条形码生成失败，请稍候重试");
        } finally {
            if (Objects.nonNull(matrix)) {
                matrix.clear();
            }
        }
    }


}
