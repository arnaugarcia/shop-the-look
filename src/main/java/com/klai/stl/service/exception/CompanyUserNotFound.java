package com.klai.stl.service.exception;

public class CompanyUserNotFound extends RuntimeException {

    public CompanyUserNotFound() {
        super("No user was found for this company");
    }
}
