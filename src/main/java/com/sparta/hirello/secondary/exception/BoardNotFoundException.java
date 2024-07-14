package com.sparta.hirello.secondary.exception;

import org.apache.commons.lang3.ObjectUtils;

public class BoardNotFoundException extends NullPointerException {
    public BoardNotFoundException(String msg){
        super(msg);
    }
}
