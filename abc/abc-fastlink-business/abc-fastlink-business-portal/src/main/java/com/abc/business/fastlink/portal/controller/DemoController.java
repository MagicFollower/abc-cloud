package com.abc.business.fastlink.portal.controller;

import com.abc.business.fastlink.order.api.FastlinkOrderService;
import com.abc.system.apollo.autoconfig.SystemConfigValues;
import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.response.BaseResponse;
import com.abc.system.common.response.ResponseData;
import com.abc.system.common.response.ResponseProcessor;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试用Controller
 *
 * @Description 测试用Controller
 * @Author [author_name]
 * @Date 2077/8/4 19:11
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {

    @DubboReference(check = false, timeout = -1)
    private FastlinkOrderService fastlinkOrderService;

    @GetMapping("/01")
    public ResponseData<String> demo01() {
        log.info(">>>>>>>>|demo01|start|<<<<<<<<");
        final ResponseProcessor<String> rp = new ResponseProcessor<>();
        BaseResponse<String> query = fastlinkOrderService.query();
        if(!SystemRetCodeConstants.OP_SUCCESS.getCode().equals(query.getCode())) {
            rp.setErrorMsg(SystemRetCodeConstants.SYSTEM_BUSINESS);
        }
        log.info(">>>>>>>>|demo01|end|<<<<<<<<");
        return rp.setData(query.getResult());

    }
}
