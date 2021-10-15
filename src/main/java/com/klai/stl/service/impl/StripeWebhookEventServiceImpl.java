package com.klai.stl.service.impl;

import com.klai.stl.service.SubscriptionPlanService;
import com.klai.stl.service.WebhookEventService;
import com.klai.stl.service.dto.requests.UpdateSubscriptionRequest;
import com.klai.stl.web.rest.WebhookResource;
import com.stripe.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StripeWebhookEventServiceImpl implements WebhookEventService<Event> {

    private final Logger log = LoggerFactory.getLogger(WebhookResource.class);

    private final SubscriptionPlanService subscriptionPlanService;

    public StripeWebhookEventServiceImpl(SubscriptionPlanService subscriptionPlanService) {
        this.subscriptionPlanService = subscriptionPlanService;
    }

    @Override
    public void processEvent(Event event) {
        log.info("Processing event ({}) of stripe webhook", event.getId());
        final UpdateSubscriptionRequest updateSubscriptionRequest = UpdateSubscriptionRequest
            .builder()
            .subscriptionReference("test")
            .build();
        final String companyReference = event.getData().getPreviousAttributes().get("company").toString();
        subscriptionPlanService.updateSubscriptionPlanForCompany(companyReference, updateSubscriptionRequest);
        log.info("Finished processing event ({}) of stripe webhook", event.getId());
    }
}
