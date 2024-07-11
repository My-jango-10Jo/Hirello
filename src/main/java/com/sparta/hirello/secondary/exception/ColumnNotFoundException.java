package com.sparta.hirello.secondary.exception;

public class ColumnNotFoundException extends NullPointerException{
    public ColumnNotFoundException(String msg){
        super(msg);
    }
}
