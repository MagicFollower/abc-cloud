## abc-common-easyexcel使用示例

`创建时间：2023年08月14日 19:39:20`

---

#### 一、上传

![导入Excel示例](https://github.com/MagicFollower/abc-cloud/blob/main/abc/abc-boot-starter-dependencies/abc-boot-starter-parent/abc-common/abc-common-easyexcel/doc/img/Snipaste_2023-08-14_20-41-52.png?raw=true)

```java
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.example.springbootdateformat.dto.UserExcelImportDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * EasyExcelController
 *
 * @Description EasyExcelController
 * @Author -
 * @Date 2077/8/14 19:47
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/easyexcel")
@RequiredArgsConstructor
public class EasyExcelController {

    /**
     * excel上传解析
     * <pre>
     * 1.sheet(0)中使用的是索引（第0页），headRowNumber(2)使用的是数量（2行header，从第三行读取）
     * </pre>
     * @param file MultipartFile
     * @return "ok"/"error"
     */
    @PostMapping("/upload")
    public String excelUpload(@RequestPart("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            // 默认BATCH_COUNT=100
            PageReadListener.BATCH_COUNT = 5;
            EasyExcel.read(inputStream, UserExcelImportDTO.class,
                    new PageReadListener<UserExcelImportDTO>(dataList -> {
                        for (UserExcelImportDTO demoData : dataList) {
                            log.info("读取到一条数据{}",
                                    JSONObject.toJSONString(demoData, JSONWriter.Feature.PrettyFormat));
                        }
                    }))
                    .sheet(0).headRowNumber(2)
                    .doRead();
        } catch (IOException e) {
            log.error(e.getMessage());
            return "error";
        }

        return "ok";
    }
}
```

```java
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * UserExcelImportDTO
 * <pre>
 * 1.时间类型，如果使用Excel中的时间格式(yyyy-mm-dd hh:mm:ss)，则均可以自动转换为Date，无关@DateTimeFormat
 * </pre>
 *
 * @Description UserExcelImportDTO
 * @Author -
 * @Date 2077/8/14 19:53
 * @Version 1.0
 */
@Data
public class UserExcelImportDTO implements Serializable {
    @ExcelProperty(index = 0)
    private String name;
    @ExcelProperty(index = 1)
    private Integer age;

    @ExcelProperty(index = 2)
    private Date createTime;

    @ExcelProperty(index = 3)
    @DateTimeFormat("yyyy年MM月dd日 HH:mm:ss")
    private Date updateTime;
}
```



#### 二、下载（`单Sheet`+`多Sheet`）

 ![导出Excel示例](https://github.com/MagicFollower/abc-cloud/blob/main/abc/abc-boot-starter-dependencies/abc-boot-starter-parent/abc-common/abc-common-easyexcel/doc/img/Snipaste_2023-08-14_20-57-29.png?raw=true)

```java
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.component.model.UserExcelExportDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * -
 *
 * @Description -
 * @Author -
 * @Date 2077/9/24 21:53
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/easyexcel")
@RequiredArgsConstructor
public class EasyExcelController {

    /**
     * 导出Excel（单Sheet）
     * 1. 设置响应头
     * 2. 获取原始数据，转换为对应DTO
     * 3. 将DTO数据写入响应输出流
     */
    @GetMapping("/singleSheet/download")
    public void excelSingleSheetDownload(HttpServletResponse response) {
        final String filename = "default.xlsx";
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" +
                URLEncoder.encode(filename, StandardCharsets.UTF_8));

        List<UserExcelExportDTO> userExcelExportDTOList = Lists.newArrayList();
        for (int i = 0; i < 23; i++) {
            UserExcelExportDTO userExcelExportDTO = new UserExcelExportDTO();
            userExcelExportDTO.setName("NAME-".concat(RandomStringUtils.randomAlphanumeric(5)));
            userExcelExportDTO.setAge(ThreadLocalRandom.current().nextInt(100));
            userExcelExportDTO.setCreateTime(new Date());
            userExcelExportDTO.setUpdateTime(new Date());
            userExcelExportDTO.setIsDeleted(ThreadLocalRandom.current().nextBoolean());
            userExcelExportDTOList.add(userExcelExportDTO);
        }

        try {
            EasyExcel.write(response.getOutputStream(), UserExcelExportDTO.class)
                    .sheet("用户数据")
                    .doWrite(userExcelExportDTOList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 导出Excel（多Sheet）
     * 1. 设置响应头
     * 2. 获取原始数据，转换为对应DTO
     * 3. 将DTO数据写入响应输出流
     */
    @GetMapping("/multiSheet/download")
    public void excelMultiSheetDownload(HttpServletResponse response) {
        final String filename = "default.xlsx";
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" +
                URLEncoder.encode(filename, StandardCharsets.UTF_8));

        List<UserExcelExportDTO> userExcelExportDTOList = Lists.newArrayList();
        for (int i = 0; i < 23; i++) {
            UserExcelExportDTO userExcelExportDTO = new UserExcelExportDTO();
            userExcelExportDTO.setName("NAME-".concat(RandomStringUtils.randomAlphanumeric(5)));
            userExcelExportDTO.setAge(ThreadLocalRandom.current().nextInt(100));
            userExcelExportDTO.setCreateTime(new Date());
            userExcelExportDTO.setUpdateTime(new Date());
            userExcelExportDTO.setIsDeleted(ThreadLocalRandom.current().nextBoolean());
            userExcelExportDTOList.add(userExcelExportDTO);
        }

        try (ExcelWriter writer = EasyExcel.write(response.getOutputStream(), UserExcelExportDTO.class).build()) {
            // Sheet1
            WriteSheet sheet = EasyExcel.writerSheet(0, "基础信息").build();
            writer.write(userExcelExportDTOList, sheet);
            // Sheet2
            WriteSheet sheet2 = EasyExcel.writerSheet(1, "详细信息").head(UserExcelExportDTO.class).build();
            writer.write(userExcelExportDTOList, sheet2);
        } catch (Exception e) {
            log.error(">>>>>>>>|excelMultiSheetDownload|error|Exception:{}|<<<<<<<<", e.getMessage(), e);
        }
    }
}
```

```java
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * UserExcelExportDTO
 * <pre>
 * 1.导出时，指定index和value，导入时指定index即可；
 * 2.导出的时间在Excel中均会展示为标准的时间格式。
 * </pre>
 *
 * @Description UserExcelImportDTO
 * @Author -
 * @Date 2077/8/14 19:53
 * @Version 1.0
 */
@Data
public class UserExcelExportDTO implements Serializable {

    @ExcelProperty(index = 0, value = "姓名")
    private String name;
    @ExcelProperty(index = 1, value = "年龄")
    private Integer age;


    @ExcelProperty(index = 2, value = "创建时间")
    private Date createTime;

    @ExcelProperty(index = 3, value = "更新时间")
    @DateTimeFormat("yyyy年MM月dd日 HH:mm:ss")
    private Date updateTime;

    @ExcelProperty(index = 4, value = "是否被删除")
    private Boolean isDeleted;
}
```





<br/><br/><br/><br/>

***<u>END.</u>***
