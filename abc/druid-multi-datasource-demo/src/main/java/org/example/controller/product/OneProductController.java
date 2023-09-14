package org.example.controller.product;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.response.ResponseData;
import com.abc.system.common.response.ResponseProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dal.one.entity.OneAbcProduct;
import org.example.dal.one.persistence.OneAbcProductMapper;
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
@RequestMapping("/one")
public class OneProductController {

    // 仅测试使用，Mapper请在Service层注入
    private final OneAbcProductMapper oneAbcProductMapper;

    @PostMapping("/add")
    public ResponseData<Void> add() {
        final ResponseProcessor<Void> rp = new ResponseProcessor<>();
        oneAbcProductMapper.insert(new OneAbcProduct());
        return rp.setData(null, SystemRetCodeConstants.OP_SUCCESS.getMessage());
    }

}
