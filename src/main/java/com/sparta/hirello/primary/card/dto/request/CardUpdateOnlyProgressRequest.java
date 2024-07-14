package com.sparta.hirello.primary.card.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CardUpdateOnlyProgressRequest {

    @NotNull
    Long boardId;

    @NotNull
    Long progressId;
}
