package com.klai.stl.service.impl;

import com.klai.stl.service.SubscriptionPlanService;
import com.klai.stl.service.WebhookEventService;
import com.klai.stl.service.dto.requests.UpdateSubscriptionRequest;
import com.klai.stl.service.dto.webhook.StripeEvent;
import com.klai.stl.web.rest.WebhookResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StripeWebhookEventServiceImpl implements WebhookEventService<StripeEvent> {

    private final Logger log = LoggerFactory.getLogger(WebhookResource.class);

    private final SubscriptionPlanService subscriptionPlanService;

    private final String endpointSecret = "whsec_1gZ7oPsQitY3vSOe4lAlYerPG6hYbLLS";

    private static final String STRIPE_EVENT = "checkout.session.completed";

    public StripeWebhookEventServiceImpl(SubscriptionPlanService subscriptionPlanService) {
        this.subscriptionPlanService = subscriptionPlanService;
    }

    @Override
    public void processEvent(StripeEvent event) {
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
}
