package com.sparta.hirello.secondary.exception;

public class PermissionDeniedException extends RuntimeException{
    public PermissionDeniedException(String msg){
        super(msg);
    }
}
