package com.klai.stl.service.impl;

import static com.klai.stl.config.Constants.STRIPE_CHECKOUT_PARAM_COMPANY_KEY;
import static com.klai.stl.config.Constants.STRIPE_CHECKOUT_PARAM_SUBSCRIPTION_KEY;

import com.klai.stl.config.ApplicationProperties;
import com.klai.stl.config.StripeClientProperties;
import com.klai.stl.service.CheckoutService;
import com.klai.stl.service.dto.requests.CheckoutRequest;
import com.klai.stl.service.exception.StripeCheckoutException;
import com.klai.stl.service.reponse.CheckoutData;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private static final Logger log = LoggerFactory.getLogger(CheckoutServiceImpl.class);

    private final StripeClientProperties stripeClientProperties;

    public CheckoutServiceImpl(ApplicationProperties applicationProperties) {
        this.stripeClientProperties = applicationProperties.getStripe();
    }

    @Override
    public CheckoutData checkout(CheckoutRequest checkoutRequest) {
        try {
            Map<String, Object> params = buildCheckoutSessionParams(checkoutRequest);
            log.info("Created a session with params: {}", params);
            Session session = Session.create(params);
            return CheckoutData.builder().checkoutUrl(new URL(session.getUrl())).build();
        } catch (StripeException | MalformedURLException e) {
            e.printStackTrace();
            throw new StripeCheckoutException();
        }
    }

    private Map<String, Object> buildCheckoutSessionParams(CheckoutRequest checkoutRequest) {
        List<Object> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        List<Object> lineItems = new ArrayList<>();
        Map<String, Object> lineItem1 = new HashMap<>();
        lineItem1.put("price", checkoutRequest.getItemReference());
        lineItem1.put("quantity", 1);

        List<String> taxRates = new ArrayList<>();
        taxRates.add(stripeClientProperties.getTaxRate());
        lineItem1.put("tax_rates", taxRates);
        lineItems.add(lineItem1);

        Map<String, String> metadata = new HashMap<>();
        metadata.put(STRIPE_CHECKOUT_PARAM_COMPANY_KEY, checkoutRequest.getCompanyReference());
        metadata.put(STRIPE_CHECKOUT_PARAM_SUBSCRIPTION_KEY, checkoutRequest.getSubscriptionReference());

        Map<String, Object> params = new HashMap<>();
        params.put("success_url", stripeClientProperties.getSuccessUrl());
        params.put("cancel_url", stripeClientProperties.getCancelUrl());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("line_items", lineItems);

        if (checkoutRequest.getTrialPeriod()) {
            Map<String, Object> subscriptionData = new HashMap<>();
            subscriptionData.put("trial_period_days", stripeClientProperties.getTrialPeriodDays());
            params.put("subscription_data", subscriptionData);
        }

        params.put("mode", "subscription");
        params.put("allow_promotion_codes", "true");
        params.put("client_reference_id", checkoutRequest.getCompanyReference());
        params.put("metadata", metadata);
        return params;
    }
}
