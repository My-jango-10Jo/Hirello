package com.sparta.hirello.primary.board.dto.request;

import com.sparta.hirello.primary.board.entity.BoardRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BoardMemberRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private BoardRole role;

}
