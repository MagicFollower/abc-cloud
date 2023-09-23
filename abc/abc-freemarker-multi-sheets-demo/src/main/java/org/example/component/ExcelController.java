package org.example.component;

import com.abc.system.common.filegenerator.service.GenerateFileService;
import com.abc.system.common.filegenerator.vo.ExportFileRequest;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.component.model.ExcelDataModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@RequiredArgsConstructor
public class ExcelController {

    private final GenerateFileService generateFileService;

    @GetMapping("/02")
    public void demo02(HttpServletResponse response) {
        ExportFileRequest exportFileRequest = new ExportFileRequest();
        final String TEMPLATE_NAME = "excel.ftl";
        HashMap<String, Object> dataSet = Maps.newHashMap();

        List<ExcelDataModel> dataModelListAll = Lists.newArrayList();
        List<ExcelDataModel> dataModelListSuccess = Lists.newArrayList();
        List<ExcelDataModel> dataModelListFail = Lists.newArrayList();
        for (int i = 0; i < 15; i++) {
            ExcelDataModel data = new ExcelDataModel();
            data.setName(RandomStringUtils.randomAlphanumeric(7));
            data.setAge(String.valueOf(ThreadLocalRandom.current().nextInt(200)));
            data.setSex(ThreadLocalRandom.current().nextBoolean() ? "男" : "女");
            data.setMemo("测试备注: " + LocalDateTime.now());
            dataModelListAll.add(data);
        }
        for (int i = 0; i < 10; i++) {
            ExcelDataModel data = new ExcelDataModel();
            data.setName(RandomStringUtils.randomAlphanumeric(7));
            data.setAge(String.valueOf(ThreadLocalRandom.current().nextInt(200)));
            data.setSex(ThreadLocalRandom.current().nextBoolean() ? "男" : "女");
            data.setMemo("测试备注: " + LocalDateTime.now());
            dataModelListSuccess.add(data);
        }
        for (int i = 0; i < 5; i++) {
            ExcelDataModel data = new ExcelDataModel();
            data.setName(RandomStringUtils.randomAlphanumeric(7));
            data.setAge(String.valueOf(ThreadLocalRandom.current().nextInt(200)));
            data.setSex(ThreadLocalRandom.current().nextBoolean() ? "男" : "女");
            data.setMemo("测试备注: " + LocalDateTime.now());
            dataModelListFail.add(data);
        }

        dataSet.put("userDataSetAll", dataModelListAll);
        dataSet.put("userDataSetSuccess", dataModelListSuccess);
        dataSet.put("userDataSetFail", dataModelListFail);
        exportFileRequest.setTemplateFile(TEMPLATE_NAME);
        exportFileRequest.setData(dataSet);
        exportFileRequest.setExportFileName("测试多页签导出.xls");
        generateFileService.export(exportFileRequest, response);
    }
}
