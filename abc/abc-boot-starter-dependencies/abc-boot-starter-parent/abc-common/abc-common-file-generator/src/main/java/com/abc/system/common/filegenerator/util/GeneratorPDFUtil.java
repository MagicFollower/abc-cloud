package com.abc.system.common.filegenerator.util;

import com.abc.system.common.exception.business.BizException;
import com.abc.system.common.filegenerator.config.FreemarkerProperties;
import com.abc.system.common.filegenerator.constant.GenerateFileRetCodeConstants;
import com.abc.system.common.filegenerator.vo.ExportFileRequest;
import com.abc.system.common.helper.SpringHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;

/**
 * 生成PDF工具类
 *
 * @Description 生成PDF工具类
 * @Author Trivis
 * @Date 2023/5/10 20:09
 * @Version 1.0
 */
@Slf4j
public class GeneratorPDFUtil {

    private GeneratorPDFUtil() {

    }

    /**
     * 通过模板导出pdf文件
     *
     * @param exportFileRequest ExportFileRequest
     * @return OutputStream
     */
    public static OutputStream createPDF(ExportFileRequest exportFileRequest) {
        // 获取FreeMarker实例
        Configuration configuration = SpringHelper.getBean(Configuration.class);

        // 获取Freemarker配置信息
        FreemarkerProperties freemarkerProperties = SpringHelper.getBean(FreemarkerProperties.class);
        try (OutputStream out = new ByteArrayOutputStream()) {
            // 获取模板文件
            Template template = configuration.getTemplate(exportFileRequest.getTemplateFile(),
                    freemarkerProperties.getTemplateCharset());
            StringWriter writer = new StringWriter();
            // 将数据输出到html中
            template.process(exportFileRequest.getData(), writer);
            writer.flush();

            String html = writer.toString();
            ITextRenderer renderer = SpringHelper.getBean(ITextRenderer.class);
            // 把html代码传入渲染器中
            renderer.setDocumentFromString(html);
            // 解决图片相对路径的问题
            renderer.getSharedContext()
                    .setBaseURL(freemarkerProperties.getTemplatePath() + freemarkerProperties.getStaticSourcePath());
            renderer.layout();
            // 创建PDF流
            renderer.createPDF(out, false);
            renderer.finishPDF();
            out.flush();

            return out;
        } catch (TemplateNotFoundException e) {
            // 加载模板失败
            log.error(">>>>>>>>>|load template file exception|fileName:{}|<<<<<<<<",
                    exportFileRequest.getExportFileName(), e);
            final String MSG = GenerateFileRetCodeConstants.GENERATOR_TEMPLATE_LOAD_TEMPLATE_ERROR.getMessage()
                    + ",details:" + e.getMessage();
            throw new BizException(GenerateFileRetCodeConstants.GENERATOR_TEMPLATE_LOAD_TEMPLATE_ERROR.getCode(), MSG);
        } catch (Exception e) {
            // 创建文件失败
            log.error(">>>>>>>>>|create file exception|fileName:{}|<<<<<<<<", exportFileRequest.getExportFileName(), e);
            final String MSG = GenerateFileRetCodeConstants.GENERATOR_TEMPLATE_CREATE_FILE_ERROR.getMessage()
                    + ",details:" + e.getMessage();
            throw new BizException(GenerateFileRetCodeConstants.GENERATOR_TEMPLATE_CREATE_FILE_ERROR.getCode(), MSG);
        }
    }
}
