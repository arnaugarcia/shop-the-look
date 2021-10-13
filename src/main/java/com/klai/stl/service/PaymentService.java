package com.klai.stl.service;

import com.klai.stl.service.reponse.CheckoutData;

public interface PaymentService {
    CheckoutData checkout(String itemReference);
}
