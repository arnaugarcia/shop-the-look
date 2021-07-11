package com.klai.stl.service.mapper;

import com.klai.stl.domain.BillingAddress;
import com.klai.stl.service.dto.BillingAddressDTO;
import com.klai.stl.service.dto.requests.BillingAddressRequest;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link BillingAddress} and its DTO {@link BillingAddressRequest}.
 */
@Mapper(componentModel = "spring")
public interface BillingAddressMapper {
    BillingAddress toEntity(BillingAddressRequest billingAddressRequest);

    BillingAddressDTO toDto(BillingAddress billingAddress);
}
