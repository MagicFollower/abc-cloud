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
     * 根据code生成相应的一维码
     *
     * @param content 条形码内容
     * @param width   图片宽度
     * @param height  图片高度
     * @return 前端src直接使用的Base64字符串
     * @throws BizException BizException
     */
    public static String generateCode(String content, int width, int height) throws BizException {
        //定义位图矩阵BitMatrix
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
