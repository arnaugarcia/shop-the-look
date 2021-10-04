package com.klai.stl.service.exception;

public class PhotoUploadException extends PhotoException {

    public PhotoUploadException() {
        super("There's been an error uploading the image");
    }
}
