package com.klai.stl.service.impl;

import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.service.PaymentService;
import com.klai.stl.service.SubscriptionCheckoutService;
import com.klai.stl.service.SubscriptionPlanService;
import com.klai.stl.service.dto.CheckoutResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionCheckoutServiceImpl implements SubscriptionCheckoutService {

    private final SubscriptionPlanService subscriptionService;
    private final PaymentService paymentService;

    public SubscriptionCheckoutServiceImpl(SubscriptionPlanService subscriptionService, PaymentService paymentService) {
        this.subscriptionService = subscriptionService;
        this.paymentService = paymentService;
    }

    @Override
    public CheckoutResponseDTO getCheckoutDataForSubscriptionPlan(String subscriptionPlanReference) {
        final SubscriptionPlan subscription = subscriptionService.findSubscriptionByReference(subscriptionPlanReference);
    }
}
