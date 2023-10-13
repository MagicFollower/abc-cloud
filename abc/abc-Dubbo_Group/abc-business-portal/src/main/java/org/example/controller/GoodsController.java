package org.example.controller;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.api.IGoodsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/abc/base/goods")
public class GoodsController {

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private IGoodsService goodsService;

    @PostMapping("/query")
    public String queryGoods() {
        int updated = 0;
        for (int i = 0; i < 100; i++) {
            List<String> goodsList = goodsService.queryGoods();
            if(goodsList.get(0).contains("goods-updated-")) {
                updated++;
            }
        }
        return String.format("updated:%d", updated);
    }
}
