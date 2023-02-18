package com.klai.stl.service.mapper;

import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.service.dto.SubscriptionPlanDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link SubscriptionPlan} and its DTO {@link SubscriptionPlanDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionPlanMapper extends EntityMapper<SubscriptionPlanDTO, SubscriptionPlan> {
    default SubscriptionPlan map(String value) {
        final SubscriptionPlan subscriptionPlan = new SubscriptionPlan();
        subscriptionPlan.setName(value);
        return subscriptionPlan;
    }
}
