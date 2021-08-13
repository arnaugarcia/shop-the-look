package com.klai.stl.service.exception;

public class ProductNotFound extends RuntimeException {

    public ProductNotFound() {
        super("Product not found exception");
    }
}
