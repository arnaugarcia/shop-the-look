package com.klai.stl.service.exception;

public class PreferenceNotFound extends RuntimeException {

    public PreferenceNotFound() {
        super("Preference not found");
    }
}
