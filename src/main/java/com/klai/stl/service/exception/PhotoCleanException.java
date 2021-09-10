package com.klai.stl.service.exception;

public class PhotoCleanException extends PhotoException {

    public PhotoCleanException() {
        super("Error removing the stored image");
    }
}
