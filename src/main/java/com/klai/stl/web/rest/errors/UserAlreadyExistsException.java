package com.klai.stl.web.rest.errors;

import static com.klai.stl.web.rest.errors.ErrorConstants.ERR_CREATING_USER;

public class UserAlreadyExistsException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException() {
        super(ERR_CREATING_USER, "Error creating user!", "user", "userexists");
    }
}
