package com.abc.business.fastlink.portal.controller.order;

import com.abc.business.fastlink.order.api.IDemoOrderService;
import com.abc.business.fastlink.order.dto.OrderDTO;
import com.abc.business.fastlink.portal.base.BaseUrl;
import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.page.PageResponse;
import com.abc.system.common.response.BaseResponse;
import com.abc.system.common.response.ResponseData;
import com.abc.system.common.response.ResponseProcessor;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * OrderController
 *
 * @Description OrderController 测试使用
 * @Author [author_name]
 * @Date 2077/5/14 21:39
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping(BaseUrl.BASE_URL)
@RequiredArgsConstructor
public class OrderController {

    @DubboReference(check = false, timeout = 5000)
    private IDemoOrderService iDemoOrderService;


    @GetMapping("/demo01")
    public ResponseData<PageResponse> demo01() {

        final ResponseProcessor<PageResponse> rp = new ResponseProcessor<>();
        // RPC异常_服务未启动/超时：org.apache.dubbo.rpc.RpcException: No provider available from registry
        //   -> RpcException➡️RuntimeException
        final BaseResponse<List<OrderDTO>> baseResponse;
        final String SERVICE_NAME = "iDemoOrderService";
        try {
            baseResponse = iDemoOrderService.demo01();
        } catch (Exception e) {
            if (e instanceof RpcException) {
                log.error(String.format("{%s}服务不可用: ", SERVICE_NAME) + e.getMessage());
                return rp.setErrorMsg(SystemRetCodeConstants.SYSTEM_ERROR);
            }
            log.error(String.format("{%s}服务出错: ", SERVICE_NAME) + e.getMessage());
            return rp.setErrorMsg(SystemRetCodeConstants.SYSTEM_ERROR);
        }

        // 多语言测试1
        //baseResponse.setCode(SystemRetCodeConstants.OP_FAILED.getCode());
        //baseResponse.setMsg(SystemRetCodeConstants.OP_FAILED.getMessage());

        if (!SystemRetCodeConstants.OP_SUCCESS.getCode().equals(baseResponse.getCode())) {
            return rp.setErrorMsg(baseResponse.getCode(), baseResponse.getMsg());
        }
        System.out.println("=========================CONTROLLER-BEGIN=========================");
        System.out.println("baseResponse.getResult() = " + baseResponse.getResult());
        System.out.println(JSONObject.toJSONString(baseResponse.getResult(), JSONWriter.Feature.PrettyFormat));
        System.out.println("=========================CONTROLLER-END=========================");

        final PageResponse pageResponse = new PageResponse();
        pageResponse.setTotal(baseResponse.getTotal());
        pageResponse.setData(baseResponse.getResult());
        // 多语言测试2
        //return rp.setData(pageResponse, "你好吗？本次访问成功了！");
        return rp.setData(pageResponse);
    }

    /**
     * 测试common-web组件的全局异常拦截器
     *
     * @return useless
     */
    @GetMapping("/demo02")
    public ResponseData<PageResponse> demo02() {
        throw new RuntimeException("发生了异常");
    }
}
