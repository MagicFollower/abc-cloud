package com.abc.business.web.converter;

import com.abc.business.dto.ProductDTO;
import com.abc.business.web.dal.entity.AbcProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * ProductConverter
 *
 * @Description ProductConverter
 * @Author -
 * @Date 2023/8/21 8:22
 * @Version 1.0
 */
@Mapper(componentModel = "spring")
public interface ProductConverter {

    @Mappings({})
    ProductDTO productEntity2DTO(AbcProduct entity);
    List<ProductDTO> productEntity2DTO(List<AbcProduct> entity);
}
