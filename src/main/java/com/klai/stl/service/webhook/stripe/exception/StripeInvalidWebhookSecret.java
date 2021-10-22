package com.klai.stl.service.webhook.stripe.exception;

public class StripeInvalidWebhookSecret extends StripeWebhookException {

    public StripeInvalidWebhookSecret() {
        super("Invalid Webhook secret signature. What are you trying here buddy? ¬¬");
    }
}
