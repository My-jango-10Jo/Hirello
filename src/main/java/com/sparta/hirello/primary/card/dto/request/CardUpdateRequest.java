package com.sparta.hirello.primary.card.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CardUpdateRequest {
    @NotBlank
    Long boardId;
    @NotBlank
    Long columnId;

    @NotBlank(message = "제목을 입력해주세요.")
    String title;

    String description;
    String workerName;
    LocalDateTime deadlineAt;
}
