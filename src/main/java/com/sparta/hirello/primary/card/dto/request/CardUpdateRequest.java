package com.sparta.hirello.primary.card.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CardUpdateRequest {

    @NotNull
    Long boardId;

    @NotNull
    Long progressId;

    @NotBlank(message = "제목을 입력해주세요.")
    String title;

    String description;
    String workerName;
    LocalDateTime deadlineAt;
}
