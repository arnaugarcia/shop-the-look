package com.klai.stl.web.rest.api.errors;

import static com.klai.stl.web.rest.api.errors.ErrorConstants.INTERNAL_SERVER;

public class PreferencesNotFound extends NotFoundException {

    private static final long serialVersionUID = 1L;

    public PreferencesNotFound() {
        super(INTERNAL_SERVER, "No preferences are found for company", "preferences", "nopreferences");
    }
}
