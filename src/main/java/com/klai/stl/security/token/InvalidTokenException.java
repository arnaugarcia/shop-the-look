package com.klai.stl.security.token;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("The token is invalid");
    }
}
