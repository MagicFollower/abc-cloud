package com.abc.business.fastlink.portal.controller.order;

import com.abc.business.fastlink.order.api.FastlinkOrderService;
import com.abc.business.fastlink.portal.base.BaseUrl;
import com.abc.business.fastlink.portal.controller.order.constant.Url;
import com.abc.business.fastlink.portal.controller.order.dto.OrderRequest;
import com.abc.system.common.cache.SystemConfigValues;
import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.business.BizException;
import com.abc.system.common.log.annotation.LogAnchor;
import com.abc.system.common.page.PageInfo;
import com.abc.system.common.page.PageResponse;
import com.abc.system.common.redis.helper.RedissonHelper;
import com.abc.system.common.redis.service.RedisService;
import com.abc.system.common.response.BaseResponse;
import com.abc.system.common.response.ResponseData;
import com.abc.system.common.response.ResponseProcessor;
import com.abc.system.lock.annotation.DistributedLock;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * OrderController
 *
 * @Description OrderController 测试使用
 * @Author Trivis
 * @Date 2023/5/14 21:39
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping(BaseUrl.BASE_URL)
@RequiredArgsConstructor
public class OrderController {

    private final String CACHE_DATA_KEY = "OrderController_Order";
    private final String CACHE_DATA_TOTAL_KEY = "OrderController_Order_Total";
    private final RedissonClient redissonClient;
    private final RedisService redisService;
    //    private final ExcelFileService excelFileService;
    @DubboReference
    private FastlinkOrderService fastlinkOrderService;


//    @PostMapping("/testExcelImport")
//    public String testExcelImport(HttpServletRequest request) {
//        ResponseData<ExcelResponse> responseData = excelFileService.dealWith(request);
//        ExcelResponse result = responseData.getResult();
//        System.out.println(JSONObject.toJSONString(result, JSONWriter.Feature.PrettyFormat));
//        return "200";
//    }


    @LogAnchor
    @PostMapping("/testSystemValues")
    public void testSystemValues() {
        String s = SystemConfigValues.get("not_need_login.urls");
        List<String> notNeedLoginUrls = Arrays.stream(s.split(",")).collect(Collectors.toList());
        System.out.println("notNeedLoginUrls = " + notNeedLoginUrls);
    }

    @LogAnchor
    @PostMapping("/testLogAnchor0")
    public void testLogAnchor0() {
        System.out.println("OrderController.testLogAnchor0");
        if (ThreadLocalRandom.current().nextBoolean()) {
            throw new BizException(SystemRetCodeConstants.SYSTEM_ERROR);
        }
    }

    @LogAnchor
    @PostMapping("/testLogAnchor1")
    public void testLogAnchor1(@RequestBody User user) {
        System.out.println("OrderController.testLogAnchor1");
        if (ThreadLocalRandom.current().nextBoolean()) {
            throw new BizException(SystemRetCodeConstants.SYSTEM_ERROR);
        }
    }

    @LogAnchor
    @PostMapping("/testLogAnchor2")
    public void testLogAnchor2(HttpServletRequest request, HttpServletResponse response, @RequestBody User user) {
        System.out.println("OrderController.testLogAnchor2");
        if (ThreadLocalRandom.current().nextBoolean()) {
            throw new BizException(SystemRetCodeConstants.SYSTEM_ERROR);
        }
    }

    @DistributedLock
    @PostMapping(Url.ORDER_BASE_TEST_DISTRIBUTED_LOCK)
    public ResponseData<String> testDistributedLock() {
        System.out.println("OrderController.testDistributedLock");

        RedissonHelper.addAndGet("aaaaaaaaaaaaaa", 10);
        RedissonHelper.addAndGet("aaaaaaaaaaaaaa", -6);

        // 模拟获取响应数据
        BaseResponse<String> stringBaseResponse = new BaseResponse<>();
        stringBaseResponse.setTotal(1L);
        stringBaseResponse.setResult("OrderController.testDistributedLock");

        return new ResponseProcessor<String>().setData(stringBaseResponse.getResult(), "操作成功");
    }


    @PostMapping(Url.ORDER_BASE_QUERY)
    public ResponseData<PageResponse<?>> queryOrder(@RequestBody OrderRequest orderRequest) {
        orderRequest.requestCheck();
        long total;
        PageInfo pageInfo = orderRequest.getPageInfo();
        // 从缓存中获取首页数据
        PageResponse<?> pageResponseCached = queryFromCache(pageInfo);
        if (pageResponseCached != null) {
            return new ResponseProcessor<PageResponse<?>>().setData(pageResponseCached, "操作成功");
        }

        // 模拟从DB中获取数据(pageSize), 模拟数据总数120条
        JSONArray jsonArray = new JSONArray();
        if (pageInfo.getPageSize() >= 120) pageInfo.setPageSize(120);
        for (int i = 0; i < pageInfo.getPageSize(); i++) {
            JSONObject item = JSONObject.of("id", UUID.randomUUID().toString().replace("-", ""));
            int age = ThreadLocalRandom.current().nextInt(0, 100);
            item.put("name", "xiaoming" + age);
            item.put("age", age);
            jsonArray.add(item);
        }
        total = 120L;

        // 将首页数据写入缓存
        saveToCacheRedisson(pageInfo, jsonArray, total);
        saveToCacheDataDataRedis(pageInfo, jsonArray, total);

        PageResponse<JSONArray> stringPageResponse = new PageResponse<>();
        stringPageResponse.setData(jsonArray);
        stringPageResponse.setTotal(total);
        log.info(">>>>>>>>>>> queryOrder from DB|dataCount:{},total:{}|success <<<<<<<<<<<",
                jsonArray.size(), total);
        return new ResponseProcessor<PageResponse<?>>().setData(stringPageResponse, "操作成功");
    }


