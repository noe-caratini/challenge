package com.tech.challenge.service.exception;

public abstract class NotFoundException extends Exception {

    public NotFoundException(String message) {
        super(message);
    }
}
