package com.sparta.hirello.primary.card.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CardMoveOrderRequest {

    @NotNull
    private int newOrder;

    private Long newProgressId;

}
