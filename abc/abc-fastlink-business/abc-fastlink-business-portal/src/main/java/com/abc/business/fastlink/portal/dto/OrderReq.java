package com.abc.business.fastlink.portal.dto;

import com.abc.system.common.request.AbstractReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Controller层请求实体
 *
 * @Description OrderReq
 * @Author Rake
 * @Date 2023/7/30 15:56
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderReq<T> extends AbstractReq<T> implements Serializable {

}
