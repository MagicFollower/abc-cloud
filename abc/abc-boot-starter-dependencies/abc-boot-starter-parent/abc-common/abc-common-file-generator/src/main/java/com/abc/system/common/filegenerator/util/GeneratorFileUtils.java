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
import org.springframework.util.FastByteArrayOutputStream;

import java.io.OutputStream;
import java.io.StringWriter;

/**
 * 生成文件工具类
 *
 * @Description 生成文件工具类
 * @Author Trivis
 * @Date 2023/5/10 20:09
 * @Version 1.0
 */
@Slf4j
public class GeneratorFileUtils {

    private GeneratorFileUtils() {
    }

    /**
     * 导出Excel文件
     *
     * @param exportFileRequest 文件导出参数实体
     * @return OutputStream 文件输出流（OutputStream → FastByteArrayOutputStream）
     */
    public static OutputStream createFile(ExportFileRequest exportFileRequest) {
        Configuration configuration = SpringHelper.getBean(Configuration.class);
        FreemarkerProperties freemarkerProperties = SpringHelper.getBean(FreemarkerProperties.class);
        try (OutputStream out = new FastByteArrayOutputStream();) {
            // 获取模板、填充数据
            Template template = configuration.getTemplate(exportFileRequest.getTemplateFile(), freemarkerProperties.getTemplateCharset());
            StringWriter writer = new StringWriter();
            template.process(exportFileRequest.getData(), writer);
            writer.flush();
            out.write(writer.toString().getBytes(freemarkerProperties.getTemplateCharset()));
            out.flush();
            return out;
        } catch (TemplateNotFoundException e) {
            // 加载模板失败
            log.error(">>>>>>>>>>> load template file exception|fileName:{} <<<<<<<<<<<", exportFileRequest.getExportFileName(), e);
            throw new BizException(GenerateFileRetCodeConstants.GENERATOR_TEMPLATE_LOAD_TEMPLATE_ERROR.getCode(),
                    GenerateFileRetCodeConstants.GENERATOR_TEMPLATE_LOAD_TEMPLATE_ERROR.getMessage() + ",details:" + e.getMessage());
        } catch (Exception e) {
            // 创建文件失败
            log.error(">>>>>>>>>>> create file exception|fileName:{} <<<<<<<<<<<", exportFileRequest.getExportFileName(), e);
            throw new BizException(GenerateFileRetCodeConstants.GENERATOR_TEMPLATE_CREATE_FILE_ERROR.getCode(),
                    GenerateFileRetCodeConstants.GENERATOR_TEMPLATE_CREATE_FILE_ERROR.getMessage() + ",details:" + e.getMessage());
        }
    }
}
