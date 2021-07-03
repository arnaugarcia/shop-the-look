package com.klai.stl.service.mapper;

import com.klai.stl.domain.BillingAddress;
import com.klai.stl.service.dto.BillingAddressDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link BillingAddress} and its DTO {@link BillingAddressDTO}.
 */
@Mapper(componentModel = "spring")
public interface BillingAddressMapper extends EntityMapper<BillingAddressDTO, BillingAddress> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BillingAddressDTO toDtoId(BillingAddress billingAddress);
}
