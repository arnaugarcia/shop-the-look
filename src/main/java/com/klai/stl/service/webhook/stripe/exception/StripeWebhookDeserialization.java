package com.klai.stl.service.webhook.stripe.exception;

public class StripeWebhookDeserialization extends StripeWebhookException {

    public StripeWebhookDeserialization() {
        super("Deserialization for object failed");
    }
}
