package com.klai.stl.service.exception;

public class ObjectNotFoundException extends CloudStorageException {

    public ObjectNotFoundException(String objectPath) {
        super("Object " + objectPath + " not found in cloud storage");
    }
}
