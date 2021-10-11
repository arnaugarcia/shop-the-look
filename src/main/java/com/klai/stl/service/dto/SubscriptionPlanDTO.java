package com.klai.stl.service.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link com.klai.stl.domain.SubscriptionPlan} entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPlanDTO implements Serializable {

    private String name;

    private String description;

    private String reference;

    private boolean popular;

    private double price;

    private boolean current;

    private SubscriptionBenefitsDTO benefits;
}
