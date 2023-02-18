package com.klai.stl.web.rest.webhook;

import static org.springframework.http.ResponseEntity.ok;

import com.klai.stl.service.webhook.WebhookEventService;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks")
public class WebhookResource {

    private final Logger log = LoggerFactory.getLogger(WebhookResource.class);

    private final WebhookEventService<String> stripeWebhookEventService;

    public WebhookResource(WebhookEventService<String> stripeWebhookEventService) {
        this.stripeWebhookEventService = stripeWebhookEventService;
    }

    @PostMapping("/payments/stripe")
    public ResponseEntity<Void> processStripeEvent(
        @RequestHeader("Stripe-Signature") String headerSignature,
        @Valid @RequestBody String event
    ) {
        log.debug("Webhook event from Stripe gateway: {}", event);
        stripeWebhookEventService.processEvent(event, headerSignature);
        return ok().build();
    }
}
