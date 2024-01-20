package org.example.controller.product;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.response.ResponseData;
import com.abc.system.common.response.ResponseProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dal.two.dal.persistence.TwoAbcProductMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

    // 仅测试使用，Mapper请在Service层注入
    private final TwoAbcProductMapper twoAbcProductMapper;

    @PostMapping("/add")
    public ResponseData<Void> add() {
        final ResponseProcessor<Void> rp = new ResponseProcessor<>();
        // twoAbcProductMapper.insert(new TwoAbcProduct());

        List<Map> test = twoAbcProductMapper.test();
        System.out.println("test = " + test);
        return rp.setData(null, SystemRetCodeConstants.OP_SUCCESS.getMessage());
    }

}
