package com.klai.stl.web.rest.errors;

import java.net.URI;

public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "https://www.jhipster.tech/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI BAD_REQUEST = URI.create(PROBLEM_BASE_URL + "/400");
    public static final URI TOO_MANY_REQUESTS = URI.create(PROBLEM_BASE_URL + "/429");
    public static final URI NOT_FOUND = URI.create(PROBLEM_BASE_URL + "/404");
    public static final URI FORBIDDEN = URI.create(PROBLEM_BASE_URL + "/403");
    public static final URI INTERNAL_SERVER = URI.create(PROBLEM_BASE_URL + "/500");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI INVALID_PASSWORD_TYPE = URI.create(PROBLEM_BASE_URL + "/invalid-password");
    public static final URI EMAIL_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/email-already-used");
    public static final URI NIF_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/nif-already-used");
    public static final URI LOGIN_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/login-already-used");

    private ErrorConstants() {}
}
