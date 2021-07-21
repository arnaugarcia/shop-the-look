package com.klai.stl.service.exception;

public class PreferencesNotFound extends RuntimeException {

    public PreferencesNotFound() {
        super("Preference not found");
    }
}
