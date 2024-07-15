package com.sparta.hirello.secondary.exception;

public class NotBoardManagerException extends RuntimeException {

    public NotBoardManagerException() {
        super("해당 보드의 매니저가 아닙니다.");
    }

}
