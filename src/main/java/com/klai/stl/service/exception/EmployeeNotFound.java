package com.klai.stl.service.exception;

public class EmployeeNotFound extends RuntimeException {

    public EmployeeNotFound() {
        super("Employee not found");
    }

    public EmployeeNotFound(String login) {
        super("Employee wth login " + login + " not found");
    }
}
