package com.klai.stl.service.mapper;

import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.service.dto.SubscriptionPlanDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

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

    default String toName(SubscriptionPlan subscriptionPlan) {
        return subscriptionPlan.getName();
    }

    default SubscriptionPlan map(String value) {
        final SubscriptionPlan subscriptionPlan = new SubscriptionPlan();
        subscriptionPlan.setName(value);
        return subscriptionPlan;
    }
}
