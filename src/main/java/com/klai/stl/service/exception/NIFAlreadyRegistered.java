package com.klai.stl.service.exception;

public class NIFAlreadyRegistered extends RuntimeException {

    public NIFAlreadyRegistered() {
        super("NIF already registered in the system");
    }
}
