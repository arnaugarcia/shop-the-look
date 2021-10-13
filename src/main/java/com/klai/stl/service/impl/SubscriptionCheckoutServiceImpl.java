package com.klai.stl.service.impl;

import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.service.CheckoutService;
import com.klai.stl.service.SubscriptionCheckoutService;
import com.klai.stl.service.SubscriptionPlanService;
import com.klai.stl.service.dto.CheckoutResponseDTO;
import com.klai.stl.service.reponse.CheckoutData;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionCheckoutServiceImpl implements SubscriptionCheckoutService {

    private final SubscriptionPlanService subscriptionService;
    private final CheckoutService checkoutService;

    public SubscriptionCheckoutServiceImpl(SubscriptionPlanService subscriptionService, CheckoutService checkoutService) {
        this.subscriptionService = subscriptionService;
        this.checkoutService = checkoutService;
    }

    @Override
    public CheckoutResponseDTO getCheckoutDataForSubscriptionPlan(String subscriptionPlanReference) {
        final SubscriptionPlan subscription = subscriptionService.findSubscriptionByReference(subscriptionPlanReference);
        final CheckoutData checkout = checkoutService.checkout(subscription.getPaymentGatewayItemReference());
        return CheckoutResponseDTO.builder().checkoutUrl(checkout.getCheckoutUrl().toString()).build();
    }
}
