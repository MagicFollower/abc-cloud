package org.example.controller;

import com.abc.system.common.response.ResponseData;
import com.abc.system.excel.service.ExcelFileService;
import com.abc.system.excel.vo.ExcelResponse;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.example.dal.entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * -
 *
 * @Description -
 * @Author -
 * @Date 2077/9/26 22:48
 * @Version 1.0
 */
@RestController
@RequiredArgsConstructor
public class DemoController {

    private final ExcelFileService excelFileService;

    @PostMapping("/demo01")
    public String demo01(@RequestBody User user, HttpServletRequest request) {
        System.out.println(request.getRequestURI());
        System.out.println(request.getRequestURL());

        return JSONObject.toJSONString(user, JSONWriter.Feature.PrettyFormat);
    }

    @PostMapping("/demo02")
    public String demo02(@RequestParam String type, HttpServletRequest request) {
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
        }

        return "200";
    }

    @PostMapping("/demo03")
    public String demo03() {
        List<User> userList = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId("1");
            user.setName("name-"+(10-i));
            userList.add(user);
        }
        User user = new User();
        user.setId("1");
        user.setName("name-999");
        userList.add(user);

        User user1 = new User();
        user1.setId("1");
        user1.setName("name-1");
        userList.add(user1);

        Map<String, List<User>> idToUserListMap = userList.stream()
                .collect(Collectors.groupingBy(User::getId));
        System.out.println(JSONObject.toJSONString(idToUserListMap, JSONWriter.Feature.PrettyFormat));


        return "200";
    }
}
