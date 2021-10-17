package com.klai.stl.service.exception;

public class WebhookSecretError extends RuntimeException {

    public WebhookSecretError() {
        super("Webhook secret endpoint");
    }
}
