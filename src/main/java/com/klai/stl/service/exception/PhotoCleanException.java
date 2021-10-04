package com.klai.stl.service.exception;

public class PhotoCleanException extends PhotoException {

    public PhotoCleanException(String message) {
        super("Error removing the stored image: " + message);
    }
}
