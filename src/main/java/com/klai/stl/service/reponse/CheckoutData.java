package com.klai.stl.service.reponse;

import java.net.URL;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CheckoutData {

    URL checkoutUrl;
}
