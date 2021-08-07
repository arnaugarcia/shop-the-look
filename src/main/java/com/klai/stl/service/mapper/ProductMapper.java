package com.klai.stl.service.mapper;

import com.klai.stl.domain.Product;
import com.klai.stl.service.dto.ProductDTO;
import com.klai.stl.service.dto.requests.NewProductRequest;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    ProductDTO toDto(Product s);

    Product toEntity(NewProductRequest product);
}
