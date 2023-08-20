package com.abc.business.dto;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.business.BizException;
import com.abc.system.common.exception.business.ValidateException;
import com.abc.system.common.request.AbstractRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品Service请求实体
 *
 * @Description 商品Service请求实体
 * @Author -
 * @Date 2023/8/20 23:07
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductRequest<T> extends AbstractRequest<T> {

    @Override
    protected void requestCheck() throws BizException {
        if (item == null) {
            throw new ValidateException(SystemRetCodeConstants.PARAMETER_EXISTS_ERROR);
        }
    }
}
