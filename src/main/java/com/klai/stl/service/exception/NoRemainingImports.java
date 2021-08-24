package com.klai.stl.service.exception;

public class NoRemainingImports extends RuntimeException {

    public NoRemainingImports() {
        super("No imports left");
    }
}
