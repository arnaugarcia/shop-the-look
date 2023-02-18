package com.klai.stl.service.webhook.stripe.dto;

import static com.klai.stl.config.Constants.STRIPE_CHECKOUT_PARAM_COMPANY_KEY;
import static com.klai.stl.config.Constants.STRIPE_CHECKOUT_PARAM_SUBSCRIPTION_KEY;
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
            .companyReference(rawMetadata.get(STRIPE_CHECKOUT_PARAM_COMPANY_KEY))
            .subscriptionReference(rawMetadata.get(STRIPE_CHECKOUT_PARAM_SUBSCRIPTION_KEY))
            .build();
    }
}
