package com.klai.stl.service.webhook.stripe.exception;

public class StripeWebhookSecret extends StripeWebhookException {

    public StripeWebhookSecret() {
        super("Invalid Webhook secret signature. What are you trying here buddy? ¬¬");
    }
}
