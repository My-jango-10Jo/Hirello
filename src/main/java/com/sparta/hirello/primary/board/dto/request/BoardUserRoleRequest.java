package com.sparta.hirello.primary.board.dto.request;

import com.sparta.hirello.primary.board.entity.BoardAuthority;
import lombok.Data;

@Data
public class BoardUserRoleRequest {

    private Long userId;
    private BoardAuthority boardAuthority;
}
