package com.klai.stl.web.rest;

import static org.springframework.http.ResponseEntity.ok;

import com.klai.stl.service.WebhookEventService;
import com.klai.stl.service.dto.webhook.StripeEvent;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookResource {

    private final Logger log = LoggerFactory.getLogger(WebhookResource.class);

    private final WebhookEventService<StripeEvent> stripeWebhookEventService;

    public WebhookResource(WebhookEventService<StripeEvent> stripeWebhookEventService) {
        this.stripeWebhookEventService = stripeWebhookEventService;
    }

    @PostMapping("/payments/stripe")
    public ResponseEntity<Void> processStripeEvent(
        @RequestHeader("Stripe-Signature") String endpointSecret,
        @Valid @RequestBody StripeEvent event
    ) {
        log.debug("Webhook event from Stripe gateway: {}", event);
        stripeWebhookEventService.processEvent(event, endpointSecret);
        return ok().build();
    }
}
