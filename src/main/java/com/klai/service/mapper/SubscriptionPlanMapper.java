package com.klai.service.mapper;

import com.klai.domain.*;
import com.klai.service.dto.SubscriptionPlanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionPlan} and its DTO {@link SubscriptionPlanDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubscriptionPlanMapper extends EntityMapper<SubscriptionPlanDTO, SubscriptionPlan> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SubscriptionPlanDTO toDtoName(SubscriptionPlan subscriptionPlan);
}
