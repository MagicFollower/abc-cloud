package com.abc.business.images.mapper;

import com.abc.business.images.domain.entity.zip.ZipInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author trivis
 */
public interface ZipInfoMapper extends BaseMapper<ZipInfo> {

    ZipInfo _locateBy(@Param("filename") String filename);
}
