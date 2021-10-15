package com.klai.stl.service.impl;

import com.klai.stl.config.ApplicationProperties;
import com.klai.stl.config.StripeClientProperties;
import com.klai.stl.service.CheckoutService;
import com.klai.stl.service.reponse.CheckoutData;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final StripeClientProperties stripeClientProperties;

    public CheckoutServiceImpl(ApplicationProperties applicationProperties) {
        this.stripeClientProperties = applicationProperties.getStripe();
    }

    @Override
    public CheckoutData checkout(String itemReference) {
        try {
            Map<String, Object> params = buildCheckoutSessionParams(itemReference);
            Session session = Session.create(params);
            return CheckoutData.builder().checkoutUrl(new URL(session.getUrl())).build();
        } catch (StripeException | MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private Map<String, Object> buildCheckoutSessionParams(String itemReference) {
        List<Object> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");
        List<Object> lineItems = new ArrayList<>();
        Map<String, Object> lineItem1 = new HashMap<>();
        lineItem1.put("price", itemReference);
        lineItem1.put("quantity", 1);
        lineItems.add(lineItem1);
        Map<String, Object> params = new HashMap<>();
        params.put("success_url", stripeClientProperties.getSuccessUrl());
        params.put("cancel_url", stripeClientProperties.getCancelUrl());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("line_items", lineItems);
        params.put("mode", "subscription");
        return params;
    }
}
