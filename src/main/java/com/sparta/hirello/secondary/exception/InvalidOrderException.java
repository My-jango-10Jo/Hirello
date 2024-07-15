package com.sparta.hirello.secondary.exception;

public class InvalidOrderException extends RuntimeException {

    public InvalidOrderException() {
        super("Invalid order");
    }

}
