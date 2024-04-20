package com.abc.business.fastlink.order.dto;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.business.BizException;
import com.abc.system.common.exception.business.ValidateException;
import com.abc.system.common.request.AbstractRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OrderRequest
 *
 * @Description OrderRequest 详细介绍
 * @Author [author_name]
 * @Date 2077/5/16 21:25
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderRequest<T> extends AbstractRequest<T> {

    @Override
    public void requestCheck() throws BizException {
        if (item == null) {
            throw new ValidateException(SystemRetCodeConstants.PARAMETER_EXISTS_ERROR);
        }
    }
}
