package com.abc.business.web.converter;

import com.abc.business.dto.ProductTypeDTO;
import com.abc.business.web.dal.entity.AbcProductType;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * ProductTypeConverter
 *
 * @Description ProductTypeConverter
 * @Author -
 * @Date 2077/8/21 8:22
 * @Version 1.0
 */
@Mapper(componentModel = "spring")
public interface ProductTypeConverter {

    @Mappings({})
    ProductTypeDTO productEntity2DTO(AbcProductType entity);

    List<ProductTypeDTO> productEntity2DTO(List<AbcProductType> entity);
}
