package com.sparta.hirello.primary.board.dto.response;

import com.sparta.hirello.primary.board.entity.BoardMember;
import com.sparta.hirello.primary.board.entity.BoardRole;
import lombok.Data;

@Data
public class BoardMemberResponse {

    private Long boardId;
    private Long userId;
    private BoardRole boardRole; // [USER, MANAGER]

    private BoardMemberResponse(BoardMember boardMember) {
        this.userId = boardMember.getUser().getId();
        this.boardId = boardMember.getBoard().getId();
        this.boardRole = boardMember.getBoardRole();
    }

    public static BoardMemberResponse of(BoardMember boardMember) {
        return new BoardMemberResponse(boardMember);
    }

}
