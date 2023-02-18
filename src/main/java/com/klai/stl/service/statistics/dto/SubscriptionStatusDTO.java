package com.klai.stl.service.statistics.dto;

import static java.util.Objects.isNull;

import com.klai.stl.domain.Company;
import com.klai.stl.domain.SubscriptionPlan;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SubscriptionStatusDTO {

    String name;
    String description;
    double price;
    boolean custom;
    String companyName;

    public static SubscriptionStatusDTO from(SubscriptionPlan currentSubscription, Company company) {
        return SubscriptionStatusDTO
            .builder()
            .name(currentSubscription.getName())
            .description(currentSubscription.getDescription())
            .price(currentSubscription.getPrice())
            .custom(currentSubscription.getRequests() == 0) // Add custom param to the Database
            .companyName(isNull(company.getCommercialName()) ? company.getName() : company.getCommercialName())
            .build();
    }
}
