package com.klai.stl.web.rest;

import static org.springframework.http.ResponseEntity.ok;

import com.klai.stl.service.WebhookEventService;
import com.stripe.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class WebhookResource {

    private final Logger log = LoggerFactory.getLogger(WebhookResource.class);

    private final WebhookEventService<Event> stripeWebhookEventService;

    public WebhookResource(WebhookEventService<Event> stripeWebhookEventService) {
        this.stripeWebhookEventService = stripeWebhookEventService;
    }

    @PostMapping("/payments/stripe")
    public ResponseEntity<Void> findSubscriptionForCompany(@RequestBody Event event) {
        log.debug("Webhook event from Stripe gateway: {}", event);
        stripeWebhookEventService.processEvent(event);
        return ok().build();
    }
}
