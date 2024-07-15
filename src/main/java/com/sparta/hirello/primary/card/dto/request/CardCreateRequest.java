package com.sparta.hirello.primary.card.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CardCreateRequest {

    @NotNull
    private Long progressId;

    @NotBlank
    private String title;

    private String description;

    private LocalDateTime deadline;

    private Long workerId;

}
