package com.klai.stl.service.exception;

public class PhotoWriteException extends RuntimeException {

    public PhotoWriteException() {
        super("Cannot process the stored photo");
    }
}
