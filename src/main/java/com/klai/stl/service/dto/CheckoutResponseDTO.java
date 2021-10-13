package com.klai.stl.service.dto;

import java.io.Serializable;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CheckoutResponseDTO implements Serializable {

    String checkoutUrl;
}
