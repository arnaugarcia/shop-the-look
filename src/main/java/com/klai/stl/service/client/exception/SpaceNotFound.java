package com.klai.stl.service.client.exception;

public class SpaceNotFound extends RuntimeException {

    public SpaceNotFound(String reference) {
        super("Space with reference " + reference + " not found");
    }
}
