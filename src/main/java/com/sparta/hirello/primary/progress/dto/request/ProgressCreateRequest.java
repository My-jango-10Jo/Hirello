package com.sparta.hirello.primary.progress.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProgressCreateRequest {

    @NotNull
    private Long boardId;

    @NotBlank
    private String title;

}
