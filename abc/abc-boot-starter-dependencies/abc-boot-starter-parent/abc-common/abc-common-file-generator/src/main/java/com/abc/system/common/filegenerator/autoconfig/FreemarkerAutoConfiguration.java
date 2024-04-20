package com.abc.system.common.filegenerator.autoconfig;

import com.abc.system.common.filegenerator.config.FreemarkerProperties;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * Freemarker自动配置类
 *
 * @Description Freemarker自动配置类
 * @Author [author_name]
 * @Date 2077/5/10 20:20
 * @Version 1.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(FreemarkerProperties.class)
@ConditionalOnClass(Template.class)
@RequiredArgsConstructor
public class FreemarkerAutoConfiguration {

    private final FreemarkerProperties freemarkerProperties;

    /**
     * Freemarker配置初始化
     *
     * @return freemarker.template.Configuration
     */
    @Bean
    public freemarker.template.Configuration configuration() {
        freemarker.template.Configuration configuration =
                new freemarker.template.Configuration(freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        // FTL模板位置
        configuration.setClassForTemplateLoading(FreemarkerAutoConfiguration.class, freemarkerProperties.getTemplatePath());
        // FTL字符集/编码
        configuration.setEncoding(Locale.CHINA, freemarkerProperties.getTemplateCharset());
        return configuration;
    }

    /**
     * 初始化IText实例（PDF渲染）
     * <pre>
     * ✨在使用iText库时，BaseFont类提供了一些常量来指定字体的编码和嵌入行为。
     * 其中，IDENTITY_H表示使用Unicode编码，EMBEDDED表示字体文件被嵌入到生成的PDF文件中。
     * 具体作用如下：
     *   → BaseFont.IDENTITY_H：指定字体使用Unicode编码，这意味着可以支持各种语言和字符集，包括中文、日文、韩文等。使用IDENTITY_H可以确保正确显示PDF中的中文字符。
     *   → BaseFont.EMBEDDED：指定字体文件被嵌入到生成的PDF文件中。默认情况下，iText会将字体文件以子集方式包含在PDF中，这意味着只会包含文档中使用的字符。嵌入字体可以确保生成的PDF文件在其他环境中也能正确显示，而不依赖于操作系统或应用程序的字体设置。
     *   → 在使用Flying Saucer或iText时，指定BaseFont.IDENTITY_H可以确保正确支持Unicode字符，指定BaseFont.EMBEDDED可以确保字体文件被嵌入到生成的PDF文件中，以便在不同环境下正确显示。这对于显示中文字符非常重要，因为中文字符通常不包含在默认字体中。
     *
     * ✨字体文件为什么以ttc结尾? (TrueType, OpenType)
     * 字体文件的扩展名取决于字体的文件格式。通常，TrueType字体文件使用`.ttf`扩展名，而OpenType字体文件使用`.otf`扩展名。
     * 然而，有时你可能会遇到以`.ttc`结尾的字体文件。`.ttc`（TrueType Collection）是一种特殊的字体格式，它可以在单个文件中包含多个TrueType字体。每个TrueType字体都可以具有不同的字体样式，例如正常、粗体、斜体等。`.ttc`文件可以节省磁盘空间，并方便管理多个相关字体。
     * `.ttc`文件中的多个字体可以通过索引来访问，每个索引对应于一个字体。因此，当需要使用`.ttc`文件中的特定字体时，你需要指定字体文件和具体的索引。
     * 对于iText中的`addFont()`方法，如果你希望添加`.ttc`文件中的特定字体，需要将`.ttc`文件作为字体文件路径的参数，并使用具体的索引来指定要添加的字体。例如：
     * {@code
     *    renderer.getFontResolver().addFont("path/to/fontfile.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
     * }
     * 上述示例中，`.ttc`文件的路径是`path/to/fontfile.ttc`，并且我们使用索引`0`来指定要添加的字体。确保替换实际的`.ttc`文件路径和索引。
     * </pre>
     *
     * @return ITextRenderer
     */
    @Bean
    public ITextRenderer iTextRenderer() throws DocumentException, IOException {
        ITextRenderer iTextRenderer = new ITextRenderer();
        String fontDirectoryPath = freemarkerProperties.getFontPath();
        // 获取目录中所有字体文件
        File fileDir;
        try {
            // 目录不存在时，抛出FileNotFoundException
            fileDir = new ClassPathResource(fontDirectoryPath).getFile();
            File[] fontFiles = fileDir.listFiles();
            if (fontFiles != null) {
                for (File fontFile : fontFiles) {
                    if (fontFile.isFile()) {
                        // 添加字体文件到字体解析器
                        log.info(">>>>>>>>|加载字体文件:{}|<<<<<<<<", fontFile.getName());
                        iTextRenderer.getFontResolver()
                                .addFont(fontFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                    }
                }
            }
        } catch (IOException e) {
            log.info(">>>>>>>>|字体目录未配置|<<<<<<<<");
            return iTextRenderer;
        }
        return iTextRenderer;
    }
}
