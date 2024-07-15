package com.sparta.hirello.secondary.exception;

import jakarta.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(Long userId) {
        super("User with id " + userId + " not found");
    }

}
