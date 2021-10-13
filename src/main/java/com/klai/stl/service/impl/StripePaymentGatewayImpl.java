package com.klai.stl.service.impl;

import com.klai.stl.service.PaymentGateway;
import com.klai.stl.service.exception.PaymentGatewayException;
import com.klai.stl.service.reponse.GatewayProduct;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentGatewayImpl implements PaymentGateway {

    @Override
    public GatewayProduct findProduct(String productReference) {
        try {
            Price price = Price.retrieve(productReference);
            return GatewayProduct.from(price);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentGatewayException(e.getMessage());
        }
    }
}
