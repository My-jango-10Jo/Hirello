package com.sparta.hirello.primary.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BoardRequest {

    @NotBlank(message = "보드 이름이 비어 있습니다.")
    private String name;

    @NotBlank(message = "보드 설명이 비어 있습니다.")
    private String description;

}
