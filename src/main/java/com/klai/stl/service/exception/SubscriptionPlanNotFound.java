package com.klai.stl.service.exception;

public class SubscriptionPlanNotFound extends RuntimeException {

    public SubscriptionPlanNotFound() {
        super("Subscription plan not found");
    }
}
