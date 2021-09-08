package com.klai.stl.service.exception;

public class PhotoReadException extends RuntimeException {

    public PhotoReadException() {
        super("Not a known image file");
    }
}
