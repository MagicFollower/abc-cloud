package com.abc.business.images.domain.entity.zip;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author trivis
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("zipinfo")
public class ZipInfo {
    private String id;
    private String name;
    private long size;
    @TableField("md5")
    private String md5Digest;
    private String compressType;

    private String filesInfo;
}
