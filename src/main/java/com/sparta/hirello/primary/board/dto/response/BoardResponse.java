package com.sparta.hirello.primary.board.dto.response;

import com.sparta.hirello.primary.board.entity.Board;
import lombok.Data;

@Data
public class BoardResponse {

    private Long boardId;
    private String boardName;
    private String description;
    private Long userId; // 보드 생성자 id
    private String username; // 보드 생성자 username

    private BoardResponse(Board board) {
        this.boardId = board.getId();
        this.boardName = board.getName();
        this.description = board.getDescription();
        this.userId = board.getUser().getId();
        this.username = board.getUser().getUsername();
    }

    public static BoardResponse of(Board board) {
        return new BoardResponse(board);
    }

}
