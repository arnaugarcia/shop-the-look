package com.klai.stl.service.exception;

public class OwnDeletionException extends RuntimeException {

    public OwnDeletionException() {
        super("Cannot remove yourself from a company");
    }
}
