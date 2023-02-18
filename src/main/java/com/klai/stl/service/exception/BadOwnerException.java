package com.klai.stl.service.exception;

public class BadOwnerException extends RuntimeException {

    public BadOwnerException() {
        super("You aren't the owner of this");
    }
}
