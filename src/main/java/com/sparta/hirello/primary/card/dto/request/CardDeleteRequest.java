package com.sparta.hirello.primary.card.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CardDeleteRequest {

    @NotBlank
    Long boardId;

    @NotBlank
    Long columnId;

}
