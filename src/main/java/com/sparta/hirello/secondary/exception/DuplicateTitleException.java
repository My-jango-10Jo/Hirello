package com.sparta.hirello.secondary.exception;

public class DuplicateTitleException extends RuntimeException {

    public DuplicateTitleException(String title) {
        super("Duplicate title: " + title);
    }

}
