package com.klai.stl.service.webhook.stripe.exception;

public abstract class StripeWebhookException extends RuntimeException {

    public StripeWebhookException(String message) {
        super(message);
    }
}
