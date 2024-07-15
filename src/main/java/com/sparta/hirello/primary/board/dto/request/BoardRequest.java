package com.sparta.hirello.primary.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BoardRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

}