    private PageResponse<?> queryFromCache(PageInfo pageInfo) {
        if (pageInfo.getPageNum() == 1) {
            try {
                String cachedTotalStr = RedissonHelper.get(CACHE_DATA_TOTAL_KEY);
                // cachedTotalStr == null时，表示缓存已被移除
                if (cachedTotalStr == null) return null;
                int cachedTotal = Integer.parseInt(cachedTotalStr);
                int cachedDataCount = RedissonHelper.getZSetCount(CACHE_DATA_KEY);
                // 缓存中包含所有数据，但是pageSize>=真实数据总记录数
                if (((pageInfo.getPageSize() >= cachedTotal) && (cachedTotal == cachedDataCount)) ||
                        (pageInfo.getPageSize() == cachedDataCount)) {
                    PageResponse<List<String>> pageInfoFromRedis = RedissonHelper
                            .getPageInfoFromRedis(pageInfo, CACHE_DATA_KEY);
                    JSONArray jsonArray = new JSONArray();
                    pageInfoFromRedis.getData().stream().map(JSONObject::parseObject).forEach(jsonArray::add);
                    PageResponse<JSONArray> stringPageResponse = new PageResponse<>();
                    stringPageResponse.setData(jsonArray);
                    stringPageResponse.setTotal(cachedTotal);
                    log.info(">>>>>>>>>>> queryOrder from cache|cachedDataCount:{},cachedTotal:{}|success <<<<<<<<<<<",
                            cachedDataCount, cachedTotal);
                    return stringPageResponse;
                }
            } catch (Exception e) {
                log.warn("缓存读取异常，请检查缓存服务是否正常运行！");
                return null;
            }
        }
        return null;
    }

    private void saveToCacheRedisson(PageInfo pageInfo, JSONArray data, long total) {
        // 缓存首页数据、数据总数
        try {
            if (pageInfo.getPageNum() == 1) {
                clearFirstPageCacheRedisson();
                Map<Object, Double> stringLongHashMap = new HashMap<>();
                data.forEach(x -> {
                    JSONObject jsonObject = (JSONObject) x;
                    stringLongHashMap.put(jsonObject.toJSONString(), jsonObject.getDouble("age"));
                });
                RedissonHelper.saveRecordToRedis(CACHE_DATA_KEY, stringLongHashMap, Duration.ofDays(1));
                RedissonHelper.set(CACHE_DATA_TOTAL_KEY, String.valueOf(total));
            }
        } catch (Exception e) {
            log.warn("缓存写入异常，请检查缓存服务是否正常运行！");
        }
    }

    private void saveToCacheDataDataRedis(PageInfo pageInfo, JSONArray data, long total) {
        // 缓存首页数据、数据总数
        try {
            if (pageInfo.getPageNum() == 1) {
                clearFirstPageCacheDataRedis();
                Map<String, String> stringLongHashMap = new HashMap<>();
                data.forEach(x -> {
                    JSONObject jsonObject = (JSONObject) x;
                    stringLongHashMap.put(jsonObject.getString("id"), jsonObject.toJSONString());
                });
                redisService.setMap("aaa", stringLongHashMap, Duration.ofDays(1));
                redisService.set("aaa_total", String.valueOf(total));
            }
        } catch (Exception e) {
            log.warn("缓存写入异常，请检查缓存服务是否正常运行！");
        }
    }

    private void clearFirstPageCacheRedisson() {
        final String CACHE_DATA_DISTRIBUTED_LOCK_TAG = "LOCK_OrderController_Order";
        RLock lock = redissonClient.getLock(CACHE_DATA_DISTRIBUTED_LOCK_TAG);
        lock.lock();
        RedissonHelper.delete(CACHE_DATA_KEY);
        RedissonHelper.delete(CACHE_DATA_TOTAL_KEY);
        lock.unlock();
    }

    private void clearFirstPageCacheDataRedis() {
        final String CACHE_DATA_DISTRIBUTED_LOCK_TAG = "LOCK_OrderController_Order";
        RLock lock = redissonClient.getLock(CACHE_DATA_DISTRIBUTED_LOCK_TAG);
        lock.lock();
        redisService.delete("aaa");
        redisService.delete("aaa_total");
        lock.unlock();
    }
}


@Data
@NoArgsConstructor
@AllArgsConstructor
class User {
    private String id;
    private String username;
    private int age;
}
