package com.sparta.hirello.primary.board.dto.response;

import com.sparta.hirello.primary.user.entity.User;
import lombok.Data;

@Data
public class BoardUserVisitResponse {

    private Long userId;
    private String username;

    private BoardUserVisitResponse(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
    }

    public static BoardUserVisitResponse of(User user) {
        return new BoardUserVisitResponse(user);
    }
}
