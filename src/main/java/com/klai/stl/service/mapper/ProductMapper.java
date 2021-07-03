package com.klai.stl.service.mapper;

import com.klai.stl.domain.Product;
import com.klai.stl.service.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class, CoordinateMapper.class })
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "company", source = "company", qualifiedByName = "nif")
    @Mapping(target = "coordinate", source = "coordinate", qualifiedByName = "id")
    ProductDTO toDto(Product s);
}
