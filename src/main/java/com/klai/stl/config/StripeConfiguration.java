package com.klai.stl.config;

import com.stripe.Stripe;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfiguration {

    private final ApplicationProperties applicationProperties;

    public StripeConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        Stripe.apiKey = applicationProperties.getStripe().getApiKey();
    }
}
