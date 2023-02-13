package com.abc.business.images.service;

import com.abc.business.images.domain.entity.zip.ZipInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * @author trivis
 */
public interface ZipService extends IService<ZipInfo> {

    ZipInfo locateBy(@Param("filename") String filename);
}
