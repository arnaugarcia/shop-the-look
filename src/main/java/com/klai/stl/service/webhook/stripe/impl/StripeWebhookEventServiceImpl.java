package com.klai.stl.service.webhook.stripe.impl;

import static java.util.Objects.isNull;

import com.klai.stl.config.ApplicationProperties;
import com.klai.stl.service.SubscriptionPlanService;
import com.klai.stl.service.dto.requests.UpdateSubscriptionRequest;
import com.klai.stl.service.webhook.WebhookEventService;
import com.klai.stl.service.webhook.stripe.dto.StripeMetadata;
import com.klai.stl.service.webhook.stripe.exception.StripeWebhookDeserialization;
import com.klai.stl.service.webhook.stripe.exception.StripeWebhookSecret;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StripeWebhookEventServiceImpl implements WebhookEventService<String> {

    private final Logger log = LoggerFactory.getLogger(StripeWebhookEventServiceImpl.class);

    private final SubscriptionPlanService subscriptionPlanService;

    private final String WEBHOOK_SECRET;

    private static final String STRIPE_EVENT = "checkout.session.completed";

    public StripeWebhookEventServiceImpl(SubscriptionPlanService subscriptionPlanService, ApplicationProperties applicationProperties) {
        this.subscriptionPlanService = subscriptionPlanService;
        this.WEBHOOK_SECRET = applicationProperties.getStripe().getWebhookSecret();
    }

    @Override
    public void processEvent(String payload, String headerSignature) {
        Event event = buildEventAndCheckSignature(payload, headerSignature);
        if (!event.getType().equals(STRIPE_EVENT)) {
            log.info("Excluding event ({}) of Stripe webhook", event.getType());
            return;
        }

        Session session = buildSessionFrom(event);
        StripeMetadata stripeMetadata = StripeMetadata.from(session.getMetadata());

        final UpdateSubscriptionRequest updateSubscriptionRequest = UpdateSubscriptionRequest
            .builder()
            .subscriptionReference(stripeMetadata.getSubscriptionReference())
            .companyReference(stripeMetadata.getCompanyReference())
            .build();

        subscriptionPlanService.updateSubscriptionPlanForCompany(updateSubscriptionRequest);
        log.info(
            "Finished processing event ({}) of stripe webhook for company ({}) for subscription ({})",
            event.getId(),
            stripeMetadata.getCompanyReference(),
            stripeMetadata.getSubscriptionReference()
        );
    }

    private Session buildSessionFrom(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        final StripeObject stripeObject = dataObjectDeserializer.getObject().orElseThrow(StripeWebhookDeserialization::new);
        log.info("Processing stripe webhook object ({})", stripeObject);
        Session session = (Session) stripeObject;
        if (isNull(session)) {
            throw new StripeWebhookDeserialization();
        }
        return session;
    }

    private Event buildEventAndCheckSignature(String payload, String headerSignature) {
        try {
            return Webhook.constructEvent(payload, headerSignature, WEBHOOK_SECRET);
        } catch (Exception e) {
            throw new StripeWebhookSecret();
        }
    }
}
