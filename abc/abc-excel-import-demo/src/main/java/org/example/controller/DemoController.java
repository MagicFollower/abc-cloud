package org.example.controller;

import com.abc.system.common.response.ResponseData;
import com.abc.system.excel.service.ExcelFileService;
import com.abc.system.excel.vo.ExcelResponse;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;

/**
 * DemoController
 *
 * @Description DemoController
 * @Author -
 * @Date 2077/9/26 22:48
 * @Version 1.0
 */
@RestController
@RequiredArgsConstructor
public class DemoController {

    private final ExcelFileService excelFileService;

    @PostMapping("/import1Test")
    public String import1Test(@RequestParam String type, HttpServletRequest request) {
        System.out.println(request.getRequestURI());
        System.out.println(request.getRequestURL());
        System.out.println(type);

        ResponseData<ExcelResponse> excelRes = excelFileService.parseExcel(request);
        if(excelRes.getSuccess()) {
            ExcelResponse result = excelRes.getResult();
            Collection<String> displayData = result.getDisplayData();
            Collection<String> displayTitle = result.getDisplayTitle();
            Collection<JSONObject> realData = result.getRealData();

            System.out.println(JSONObject.toJSONString(displayData, JSONWriter.Feature.PrettyFormat));
            System.out.println(JSONObject.toJSONString(displayTitle, JSONWriter.Feature.PrettyFormat));
            System.out.println(JSONObject.toJSONString(realData, JSONWriter.Feature.PrettyFormat));

            /* 使用GSON序列化null为指定字符串 */
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .serializeNulls()
                    .registerTypeAdapter(String.class, new StringTypeNullToEmptyStringAdapter()).create();
            String json = gson.toJson(realData);
            System.out.println("json = " + json);
            System.out.println("DemoController.import1Test");
        }

        return "200";
    }


    /**
     * GSON字符串类型自定义序列化器
     */
    static class StringTypeNullToEmptyStringAdapter extends TypeAdapter<String> {

        @Override
        public void write(JsonWriter jsonWriter, String s) throws IOException {
            jsonWriter.value(s == null ? "" : s);
        }

        @Override
        public String read(JsonReader jsonReader) throws IOException {
            return jsonReader.nextString();
        }
    }
}
