package com.klai.stl.config;

import com.stripe.Stripe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfiguration {

    private final Logger log = LoggerFactory.getLogger(StripeConfiguration.class);

    public StripeConfiguration(ApplicationProperties applicationProperties) {
        final String apiKey = applicationProperties.getStripe().getApiKey();
        Stripe.apiKey = apiKey;
        log.debug("Configured Stripe API Key ({})", apiKey);
    }
}
