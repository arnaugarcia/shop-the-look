package com.klai.stl.service.dto;

import com.klai.stl.domain.enumeration.SubscriptionCategory;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the {@link com.klai.stl.domain.SubscriptionPlan} entity.
 */
@Data
public class SubscriptionPlanDTO implements Serializable {

    @NotNull
    private String name;

    private String description;

    @NotNull
    private SubscriptionCategory category;

    @NotNull
    private Integer maxProducts;

    @NotNull
    private Integer maxSpaces;

    @NotNull
    private Integer maxRequests;
}
