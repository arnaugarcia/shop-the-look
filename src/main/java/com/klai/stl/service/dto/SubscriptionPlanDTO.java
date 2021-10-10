package com.klai.stl.service.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * A DTO for the {@link com.klai.stl.domain.SubscriptionPlan} entity.
 */
@Value
@Builder
@AllArgsConstructor
public class SubscriptionPlanDTO implements Serializable {

    String name;

    String description;

    String reference;

    boolean popular;

    double price;

    boolean current;

    SubscriptionBenefitsDTO benefits;
}
