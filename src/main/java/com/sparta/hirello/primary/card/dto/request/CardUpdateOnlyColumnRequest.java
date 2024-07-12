package com.sparta.hirello.primary.card.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CardUpdateOnlyColumnRequest {

    @NotNull
    Long boardId;

    @NotNull
    Long columnId;
}
