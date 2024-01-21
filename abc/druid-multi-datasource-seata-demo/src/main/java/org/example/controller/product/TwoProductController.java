package org.example.controller.product;

import com.abc.system.common.response.ResponseData;
import com.abc.system.common.response.ResponseProcessor;
import com.baomidou.dynamic.datasource.annotation.DS;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dal.one.entity.OneAbcProduct;
import org.example.dal.one.persistence.OneAbcProductMapper;
import org.example.dal.two.entity.TwoAbcProduct;
import org.example.dal.two.persistence.TwoAbcProductMapper;
import org.example.service.IOneService;
import org.example.service.ITwoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品Controller
 *
 * @Description 商品Controller
 * @Author -
 * @Date 2023/8/20 22:57
 * @Version 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/two")
public class TwoProductController {

    private final IOneService oneService;
    private final ITwoService twoService;

    /**
     * 多数据源事务回滚测试
     *
     * @return ResponseData
     */
    @PostMapping("/add")
    public ResponseData<Void> add() {
        final ResponseProcessor<Void> rp = new ResponseProcessor<>();
        twoService.oneTwoAdd();
        return rp.setData(null);
    }

}
