package com.klai.stl.service.exception;

import static com.klai.stl.web.rest.errors.ErrorConstants.NOT_FOUND;

import com.klai.stl.web.rest.errors.NotFoundException;

public class UserNotFound extends NotFoundException {

    public UserNotFound() {
        super(NOT_FOUND, "No current user found", "user", "usernotfound");
    }
}
