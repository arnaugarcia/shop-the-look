package com.klai.stl.service.mapper;

import com.klai.stl.domain.Product;
import com.klai.stl.service.dto.ProductDTO;
import com.klai.stl.service.dto.feed.FeedProduct;
import com.klai.stl.service.dto.feed.Item;
import com.klai.stl.service.dto.requests.NewProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

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

    @Mapping(source = "price", target = "price", qualifiedByName = "googleFeedProduct")
    @Mapping(source = "title", target = "name")
    NewProductRequest toRequest(FeedProduct feedProduct);

    @Named("googleFeedProduct")
    default Float googleFeedProduct(String price) {
        if (price == null) {
            return null;
        }
        final String[] split = price.split(" ");
        return Float.parseFloat(split[0]);
    }
}
