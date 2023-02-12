package com.abc.business.images.controller;

import com.abc.business.images.domain.dto.ImageUploadDTO;
import com.abc.business.utils.ZipUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author trivis
 */
@Slf4j
@RestController
@RequestMapping("/images")
public class ImageController {

    @PostMapping("/upload_s")
    public void singleImageImport(@RequestBody ImageUploadDTO imageUploadInfo) {

        try (BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream("backup-" + imageUploadInfo.getFilename()))) {
            bo.write(Base64Utils.decodeFromString(imageUploadInfo.getImage()));
            bo.flush();
        } catch (IOException e) {
            // todo log
            throw new RuntimeException(e);
        }

    }

    /**
     * 根据表单属性解析出Multipart，可以使用@RequestPart、也可以使用@RequestParam.
     *
     * @param fileArray 多文件
     */
    @PostMapping("/upload")
    public void multiImageImport(@RequestPart("files") MultipartFile[] fileArray) {

        for (MultipartFile multipartFile : fileArray) {
            String filename = Objects.requireNonNull(multipartFile.getOriginalFilename());
            try (OutputStream os = new FileOutputStream(filename); InputStream is = multipartFile.getInputStream()) {
                is.transferTo(os);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 批量导出图片到zip（指定大小）
     *
     * @param response HttpServletResponse
     */
    @GetMapping("/download_zip")
    public void zipDownload(HttpServletResponse response) {
        final String zipFilename = "files.zip";
        int zipLength = 0;
        List<String> filenames = new ArrayList<>();
        List<File> fileList = new ArrayList<>();
        filenames.add("abc.png");
        filenames.add("Java面试必知必会.pdf");
        filenames.add("abc.xlsx");
        filenames.add("abc.xlsx");
        for (String filename : filenames) {
            try {
                File file = new ClassPathResource(filename).getFile();
                fileList.add(file);
                zipLength += file.length();
            } catch (IOException e) {
                // todo log
                throw new RuntimeException(e);
            }
        }
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" +
                URLEncoder.encode(zipFilename, StandardCharsets.UTF_8));
        response.setHeader(HttpHeaders.CONTENT_LENGTH, "" + zipLength * 2);

        String password = "123456";
        try {
            ZipUtils.init(password).to(response.getOutputStream(), fileList);
        } catch (Exception e) {
            log.error("文件下载异常", e);
        }
    }

    /**
     * 批量导出图片到zip（不指定大小）
     *
     * @param response HttpServletResponse
     */
    @GetMapping("/download_zip1")
    public void zipDownload1(HttpServletResponse response) {
        final String zipFilename = "files.zip";
        List<String> filenames = new ArrayList<>();
        List<File> fileList = new ArrayList<>();
        filenames.add("abc.png");
        filenames.add("Java面试必知必会.pdf");
        filenames.add("abc.xlsx");
        filenames.add("abc.xlsx");
        filenames.add("abc.xlsx");
        for (String filename : filenames) {
            try {
                File file = new ClassPathResource(filename).getFile();
                fileList.add(file);
            } catch (IOException e) {
                // todo log
                throw new RuntimeException(e);
            }
        }
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" +
                URLEncoder.encode(zipFilename, StandardCharsets.UTF_8));

        String password = "123456";
        try {
            // ZipUtils.init().to(response.getOutputStream(), fileList);
            ZipUtils.init(password).to(response.getOutputStream(), fileList);
        } catch (Exception e) {
            log.error("文件下载异常", e);
            // 重置reset，使得return返回数据生效（忽略response中下载相关配置）
            // 正常不会这么做，必须确保下载正常，下载接口不提供返回值。如果异常，需要手动排查。
            // response.reset();
            // return "error";
        }
    }

    /**
     * 使用Content-Disposition进行图片下载
     *
     * @param response HttpServletResponse
     */
    @GetMapping("/download_s1")
    public void singleDownload1(HttpServletResponse response) {
        // final String filename = "abc.png";
        // final String filename = "Java面试必知必会.pdf";
        final String filename = "abc.xlsx";
        ClassPathResource classPathResource = new ClassPathResource(filename);
        try (InputStream is = classPathResource.getInputStream()) {
            // 需要主动暴露Content-Disposition，否则Axios获取不到响应头的这个header属性
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
            response.addHeader("Content-Length", "" + is.available());
            response.addHeader("Content-Type", MediaType.TEXT_XML_VALUE);

            is.transferTo(response.getOutputStream());
            //final int BUFFER_SIZE = 10 * 1024 * 1024;
            //byte[] buf = new byte[BUFFER_SIZE];
            //int read;
            //while ((read = is.read(buf, 0, BUFFER_SIZE)) >= 0) {
            //    os.write(buf, 0, read);
            //}
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用自定义header的方式，实现图片下载（推荐）
     * 1. 文件长度推荐从File中获取length():long, InputStream中的available()返回的数据为int型，大小阈值2G.
     *
     * @param response HttpServletResponse
     */
    @GetMapping("/download_s2")
    public void singleDownload2(HttpServletResponse response) {
        final String filename = "abc.png";
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Access-Control-Expose-Headers", "File-Name,File-Type");
        response.addHeader("File-Name", "" + filename);
        response.addHeader("File-Type", "" + getContentType(filename));

        ClassPathResource classPathResource = new ClassPathResource(filename);
        try (InputStream is = classPathResource.getInputStream()) {
            response.addHeader("Content-Length", "" + classPathResource.getFile().length());
            is.transferTo(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * qs.stringify(a, {arrayFormat: "repeat"})  ->  默认indices
     * qs.stringify(a, {addQueryPrefix: true})   ->  默认false
     * <p>
     * qs.parse("name=xiaoMing&age=18&hobby=hobby-1&hobby=hobby-2&hobby=hobby-3")
     * qs.parse("?name=xiaoMing&age=18&hobby=hobby-1%2Chobby-2%2Chobby-3", {ignoreQueryPrefix: true})
     *
     * @param ids ids
     */
    @PostMapping("/test")
    public void test(String[] ids) {
        System.out.println("ids = " + Arrays.toString(ids));
    }

    @PostMapping("/test2")
    public void test2(@RequestBody UpdateDTO updateDTO, @RequestAttribute("uid") String userId) {
        System.out.println("updateDTO = " + updateDTO);
        System.out.println(userId);
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    static class UpdateDTO {
        List<String> ids;
    }

    private static String getContentType(String fileName) {
        return MediaTypeFactory.getMediaType(fileName).orElse(MediaType.TEXT_PLAIN).getType();
    }
}
