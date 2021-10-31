package com.klai.stl.service.client.exception;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("The provided token is not valid");
    }
}
