package com.klai.stl.web.rest.errors;

public class CompanyNotFound extends NotFoundException {

    public CompanyNotFound() {
        super(ErrorConstants.NOT_FOUND, "Company not found", "company", "companynotfound");
    }
}
