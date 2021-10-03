package com.klai.stl.service.exception;

public class PhotoExtensionException extends PhotoException {

    public PhotoExtensionException() {
        super("No extension for was found for image");
    }
}
