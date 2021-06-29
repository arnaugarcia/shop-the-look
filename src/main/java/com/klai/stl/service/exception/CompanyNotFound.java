package com.klai.stl.service.exception;

public class CompanyNotFound extends RuntimeException {

    public CompanyNotFound() {
        super("Company not found");
    }
}
