package com.klai.stl.service.exception;

public class PaymentGatewayException extends RuntimeException {

    public PaymentGatewayException(String message) {
        super("There is been an error in the gateway process: " + message);
    }
}
