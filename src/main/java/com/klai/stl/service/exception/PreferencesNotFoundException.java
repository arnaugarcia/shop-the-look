package com.klai.stl.service.exception;

public class PreferencesNotFoundException extends RuntimeException {

    public PreferencesNotFoundException() {
        super("Preference not found");
    }
}
