package com.klai.stl.service.exception;

public class PhotoUploadException extends PhotoException {

    public PhotoUploadException() {
        super("No extension for was found for image");
    }
}
