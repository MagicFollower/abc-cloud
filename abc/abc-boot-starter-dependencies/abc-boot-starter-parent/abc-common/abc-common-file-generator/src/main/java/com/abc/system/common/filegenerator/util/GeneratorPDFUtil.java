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
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.OutputStream;
import java.io.StringWriter;

/**
 * 生成PDF工具类
 * <pre>
 * 1.关于renderer.createPDF(out, false);✨
 *   → 当调用`renderer.createPDF(outputStream);`方法时，第二个参数为`false`，它用于指示是否要使用PDF的PDF/X模式。
 *   → PDF/X是一种专门用于印刷和出版领域的PDF标准。它规定了一些特定的要求和限制，以确保生成的PDF文件能够在印刷流程中正确呈现。
 *   → 如果将第二个参数设置为`false`，则表示不使用PDF/X模式，生成的PDF将不需要符合PDF/X标准。这意味着生成的PDF文件可能包含一些不符合印刷要求的内容或特性。
 *   → 如果要生成符合PDF/X标准的PDF文件，将第二个参数设置为`true`即可。这样Flying Saucer会对生成的PDF文件应用特定的PDF/X规则，以确保它符合印刷要求。
 *   → 在一般情况下，如果你不需要生成符合印刷要求的PDF文件，可以将第二个参数设置为`false`。这样会减少一些额外的处理和验证步骤，提高生成PDF的性能。
 *
 * 2.关于renderer.finishPDF();✨
 *   → `renderer.finishPDF()`方法是在PDF生成完成后调用的一个方法，它用于完成PDF的生成并进行一些必要的清理操作。
 *   → 在调用`renderer.createPDF(outputStream)`方法后，PDF的生成过程会持续进行，直到对`finishPDF()`方法的调用。`finishPDF()`方法会完成PDF的生成，并对生成的PDF文件进行一些后处理操作。
 *   → 具体来说，`finishPDF()`方法会执行以下操作：
 *      → 1. 写入PDF文件的结尾标记，以标记PDF的结束。
 *      → 2. 关闭与PDF文件相关的资源，如字体、图像等，以确保资源被正确释放。
 *      → 3. 生成并添加任何需要的附加信息或元数据，如PDF/X的验证信息、PDF版本等。
 *  → 调用`finishPDF()`方法后，生成的PDF文件将被完整地写入输出流中，并且输出流将保持打开状态。
 *   → 在使用Flying Saucer生成PDF时，务必在完成所有需要生成PDF的操作后调用`finishPDF()`方法来确保生成的PDF文件完整且符合预期。
 *
 * 3.基本pdf导出示例✨
 * {@code
 *     @GetMapping("/03")
 *     public void demo03() {
 *         // Load HTML content from a file
 *         String htmlContent = "<html><body><h1>Example PDF Export</h1><p>This is a sample PDF generated using Flying Saucer PDF renderer.</p></body></html>";
 *         // Create an OutputStream to write the PDF
 *         try (OutputStream outputStream = new FileOutputStream("output.pdf")) {
 *             // Create an ITextRenderer instance
 *             ITextRenderer renderer = new ITextRenderer();
 *             // Set the base URL for resolved CSS files and images, if any
 *             renderer.setDocumentFromString(htmlContent);
 *             renderer.layout();
 *             // Generate the PDF
 *             renderer.createPDF(outputStream, false);
 *             renderer.finishPDF();
 *         } catch (DocumentException | IOException e) {
 *             throw new RuntimeException(e);
 *         }
 *     }
 * }
 * </pre>
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
        try (OutputStream out = new FastByteArrayOutputStream()) {
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
