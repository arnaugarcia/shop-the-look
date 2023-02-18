package com.klai.stl.service.exception;

public class FeedException extends RuntimeException {

    public FeedException() {
        super("There is been an error obtaining products from the feed");
    }
}
