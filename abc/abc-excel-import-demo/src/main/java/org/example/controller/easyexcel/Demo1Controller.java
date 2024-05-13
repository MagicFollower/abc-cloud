package org.example.controller.easyexcel;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * 测试MultipartFile接收文件
 */
@RestController
@RequestMapping("/multipartFile")
@RequiredArgsConstructor
public class Demo1Controller {

    @PostMapping("/uploadTest1")
    public void uploadTest1(@RequestParam(name = "file", required = false) MultipartFile[] files,
                             @RequestParam(name = "file", required = false) MultipartFile[] files1) {
        // 参数没传：null
        System.out.println("files = " + files);    // [Lorg.springframework.web.multipart.MultipartFile;@6d711871
        System.out.println("files1 = " + files1);  // [Lorg.springframework.web.multipart.MultipartFile;@4a5fc50
        System.out.println(files.equals(files));   // true（不同的数组地址）

        for (MultipartFile file : files) {
            System.out.println(file);  // @5caf937e（不同的数组地址，同一个对象引用）

            // logging.properties（文件名）
            String originalFilename = file.getOriginalFilename();
            System.out.println("originalFilename = " + originalFilename);
            // file（这里是参数的key）
            String name = file.getName();
            System.out.println("name = " + name);
            // Byte（long型的字节大小）
            long size = file.getSize();
            System.out.println("size = " + size);
        }

        System.out.println("====================");

        for (MultipartFile file : files1) {
            System.out.println(file);  // @5caf937e（不同的数组地址，同一个对象引用）

            // logging.properties（文件名）
            String originalFilename = file.getOriginalFilename();
            System.out.println("originalFilename = " + originalFilename);
            // file（这里是参数的key）
            String name = file.getName();
            System.out.println("name = " + name);
            // Byte（long型的字节大小）
            long size = file.getSize();
            System.out.println("size = " + size);
        }
    }

    @PostMapping("/uploadTest2")
    public void uploadTest2(@RequestParam(name = "file", required = false) List<MultipartFile> files) {
        // 参数没传：null
        System.out.println("files = " + files);

        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            System.out.println("originalFilename = " + originalFilename);
            String name = file.getName();
            System.out.println("name = " + name);
            long size = file.getSize();
            System.out.println("size = " + size);
        }
    }


    @PostMapping("/uploadTest3")
    public void uploadTest3(@RequestParam(name = "file") MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null) {
                try {
                    File localFile = new File(originalFilename);
                    Files.copy(file.getInputStream(), localFile.toPath());

                    // 确保文件已完全写入后再检查文件大小
                    long fileSize = localFile.length();
                    if (fileSize > 0) {
                        System.out.println("File uploaded successfully with size: " + fileSize);
                    } else {
                        System.out.println("File uploaded but size is 0.");
                    }
                } catch (IOException e) {
                    // 处理异常，比如文件写入失败
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
