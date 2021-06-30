package com.klai.stl.service.exception;

public class EmployeeNotFound extends RuntimeException {

    public EmployeeNotFound() {
        super("No user was found for this company");
    }
}
