package com.sparta.hirello.secondary.exception;

public class ColumnDuplicatedException extends RuntimeException{
    public ColumnDuplicatedException(String msg){
        super(msg);
    }
}
