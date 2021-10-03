package com.klai.stl.service.exception;

public class PhotoReadException extends PhotoException {

    public PhotoReadException() {
        super("Not a known image file");
    }
}
