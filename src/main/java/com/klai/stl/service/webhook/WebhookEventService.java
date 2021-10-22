package com.klai.stl.service.webhook;

public interface WebhookEventService<T> {
    void processEvent(T event, String headerSignature);
}
