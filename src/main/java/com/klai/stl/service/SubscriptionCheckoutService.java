package com.klai.stl.service;

import com.klai.stl.service.dto.CheckoutResponseDTO;

public interface SubscriptionCheckoutService {
    /**
     * Finds and builds the data needed for checking out the subscription plan
     * @param subscriptionPlanReference the reference of the subscription plan tu checkout
     * @return the checkout response
     */
    CheckoutResponseDTO buildCheckoutDataBySubscriptionPlanForCurrentUserCompany(String subscriptionPlanReference);
}
