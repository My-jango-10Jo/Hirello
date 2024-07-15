package com.sparta.hirello.primary.progress.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProgressMoveOrderRequest {

    @NotNull
    private int newOrder;

}
