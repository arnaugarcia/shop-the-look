package com.klai.stl.service.impl;

import static java.util.Objects.isNull;

import com.klai.stl.domain.Company;
import com.klai.stl.domain.SubscriptionPlan;
import com.klai.stl.service.*;
import com.klai.stl.service.dto.CheckoutResponseDTO;
import com.klai.stl.service.dto.requests.CheckoutRequest;
import com.klai.stl.service.reponse.CheckoutData;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionCheckoutServiceImpl implements SubscriptionCheckoutService {

    private final UserService userService;
    private final SubscriptionPlanService subscriptionService;
    private final CheckoutService checkoutService;
    private final CompanyService companyService;

    public SubscriptionCheckoutServiceImpl(
        UserService userService,
        SubscriptionPlanService subscriptionService,
        CheckoutService checkoutService,
        CompanyService companyService
    ) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.checkoutService = checkoutService;
        this.companyService = companyService;
    }

    @Override
    public CheckoutResponseDTO buildCheckoutDataBySubscriptionPlanForCurrentUserCompany(String subscriptionPlanReference) {
        return buildCheckoutDataForSubscriptionPlanAndCompany(subscriptionPlanReference, userService.getCurrentUserCompanyReference());
    }

    private CheckoutResponseDTO buildCheckoutDataForSubscriptionPlanAndCompany(String subscriptionPlanReference, String companyReference) {
        final SubscriptionPlan subscription = subscriptionService.findSubscriptionByReference(subscriptionPlanReference);
        final Company company = companyService.findByReference(companyReference);

        final boolean notHaveASubscription = isNull(company.getSubscriptionPlan());

        final CheckoutRequest checkoutRequest = CheckoutRequest
            .builder()
            .itemReference(subscription.getPaymentGatewayItemReference())
            .companyReference(companyReference)
            .trialPeriod(notHaveASubscription)
            .subscriptionReference(subscription.getReference())
            .build();

        final CheckoutData checkout = checkoutService.checkout(checkoutRequest);

        return CheckoutResponseDTO.builder().checkoutUrl(checkout.getCheckoutUrl().toString()).build();
    }
}
