package com.klai.stl.service;

public interface WebhookEventService<T> {
    void processEvent(T event, String secret);
}
