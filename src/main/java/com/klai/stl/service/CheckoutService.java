package com.klai.stl.service;

import com.klai.stl.service.dto.requests.CheckoutRequest;
import com.klai.stl.service.reponse.CheckoutData;

public interface CheckoutService {
    CheckoutData checkout(CheckoutRequest checkoutRequest);
}
