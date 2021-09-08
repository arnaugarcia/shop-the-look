package com.klai.stl.service.exception;

public class PhotoExtensionException extends RuntimeException {

    public PhotoExtensionException() {
        super("No extension for was found for image");
    }
}
