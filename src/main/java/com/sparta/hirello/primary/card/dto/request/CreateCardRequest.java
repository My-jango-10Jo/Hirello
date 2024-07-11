package com.sparta.hirello.primary.card.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateCardRequest {

    @NotNull
    Long boardId;

    @NotNull(message = "컬럼을 선택해 주세요")
    Long columnId;

    @NotBlank(message = "제목을 입력해주세요.")
    String title;

    @Size(max = 255)
    String description;

    LocalDateTime deadlineAt;

    Long workerId;

}
