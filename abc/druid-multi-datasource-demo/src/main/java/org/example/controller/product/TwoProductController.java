package org.example.controller.product;

import com.abc.system.common.response.ResponseData;
import com.abc.system.common.response.ResponseProcessor;
import lombok.extern.slf4j.Slf4j;
import org.example.dal.one.entity.OneAbcProduct;
import org.example.dal.one.persistence.OneAbcProductMapper;
import org.example.dal.two.dal.entity.TwoAbcProduct;
import org.example.dal.two.dal.persistence.TwoAbcProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品Controller
 *
 * @Description 商品Controller
 * @Author -
 * @Date 2077/8/20 22:57
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/two")
public class TwoProductController {

    // 仅测试使用，Mapper请在Service层注入
    @Autowired
    private TwoAbcProductMapper twoAbcProductMapper;
    @Autowired
    private OneAbcProductMapper oneAbcProductMapper;

    private final PlatformTransactionManager txManagerOne;
    private final PlatformTransactionManager txManagerTwo;

    public TwoProductController(@Qualifier("oneTransactionManager") PlatformTransactionManager txManagerOne,
                                @Qualifier("twoTransactionManager") PlatformTransactionManager txManagerTwo) {
        this.txManagerOne = txManagerOne;
        this.txManagerTwo = txManagerTwo;
    }

    /**
     * 多数据源事务回滚测试
     *
     * @return ResponseData
     */
    @PostMapping("/add")
    public ResponseData<Void> add() {
        final ResponseProcessor<Void> rp = new ResponseProcessor<>();
        TransactionDefinition definitionOne = new DefaultTransactionDefinition();
        TransactionDefinition definitionTwo = new DefaultTransactionDefinition();

        TransactionStatus txStatusTwo = txManagerTwo.getTransaction(definitionTwo);
        TransactionStatus txStatusOne = txManagerOne.getTransaction(definitionOne);
        try {
            twoAbcProductMapper.insert(new TwoAbcProduct());
            oneAbcProductMapper.insert(new OneAbcProduct());
            int x = 1 / 0;
        } catch (Exception e) {
            txManagerOne.rollback(txStatusOne);
            txManagerTwo.rollback(txStatusTwo);
        }
        txManagerOne.commit(txStatusOne);
        txManagerTwo.commit(txStatusTwo);
        return null;
    }

}
