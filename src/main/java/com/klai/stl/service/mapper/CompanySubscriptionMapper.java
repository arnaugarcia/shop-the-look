package com.klai.stl.service.mapper;

import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.repository.projections.CompanySubscription;
import com.klai.stl.service.dto.SubscriptionPlanDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link SubscriptionPlan} and its DTO {@link SubscriptionPlanDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanySubscriptionMapper {
    @Mapping(source = "spaces", target = "benefits.spaces")
    @Mapping(source = "requests", target = "benefits.requests")
    @Mapping(source = "products", target = "benefits.products")
    SubscriptionPlanDTO toDto(CompanySubscription entity);

    List<SubscriptionPlanDTO> toDTO(List<CompanySubscription> companySubscriptions);
}
