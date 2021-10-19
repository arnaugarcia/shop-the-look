package com.klai.stl.service.impl;

import static java.util.Objects.isNull;

import com.klai.stl.config.ApplicationProperties;
import com.klai.stl.service.SubscriptionPlanService;
import com.klai.stl.service.WebhookEventService;
import com.klai.stl.service.dto.requests.UpdateSubscriptionRequest;
import com.klai.stl.service.dto.webhook.StripeEvent;
import com.klai.stl.service.exception.WebhookSecretError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StripeWebhookEventServiceImpl implements WebhookEventService<StripeEvent> {

    private final Logger log = LoggerFactory.getLogger(StripeWebhookEventServiceImpl.class);

    private final SubscriptionPlanService subscriptionPlanService;

    private final String WEBHOOK_SECRET;

    private static final String STRIPE_EVENT = "checkout.session.completed";

    public StripeWebhookEventServiceImpl(SubscriptionPlanService subscriptionPlanService, ApplicationProperties applicationProperties) {
        this.subscriptionPlanService = subscriptionPlanService;
        this.WEBHOOK_SECRET = applicationProperties.getStripe().getWebhookSecret();
    }

    @Override
    public void processEvent(StripeEvent event, String endpointSecret) {
        validateSecret(endpointSecret);
        if (!event.getType().equals(STRIPE_EVENT)) {
            log.info("Excluding stripe webhook event type {}", event.getType());
            return;
        }
        log.info("Processing stripe webhook event ({})", event);
        StripeEvent.Metadata metadata = event.getData().getObject().getMetadata();
        final UpdateSubscriptionRequest updateSubscriptionRequest = UpdateSubscriptionRequest
            .builder()
            .subscriptionReference(metadata.getSubscriptionReference())
            .companyReference(metadata.getCompanyReference())
            .build();
        subscriptionPlanService.updateSubscriptionPlanForCompany(updateSubscriptionRequest);
        log.info("Finished processing event ({}) of stripe webhook for company ({})", event.getId(), metadata.getCompanyReference());
    }

    private void validateSecret(String webhookSecret) {
        if (isNull(webhookSecret) || !webhookSecret.equals(WEBHOOK_SECRET)) {
            throw new WebhookSecretError();
        }
    }
}
