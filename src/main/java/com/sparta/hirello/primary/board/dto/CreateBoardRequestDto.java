package com.sparta.hirello.primary.board.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateBoardRequestDto {

    @NotNull(message = "보드 이름이 비어 있습니다.")
    private String boardName;
    @NotNull(message = "한줄 소개가 비어있습니다.")
    private String headline;

}
