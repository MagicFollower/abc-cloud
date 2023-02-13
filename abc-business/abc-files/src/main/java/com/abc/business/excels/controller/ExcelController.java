package com.abc.business.excels.controller;

import com.abc.business.excels.domain.dto.UserExcelExportDTO;
import com.abc.business.excels.domain.dto.UserExcelImportDTO;
import com.abc.business.excels.domain.entity.User;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author trivis
 */
@Slf4j
@RestController
@RequestMapping("/excels")
public class ExcelController {

    /**
     * 解析Excel
     * 1. Tips：检测相关的工作请在前端完成
     * 2. 默认值
     * 2.1 PageReadListener.BATCH_COUNT=100                         -> 默认每组100个数据
     * 2.2 EasyExcel.read(a,b,c).sheet().headRowNumber(1).doRead()  -> 默认读取第一个sheet，header为第一行
     *
     * @param file MultipartFile
     * @return "ok"/"error"
     */
    @PostMapping("/upload")
    public String excelUpload(@RequestPart("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            // 默认BATCH_COUNT=100
            PageReadListener.BATCH_COUNT = 5;
            EasyExcel.read(inputStream, UserExcelImportDTO.class, new PageReadListener<UserExcelImportDTO>(dataList -> {
                for (UserExcelImportDTO demoData : dataList) {
                    log.info("读取到一条数据{}", JSONObject.toJSONString(demoData));
                }
            })).sheet().headRowNumber(1).doRead();
        } catch (IOException e) {
            log.error(e.getMessage());
            return "error";
        }

        return "ok";
    }

    /**
     * 导出Excel
     * 1. 设置响应头
     * 2. 获取原始数据，转换为对应DTO
     * 3. 将DTO数据写入响应输出流
     */
    @GetMapping("/download")
    public void excelDownload(HttpServletResponse response) {
        final String filename = "default.xlsx";
        // response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" +
                URLEncoder.encode(filename, StandardCharsets.UTF_8));

        List<User> users = new ArrayList<>();
        users.add(new User(UUID.randomUUID().toString().replaceAll("-", ""), "AAA",
                "0001", 12, LocalDateTime.now(), ""));
        users.add(new User(UUID.randomUUID().toString().replaceAll("-", ""), "BBB",
                "0002", 13, LocalDateTime.now(), ""));

        /* 使用BeanUtils完成DTO的生成 */
        // List<UserExcelExportDTO> data = users.stream().map(x -> {
        //     UserExcelExportDTO t = new UserExcelExportDTO();
        //     BeanUtils.copyProperties(x, t);
        //     return t;
        // }).collect(Collectors.toList());

        /* 引入mapstruct完成DTO的生成 */
        List<UserExcelExportDTO> data = UserExcelExportDTO.UserExcelExportDTOStruct.copy.from(users);

        try {
            EasyExcel.write(response.getOutputStream(), UserExcelExportDTO.class).sheet("模板").doWrite(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
