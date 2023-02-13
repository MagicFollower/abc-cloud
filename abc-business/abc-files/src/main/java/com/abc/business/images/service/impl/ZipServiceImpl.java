package com.abc.business.images.service.impl;

import com.abc.business.images.domain.entity.zip.ZipInfo;
import com.abc.business.images.mapper.ZipInfoMapper;
import com.abc.business.images.service.ZipService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author trivis
 */
@Service
public class ZipServiceImpl extends ServiceImpl<ZipInfoMapper, ZipInfo> implements ZipService {
    @Override
    public ZipInfo locateBy(String filename) {
        return baseMapper._locateBy(filename);
    }
}
