package com.klai.stl.service.exception;

public abstract class CloudStorageException extends RuntimeException {

    public CloudStorageException(String message) {
        super(message);
    }
}
