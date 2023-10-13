package org.example.service;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.api.IGoodsService;

import java.util.List;

@DubboService
public class GoodsServiceImpl implements IGoodsService {
    @Override
    public List<String> queryGoods() {
        List<String> goodsList = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            goodsList.add("goods-".concat(RandomStringUtils.randomAlphabetic(7).toUpperCase()));
        }
        return goodsList;
    }
}
