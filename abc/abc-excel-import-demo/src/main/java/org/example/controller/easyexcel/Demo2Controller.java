package org.example.controller.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserImportDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 测试 EasyExcel 解析 Excel
 * <pre>
 * ⚠️⚠️⚠️EasyExcel数据所有cell处理时，都会自动移除数据首尾的空格⚠️⚠️⚠️
 * 1.类型为Long/Integer时，对于小数会自动截断取整;
 * 2.类型为Boolean时，只会处理EXCEL中的TRUE/FALSE;
 * 3.类型为String时，会自动移除首尾空格;
 * 4.类型为Date时，需要确保时间能被Excel解析为时间。
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/easyExcel")
@RequiredArgsConstructor
public class Demo2Controller {

    @PostMapping("/uploadTest1")
    public void uploadTest1(@RequestParam(name = "file") MultipartFile[] files) {
        MultipartFile firstFile = files[0];

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (InputStream is = firstFile.getInputStream()) {
            EasyExcel.read(is, UserImportDTO.class, new PageReadListener<>(dataList -> {
                dataList.forEach(row -> {
                    System.out.println(gson.toJson(row));
                });
                // sheet()不指定参数默认读取第一个
                // headRowNumber(1)会将前1行作为header，并跳过head解析
            })).sheet(0).headRowNumber(1).doRead();
        } catch (Exception ex) {
            log.error(">>> " + ex.getMessage(), ex);
        }
    }
}
