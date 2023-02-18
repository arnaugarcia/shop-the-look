package com.klai.stl.service.exception;

public class StripeCheckoutException extends RuntimeException {

    public StripeCheckoutException() {
        super("There is been an error creating the Stripe Checkout session.");
    }
}
