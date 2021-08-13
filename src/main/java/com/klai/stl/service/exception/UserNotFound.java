package com.klai.stl.service.exception;

public class UserNotFound extends RuntimeException {

    public UserNotFound() {
        super("User not found");
    }
}
