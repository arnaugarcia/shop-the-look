package com.klai.stl.service.impl;

import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.service.CheckoutService;
import com.klai.stl.service.SubscriptionCheckoutService;
import com.klai.stl.service.SubscriptionPlanService;
import com.klai.stl.service.UserService;
import com.klai.stl.service.dto.CheckoutResponseDTO;
import com.klai.stl.service.dto.requests.CheckoutRequest;
import com.klai.stl.service.reponse.CheckoutData;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionCheckoutServiceImpl implements SubscriptionCheckoutService {

    private final UserService userService;
    private final SubscriptionPlanService subscriptionService;
    private final CheckoutService checkoutService;

    public SubscriptionCheckoutServiceImpl(
        UserService userService,
        SubscriptionPlanService subscriptionService,
        CheckoutService checkoutService
    ) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.checkoutService = checkoutService;
    }

    @Override
    public CheckoutResponseDTO buildCheckoutDataBySubscriptionPlanForCurrentUserCompany(String subscriptionPlanReference) {
        return buildCheckoutDataForSubscriptionPlanAndCompany(subscriptionPlanReference, userService.getCurrentUserCompanyReference());
    }

    private CheckoutResponseDTO buildCheckoutDataForSubscriptionPlanAndCompany(String subscriptionPlanReference, String companyReference) {
        final SubscriptionPlan subscription = subscriptionService.findSubscriptionByReference(subscriptionPlanReference);

        final CheckoutRequest checkoutRequest = CheckoutRequest
            .builder()
            .itemReference(subscription.getPaymentGatewayItemReference())
            .companyReference(companyReference)
            .build();

        final CheckoutData checkout = checkoutService.checkout(checkoutRequest);

        return CheckoutResponseDTO.builder().checkoutUrl(checkout.getCheckoutUrl().toString()).build();
    }
}
