package com.klai.stl.service.dto.requests;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CheckoutRequest {

    String companyReference;
    String subscriptionReference;
    String itemReference;
    String trialPeriodDays;
}
