package com.sparta.hirello.secondary.exception;

public class BoardNotFoundException extends NullPointerException {
    public BoardNotFoundException(String msg){
        super(msg);
    }
}
