package com.sparta.hirello.primary.board.dto.response;

import com.sparta.hirello.primary.board.entity.BoardMember;
import lombok.Data;

@Data
public class BoardMemberResponse {

    private Long userId;
    private Long boardId;

    private BoardMemberResponse(BoardMember boardMember) {
        this.userId = boardMember.getUser().getId();
        this.boardId = boardMember.getBoard().getId();
    }

    public static BoardMemberResponse of(BoardMember boardMember) {
        return new BoardMemberResponse(boardMember);
    }

}
