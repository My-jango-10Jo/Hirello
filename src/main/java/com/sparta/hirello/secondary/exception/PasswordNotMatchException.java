package com.sparta.hirello.secondary.exception;

public class PasswordNotMatchException extends RuntimeException {

    public PasswordNotMatchException() {
    }

    public PasswordNotMatchException(String message) {
        super(message);
    }

}
