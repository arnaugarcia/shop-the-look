package com.klai.stl.service.dto.requests;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateSubscriptionRequest {

    String companyReference;
    String subscriptionReference;
}
