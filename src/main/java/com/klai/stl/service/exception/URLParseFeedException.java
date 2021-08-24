package com.klai.stl.service.exception;

public class URLParseFeedException extends RuntimeException {

    public URLParseFeedException() {
        super("The feed URL is not valid");
    }
}
