package com.sparta.hirello.secondary.exception;

public class UninvitedBoardMemberException extends RuntimeException {

    public UninvitedBoardMemberException() {
        super("해당 보드에 초대받지 않은 사용자입니다.");
    }

}
