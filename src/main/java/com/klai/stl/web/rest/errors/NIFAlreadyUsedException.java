package com.klai.stl.web.rest.errors;

public class NIFAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public NIFAlreadyUsedException() {
        super(ErrorConstants.NIF_ALREADY_USED_TYPE, "NIF is already in use!", "userManagement", "nifexists");
    }
}
