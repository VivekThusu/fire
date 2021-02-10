package com.embl.fire.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String firstName) {
        super("Person not found: " + firstName);
    }
}
