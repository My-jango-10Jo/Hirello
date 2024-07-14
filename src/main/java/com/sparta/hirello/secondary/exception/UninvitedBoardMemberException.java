package com.sparta.hirello.secondary.exception;

public class UninvitedBoardMemberException extends RuntimeException {
    public UninvitedBoardMemberException() {
        super("회원이 일치하지 않습니다.");
    }

    public UninvitedBoardMemberException(String message) {
        super(message);
    }
}
