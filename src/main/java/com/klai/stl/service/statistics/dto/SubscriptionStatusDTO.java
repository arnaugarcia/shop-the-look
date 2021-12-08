package com.klai.stl.service.statistics.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class SubscriptionStatusDTO {

    String name;
    String description;
    SubscriptionType type;
    double price;
    boolean custom;
}
