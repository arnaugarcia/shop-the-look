package com.klai.stl.service.exception;

public class CompanyReferenceNotFound extends RuntimeException {

    public CompanyReferenceNotFound() {
        super("Company reference not found!");
    }
}
