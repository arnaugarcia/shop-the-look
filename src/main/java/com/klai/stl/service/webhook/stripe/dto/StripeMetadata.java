package com.klai.stl.service.webhook.stripe.dto;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
public class StripeMetadata {

    @NotNull
    String companyReference;

    @NotNull
    String subscriptionReference;

    @Builder
    public StripeMetadata(String companyReference, String subscriptionReference) {
        if (isBlank(companyReference) || isBlank(subscriptionReference)) {
            throw new IllegalArgumentException();
        }
        this.companyReference = companyReference;
        this.subscriptionReference = subscriptionReference;
    }

    public static StripeMetadata from(Map<String, String> rawMetadata) {
        return builder()
            .companyReference(rawMetadata.get("companyReference"))
            .subscriptionReference(rawMetadata.get("subscriptionReference"))
            .build();
    }
}
