package com.abc.business.images.controller;

import com.abc.business.images.domain.dto.ImageUploadDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author trivis
 */
@RestController
@RequestMapping("/images")
public class ImageController {

    @PostMapping("/upload_s")
    public void singleImageImport(@RequestBody ImageUploadDTO imageUploadInfo) {

        try (BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream("backup-" + imageUploadInfo
                .getFilename()))) {
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
            try (
                    OutputStream os = new FileOutputStream(filename);
                    InputStream is = multipartFile.getInputStream();
            ) {
                is.transferTo(os);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * qs.stringify(a, {arrayFormat: "repeat"})  ->  默认indices
     * qs.stringify(a, {addQueryPrefix: true})   ->  默认false
     *
     * qs.parse("name=xiaoMing&age=18&hobby=hobby-1&hobby=hobby-2&hobby=hobby-3")
     * qs.parse("?name=xiaoMing&age=18&hobby=hobby-1%2Chobby-2%2Chobby-3", {ignoreQueryPrefix: true})
     *
     * @param ids
     */
    @PostMapping("/test")
    public void test(String[] ids){
        System.out.println("ids = " + ids);
    }
    
    @PostMapping("/test2")
    public void test2(@RequestBody UpdateDTO updateDTO, @RequestAttribute("uid") String userId){
        System.out.println("updateDTO = " + updateDTO);
    }


    @Getter
    @Setter
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    static class UpdateDTO {
        List<String> ids;
    }
}
