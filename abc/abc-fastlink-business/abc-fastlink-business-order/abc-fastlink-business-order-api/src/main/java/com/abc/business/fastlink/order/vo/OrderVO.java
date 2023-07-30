package com.abc.business.fastlink.order.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * OrderVO
 *
 * @Description OrderVO
 * @Author Rake
 * @Date 2023/7/30 11:37
 * @Version 1.0
 */
@Data
public class OrderVO implements Serializable {
    /* add */
    private String id;
    private String name;
    private Integer age;
    private Byte sex;
    private String memo;

    /* remove */
    /* 1.删除统一使用ids参数，逗号分隔多id */
    private String ids;

    /* update */
    /* 1.更新条件为：name、age、sex、memo，但是四个条件均出现在上方，此处省略update属性块 */

    /* query */
    /* 1.查询条件为：name、age、createTime, 前两个出现在上面属性中，此处仅配置createTime */
    private LocalDateTime createTime;

}
