package com.sparta.hirello.primary.board.dto.response;

import com.sparta.hirello.primary.board.entity.Board;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoardResponseDto {

    private final Long boardId;
    private final String boardName;
    private final String headline;
    private final String username;

    public BoardResponseDto(Board board, String username) {
        this.boardId = board.getBoardId();
        this.boardName = board.getBoardName();
        this.headline = board.getHeadline();
        this.username = username;
    }
}
