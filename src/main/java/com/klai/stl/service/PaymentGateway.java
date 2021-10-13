package com.klai.stl.service;

import com.klai.stl.service.reponse.GatewayProduct;

public interface PaymentGateway {
    GatewayProduct findProduct(String productReference);
}
