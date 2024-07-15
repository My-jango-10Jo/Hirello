package com.sparta.hirello.secondary.exception;

import jakarta.persistence.EntityNotFoundException;

public class ProgressNotFoundException extends EntityNotFoundException {

    public ProgressNotFoundException(Long progressId) {
        super("Progress with id " + progressId + " not found");
    }

}
