package com.abc.business.fastlink.portal.controller.order.constant;

import com.abc.business.fastlink.portal.base.BaseUrl;

/**
 * Url
 *
 * @Description Url 详细介绍
 * @Author [author_name]
 * @Date 2077/5/14 21:37
 * @Version 1.0
 */
public class Url {
    private final static String ORDER_BASE = BaseUrl.BASE_URL_BASE + "/order";
    public final static String ORDER_BASE_QUERY = ORDER_BASE + "/query";
    public final static String ORDER_BASE_TEST_DISTRIBUTED_LOCK = ORDER_BASE + "/test_distributed_lock";

    private Url() {
    }

}
