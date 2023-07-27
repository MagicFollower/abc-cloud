package com.abc.system.web.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * ServletUtils
 *
 * @Description ServletUtils 详细介绍
 * @Author Trivis
 * @Date 2023/5/31 9:13
 * @Version 1.0
 */
@Slf4j
public class ServletUtils {

    private HttpServletResponse response;


    public static ServletUtils withInitial(HttpServletResponse response) {
        ServletUtils servletUtils = new ServletUtils();
        servletUtils.response = response;
        return servletUtils;
    }

    public static ServletUtils withInitial(Supplier<HttpServletResponse> supplier) {
        ServletUtils servletUtils = new ServletUtils();
        servletUtils.response = requireNonNull(requireNonNull(supplier, "supplier").get(), "supplier.get()");
        return servletUtils;
    }


    /**
     * 将字符串渲染到客户端
     * <pre>
     * 1.响应状态码：   200
     * 2.响应内容类型： application/json
     * 3.响应内容编码： UTF-8
     * 🤔️{@code response.getWriter().print(string);}和{@code response.getWriter().write(string);}的区别是什么？
     *   → print()方法会将给定的字符串写入到响应输出流中，但不会自动刷新输出流，需要手动调用flush()方法或者等待输出流关闭时才会将内容发送给客户端。print()方法可以接受任何类型的参数，并将其转换为字符串输出。
     *   → write()方法与print()方法类似，也会将给定的字符串写入到响应输出流中。但是，write()方法不仅会将字符串写入到响应输出流中，还会自动刷新输出流，将内容发送给客户端。write()方法只能接受字符串类型的参数。
     *   因此，如果需要手动控制输出流的刷新时机，可以使用print()方法；如果不需要手动控制输出流的刷新时机，可以使用write()方法，它会自动刷新输出流。
     * </pre>
     *
     * @param string 待渲染的字符串
     */
    public void renderString(String string) {
        PrintWriter writer = null;
        try {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            // text/html; charset=UTF-8
            //   -> 手动指定setCharacterEncoding是为了防止Content-Type设置为text/html但没有指定charset=UTF-8时渲染数据存在非ASCII字符出现的乱码场景😷
            // application/json; charset=UTF-8
            //   -> application/json;charset=UTF-8已经被Spring标记为了Deprecated，因为主流浏览器都能解析application/json中的非ASCII字符了😃
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            writer = response.getWriter();
            writer.write(string);
            writer.flush();
        } catch (IOException e) {
            log.error(">>>>>>>>|ServletUtils#renderString|exception:{}<<<<<<<<", e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 重定向
     *
     * @param urlOrUri 重定向URL/URI
     */
    public void redirectTo(String urlOrUri) {
        try {
            response.sendRedirect(urlOrUri);
        } catch (IOException e) {
            log.error(">>>>>>>>|ServletUtils#renderString|exception:{}<<<<<<<<", e.getMessage());
        }
    }
}
