package com.klai.stl.service.webhook.stripe.exception;

public class StripeWebhookDeserializationError extends StripeWebhookException {

    public StripeWebhookDeserializationError() {
        super("Deserialization for event failed");
    }
}
