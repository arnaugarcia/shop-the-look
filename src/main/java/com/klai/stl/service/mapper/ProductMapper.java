package com.klai.stl.service.mapper;

import com.klai.stl.domain.Product;
import com.klai.stl.service.dto.ProductDTO;
import com.klai.stl.service.dto.feed.FeedProduct;
import com.klai.stl.service.dto.feed.Item;
import com.klai.stl.service.dto.requests.NewProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(source = "company.reference", target = "companyReference")
    ProductDTO toDto(Product s);

    Product toEntity(NewProductRequest product);

    @Mapping(source = "id", target = "sku")
    FeedProduct toEntity(Item item);

    NewProductRequest toRequest(FeedProduct feedProduct);
}
