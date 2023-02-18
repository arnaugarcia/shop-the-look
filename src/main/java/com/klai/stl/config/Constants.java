package com.klai.stl.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "es";
    public static final String ADMIN_COMPANY_NIF = "B42951921";

    public static final String STRIPE_CHECKOUT_PARAM_COMPANY_KEY = "company_reference";
    public static final String STRIPE_CHECKOUT_PARAM_SUBSCRIPTION_KEY = "subscription_reference";

    public static final Integer DEFAULT_REMAINING_IMPORTS = 10;

    private Constants() {}
}
