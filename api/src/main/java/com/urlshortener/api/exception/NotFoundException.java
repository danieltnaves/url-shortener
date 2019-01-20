package com.urlshortener.api.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(final String description) {
        super(description);
    }
}
