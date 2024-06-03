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
     * <pre>
     * 手动控制事务提交与回滚的注意事项：
     * 1.PlatformTransactionManager是一个接口，继承了TransactionManager接口；
     * 2.PlatformTransactionManager提供了三个API：开启事务(获取事务_返回TransactionStatus)、提交事务commit、混滚事务rollback；
     * 3.提交与混滚顺序要和声明事务顺序相反。
     * </pre>
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
