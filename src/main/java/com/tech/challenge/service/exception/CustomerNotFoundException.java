package com.tech.challenge.service.exception;

public class CustomerNotFoundException extends NotFoundException {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
