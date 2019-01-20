package com.urlshortener.api.exception;

public class UnvailableKeyException extends RuntimeException {

    public UnvailableKeyException(final String description) {
        super(description);
    }
}
