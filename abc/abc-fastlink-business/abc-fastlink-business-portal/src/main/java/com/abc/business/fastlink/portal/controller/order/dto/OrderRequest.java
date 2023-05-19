package com.abc.business.fastlink.portal.controller.order.dto;

import com.abc.system.common.exception.business.BizException;
import com.abc.system.common.page.PageInfo;
import com.abc.system.common.request.AbstractRequest;

/**
 * OrderRequest
 *
 * @Description OrderRequest 详细介绍
 * @Author Trivis
 * @Date 2023/5/16 21:25
 * @Version 1.0
 */
public class OrderRequest extends AbstractRequest {

    @Override
    public void requestCheck() throws BizException {
        if (pageInfo == null) {
            pageInfo = new PageInfo();
        }
    }
}
