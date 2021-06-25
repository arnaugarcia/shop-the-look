package com.klai.stl.service.mapper;

import com.klai.stl.domain.*;
import com.klai.stl.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class, CoordinateMapper.class })
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "company", source = "company", qualifiedByName = "cif")
    @Mapping(target = "coordinate", source = "coordinate", qualifiedByName = "id")
    ProductDTO toDto(Product s);
}
