package com.klai.stl.service.exception;

public class WebhookSecretError extends RuntimeException {

    public WebhookSecretError() {
        super("Invalid Webhook secret signature. What are you trying here buddy? ¬¬");
    }
}
