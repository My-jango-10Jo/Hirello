package com.sparta.hirello.primary.board.dto.response;

import com.sparta.hirello.primary.board.entity.Board;
import lombok.Data;

@Data
public class BoardResponse {

    private Long id;
    private String title;
    private String description;

    private BoardResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.description = board.getDescription();
    }

    public static BoardResponse of(Board board) {
        return new BoardResponse(board);
    }

}
