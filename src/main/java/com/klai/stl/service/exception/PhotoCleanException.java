package com.klai.stl.service.exception;

public class PhotoCleanException extends RuntimeException {

    public PhotoCleanException() {
        super("Error removing the stored image");
    }
}
