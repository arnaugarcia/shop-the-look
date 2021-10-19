package com.klai.stl.service.reponse;

import com.stripe.model.Price;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GatewayProduct {

    String id;
    Integer price;
    Boolean active;

    public static GatewayProduct from(Price price) {
        return GatewayProduct.builder().id(price.getId()).price(price.getUnitAmount().intValue()).active(price.getActive()).build();
    }
}
