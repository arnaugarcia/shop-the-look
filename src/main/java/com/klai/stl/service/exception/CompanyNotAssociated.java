package com.klai.stl.service.exception;

import lombok.Getter;

@Getter
public class CompanyNotAssociated extends RuntimeException {

    private final String login;

    public CompanyNotAssociated(String login) {
        super("Company not found for user " + login);
        this.login = login;
    }
}
