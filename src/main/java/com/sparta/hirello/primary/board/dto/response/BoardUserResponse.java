package com.sparta.hirello.primary.board.dto.response;

import com.sparta.hirello.primary.user.entity.User;
import lombok.Data;

@Data
public class BoardUserResponse {

    private Long userId;
    private String username;

    private BoardUserResponse(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
    }

    public static BoardUserResponse of(User user) {
        return new BoardUserResponse(user);
    }
}
