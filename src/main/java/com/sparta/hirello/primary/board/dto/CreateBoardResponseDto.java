package com.sparta.hirello.primary.board.dto;

import com.sparta.hirello.primary.board.entity.Board;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateBoardResponseDto {

    private final String boardName;
    private final String headline;
    private final String username;

    public CreateBoardResponseDto(Board board, String username) {
        this.boardName = board.getBoardName();
        this.headline = board.getHeadline();
        this.username = username;
    }
}
