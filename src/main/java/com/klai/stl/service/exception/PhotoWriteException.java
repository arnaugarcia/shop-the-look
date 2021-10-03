package com.klai.stl.service.exception;

public class PhotoWriteException extends PhotoException {

    public PhotoWriteException() {
        super("Cannot process the stored photo");
    }
}
