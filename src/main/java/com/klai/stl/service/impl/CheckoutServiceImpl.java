package com.klai.stl.service.impl;

import com.klai.stl.service.CheckoutService;
import com.klai.stl.service.PaymentGateway;
import com.klai.stl.service.reponse.CheckoutData;
import com.klai.stl.service.reponse.GatewayProduct;
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

    private final PaymentGateway paymentGateway;

    public CheckoutServiceImpl(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    @Override
    public CheckoutData checkout(String itemReference) {
        final GatewayProduct product = paymentGateway.findProduct(itemReference);
        List<Object> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");
        List<Object> lineItems = new ArrayList<>();
        Map<String, Object> lineItem1 = new HashMap<>();
        lineItem1.put("price", itemReference);
        lineItem1.put("quantity", 1);
        lineItems.add(lineItem1);
        Map<String, Object> params = new HashMap<>();
        params.put("success_url", "http://localhost:8080/success");
        params.put("cancel_url", "http://localhost:8080/cancel");
        params.put("payment_method_types", paymentMethodTypes);
        params.put("line_items", lineItems);
        params.put("mode", "subscription");

        try {
            Session session = Session.create(params);
            return CheckoutData.builder().checkoutUrl(new URL(session.getUrl())).build();
        } catch (StripeException | MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
