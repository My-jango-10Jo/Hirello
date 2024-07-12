package com.sparta.hirello.primary.board.dto.response;

import com.sparta.hirello.primary.board.entity.Board;
import lombok.Data;

@Data
public class BoardResponse {

    private final Long boardId;
    private final String boardName;
    private final String headline;
    private final Long userId;
    private final String username;

    private BoardResponse(Board board) {
        this.boardId = board.getBoardId();
        this.boardName = board.getBoardName();
        this.headline = board.getHeadline();
        this.userId = board.getUser().getId();
        this.username = board.getUser().getUsername();
    }

    public static BoardResponse of(Board board) {
        return new BoardResponse(board);
    }

}
